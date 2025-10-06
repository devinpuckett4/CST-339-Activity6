param(
  [string]$SourceProject = "topic3-2",
  [string]$DbHost = "localhost",
  [int]$DbPort = 3306,
  [string]$DbName = "cst339",
  [string]$DbUser = "root",
  [string]$DbPass = "root"
)
$ErrorActionPreference = "Stop"

function Copy-Project { param($fromPath,$toPath)
  if (Test-Path $toPath) { Write-Host ">> Removing existing $toPath"; Remove-Item -Recurse -Force $toPath }
  Write-Host ">> Copying $fromPath -> $toPath"
  Copy-Item -Recurse -Force $fromPath $toPath
}

function Get-BasePackage { param($projPath)
  $app = Get-ChildItem -Path (Join-Path $projPath "src/main/java") -Recurse -Include "*Application.java" -ErrorAction SilentlyContinue | Select-Object -First 1
  if ($app) { $content = Get-Content $app.FullName -Raw } else {
    $any = Get-ChildItem -Path (Join-Path $projPath "src/main/java") -Recurse -Include "*.java" | Select-Object -First 1
    if (-not $any) { return "com.gcu" }
    $content = Get-Content $any.FullName -Raw
  }
  if ($content -match 'package\s+([A-Za-z0-9_.]+);') { return $Matches[1] } else { return "com.gcu" }
}

function Rename-AppClass { param($projPath,$newClass)
  $app = Get-ChildItem -Path (Join-Path $projPath "src/main/java") -Recurse -Include "*Application.java" | Select-Object -First 1
  if (-not $app) { Write-Warning "No *Application.java found in $projPath"; return }
  $text = Get-Content $app.FullName -Raw
  $oldClass = $newClass
  if ($text -match 'class\s+([A-Za-z0-9_]+)\s*{') { $oldClass = $Matches[1] }
  $text = $text -replace "class\s+$oldClass", "class $newClass"
  $text = $text -replace [regex]::Escape("$oldClass.class"), "$newClass.class"
  Set-Content -Path $app.FullName -Value $text -Encoding UTF8
  $dir = Split-Path $app.FullName -Parent
  if ((Split-Path $app.FullName -Leaf) -ne ($newClass + ".java")) {
    Rename-Item -Path $app.FullName -NewName ($newClass + ".java")
  }
}

function Update-Pom { param($projPath,$artifact,$depsToAdd)
  $pomPath = Join-Path $projPath "pom.xml"
  if (-not (Test-Path $pomPath)) { throw "pom.xml not found in $projPath" }
  [xml]$pom = Get-Content $pomPath
  $pom.project.artifactId = $artifact
  $pom.project.name = $artifact
  if (-not $pom.project.dependencies) {
    $deps = $pom.CreateElement("dependencies")
    $pom.project.AppendChild($deps) | Out-Null
  }
  $depsNode = $pom.project.dependencies
  function Ensure-Dep($pom,$depsNode,[string]$groupId,[string]$artifactId){
    $exists = $depsNode.dependency | Where-Object { $_.groupId -eq $groupId -and $_.artifactId -eq $artifactId }
    if (-not $exists) {
      $dep = $pom.CreateElement("dependency")
      $g = $pom.CreateElement("groupId");   $g.InnerText = $groupId; $dep.AppendChild($g) | Out-Null
      $a = $pom.CreateElement("artifactId");$a.InnerText = $artifactId; $dep.AppendChild($a) | Out-Null
      $depsNode.AppendChild($dep) | Out-Null
    }
  }
  if ($depsToAdd -contains "jdbc") {
    Ensure-Dep $pom $depsNode "org.springframework.boot" "spring-boot-starter-jdbc"
    Ensure-Dep $pom $depsNode "com.mysql" "mysql-connector-j"
  }
  if ($depsToAdd -contains "datajdbc") {
    Ensure-Dep $pom $depsNode "org.springframework.boot" "spring-boot-starter-data-jdbc"
  }
  $pom.Save($pomPath)
}

function Write-AppProps { param($projPath,$dbHost,$dbPort,$dbName,$dbUser,$dbPass)
  $resDir = Join-Path $projPath "src/main/resources"
  New-Item -ItemType Directory -Force -Path $resDir | Out-Null
  $propsPath = Join-Path $resDir "application.properties"
  $props = @"
spring.application.name=$((Split-Path $projPath -Leaf))
spring.datasource.url=jdbc:mysql://$dbHost`:$dbPort/$dbName
spring.datasource.username=$dbUser
spring.datasource.password=$dbPass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.sql.init.mode=never
"@
  Set-Content -Path $propsPath -Value $props -Encoding UTF8
}

function New-JavaFile { param($path,$content)
  New-Item -ItemType Directory -Force -Path (Split-Path $path -Parent) | Out-Null
  Set-Content -Path $path -Value $content -Encoding UTF8
}

function Scaff-DAO-Interface { param($projPath,$basePkg)
  $pkgPath = (Join-Path $projPath ("src/main/java/" + ($basePkg -replace '\.','/')))
  $file = Join-Path $pkgPath "data/DataAccessInterface.java"
  $content = @"
package $basePkg.data;
import java.util.List;
public interface DataAccessInterface<T> {
    java.util.List<T> findAll();
    T findById(int id);
    boolean create(T t);
    boolean update(T t);
    boolean delete(T t);
}
"@
  New-JavaFile -path $file -content $content
}

function Scaff-Part1-OrdersDataService { param($projPath,$basePkg)
  $pkgPath = (Join-Path $projPath ("src/main/java/" + ($basePkg -replace '\.','/')))
  $file = Join-Path $pkgPath "data/OrdersDataService.java"
  $content = @"
package $basePkg.data;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import $basePkg.model.OrderModel;
@Service
public class OrdersDataService implements DataAccessInterface<OrderModel> {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplateObject;
    public OrdersDataService(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    @Override
    public List<OrderModel> findAll() {
        List<OrderModel> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ORDERS";
            SqlRowSet srs = jdbcTemplateObject.queryForRowSet(sql);
            while (srs.next()) {
                orders.add(new OrderModel(
                    srs.getLong("ID"),
                    srs.getString("ORDER_NO"),
                    srs.getString("PRODUCT_NAME"),
                    srs.getFloat("PRICE"),
                    srs.getInt("QUANTITY")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return orders;
    }
    @Override public OrderModel findById(int id) { return null; }
    @Override public boolean create(OrderModel order) {
        try {
            String sql = "INSERT INTO ORDERS(ORDER_NO, PRODUCT_NAME, PRICE, QUANTITY) VALUES(?, ?, ?, ?)";
            int rows = jdbcTemplateObject.update(sql, order.getOrderNo(), order.getProductName(), order.getPrice(), order.getQuantity());
            return rows == 1;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }
    @Override public boolean update(OrderModel order) { return true; }
    @Override public boolean delete(OrderModel order) { return true; }
}
"@
  New-JavaFile -path $file -content $content
}

function Scaff-Part2-EntitiesAndRepo { param($projPath,$basePkg)
  $root = (Join-Path $projPath ("src/main/java/" + ($basePkg -replace '\.','/')))
  $entity = Join-Path $root "data/entity/OrderEntity.java"
  $mapper = Join-Path $root "data/mapper/OrderRowMapper.java"
  $repo   = Join-Path $root "data/repository/OrdersRepository.java"
  $service= Join-Path $root "data/OrdersDataService.java"

  $entityContent = @"
package $basePkg.data.entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
@Table("ORDERS")
public class OrderEntity {
    @Id private Long id;
    @Column("ORDER_NO") private String orderNo;
    @Column("PRODUCT_NAME") private String productName;
    @Column("PRICE") private float price;
    @Column("QUANTITY") private int quantity;
    public OrderEntity(){}
    public OrderEntity(Long id,String orderNo,String productName,float price,int quantity){
        this.id=id;this.orderNo=orderNo;this.productName=productName;this.price=price;this.quantity=quantity;
    }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getOrderNo(){return orderNo;} public void setOrderNo(String v){this.orderNo=v;}
    public String getProductName(){return productName;} public void setProductName(String v){this.productName=v;}
    public float getPrice(){return price;} public void setPrice(float v){this.price=v;}
    public int getQuantity(){return quantity;} public void setQuantity(int v){this.quantity=v;}
}
"@

  $mapperContent = @"
package $basePkg.data.mapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import $basePkg.data.entity.OrderEntity;
public class OrderRowMapper implements RowMapper<OrderEntity> {
    @Override
    public OrderEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new OrderEntity(
            rs.getLong("ID"),
            rs.getString("ORDER_NO"),
            rs.getString("PRODUCT_NAME"),
            rs.getFloat("PRICE"),
            rs.getInt("QUANTITY")
        );
    }
}
"@

  $repoContent = @"
package $basePkg.data.repository;
import org.springframework.data.repository.CrudRepository;
import $basePkg.data.entity.OrderEntity;
public interface OrdersRepository extends CrudRepository<OrderEntity, Long> { }
"@

  $serviceContent = @"
package $basePkg.data;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import $basePkg.data.entity.OrderEntity;
import $basePkg.data.repository.OrdersRepository;
@Service
public class OrdersDataService implements DataAccessInterface<OrderEntity> {
    @Autowired private OrdersRepository ordersRepository;
    public OrdersDataService(OrdersRepository ordersRepository){ this.ordersRepository = ordersRepository; }
    @Override public OrderEntity findById(int id){ return null; }
    @Override public List<OrderEntity> findAll(){
        List<OrderEntity> orders = new ArrayList<>();
        try { Iterable<OrderEntity> it = ordersRepository.findAll(); it.forEach(orders::add); }
        catch(Exception e){ e.printStackTrace(); }
        return orders;
    }
    @Override public boolean create(OrderEntity order){
        try { ordersRepository.save(order); return true; } catch(Exception e){ e.printStackTrace(); return true; }
    }
    @Override public boolean update(OrderEntity order){ return true; }
    @Override public boolean delete(OrderEntity order){ return true; }
}
"@

  New-JavaFile -path $entity -content $entityContent
  New-JavaFile -path $mapper -content $mapperContent
  New-JavaFile -path $repo   -content $repoContent
  New-JavaFile -path $service -content $serviceContent
}

function Scaff-Part3-Overrides { param($projPath,$basePkg)
  $root = (Join-Path $projPath ("src/main/java/" + ($basePkg -replace '\.','/')))
  $repo = Join-Path $root "data/repository/OrdersRepository.java"
  $repoContent = @"
package $basePkg.data.repository;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import $basePkg.data.entity.OrderEntity;
public interface OrdersRepository extends CrudRepository<OrderEntity, Long> {
    @Query("SELECT * FROM ORDERS")
    List<OrderEntity> findAll();
}
"@
  New-JavaFile -path $repo -content $repoContent

  $service = Join-Path $root "data/OrdersDataService.java"
  $serviceContent = @"
package $basePkg.data;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import $basePkg.data.entity.OrderEntity;
import $basePkg.data.repository.OrdersRepository;
@Service
public class OrdersDataService implements DataAccessInterface<OrderEntity> {
    @Autowired private OrdersRepository ordersRepository;
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplateObject;
    public OrdersDataService(OrdersRepository ordersRepository, DataSource dataSource){
        this.ordersRepository = ordersRepository;
        this.dataSource = dataSource;
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    @Override public OrderEntity findById(int id){ return null; }
    @Override public List<OrderEntity> findAll(){
        List<OrderEntity> orders = new ArrayList<>();
        try { ordersRepository.findAll().forEach(orders::add); } catch(Exception e){ e.printStackTrace(); }
        return orders;
    }
    @Override public boolean create(OrderEntity order){
        try {
            String sql = "INSERT INTO ORDERS(ORDER_NO, PRODUCT_NAME, PRICE, QUANTITY) VALUES(?, ?, ?, ?)";
            int rows = jdbcTemplateObject.update(sql, order.getOrderNo(), order.getProductName(), order.getPrice(), order.getQuantity());
            return rows == 1;
        } catch(Exception e){ e.printStackTrace(); return false; }
    }
    @Override public boolean update(OrderEntity order){ return true; }
    @Override public boolean delete(OrderEntity order){ return true; }
}
"@
  New-JavaFile -path $service -content $serviceContent
}

$root = Get-Location
$src = Join-Path $root $SourceProject
if (-not (Test-Path $src)) { throw "Source project '$SourceProject' not found in $(Get-Location)" }

$parts = @(
  @{ Name = "topic4-1"; App = "Topic42Application"; Deps = @("jdbc") },
  @{ Name = "topic4-2"; App = "Topic42Application"; Deps = @("jdbc","datajdbc") },
  @{ Name = "topic4-3"; App = "Topic43Application"; Deps = @("jdbc","datajdbc") }
)

foreach($p in $parts){
  $dst = Join-Path $root $p.Name
  Copy-Project -fromPath $src -toPath $dst
  Rename-AppClass -projPath $dst -newClass $p.App
  Update-Pom -projPath $dst -artifact $p.Name -depsToAdd $p.Deps
  Write-AppProps -projPath $dst -dbHost $DbHost -dbPort $DbPort -dbName $DbName -dbUser $DbUser -dbPass $DbPass
  $base = Get-BasePackage -projPath $dst
  Scaff-DAO-Interface -projPath $dst -basePkg $base
  switch ($p.Name) {
    "topic4-1" { Scaff-Part1-OrdersDataService -projPath $dst -basePkg $base }
    "topic4-2" { Scaff-Part2-EntitiesAndRepo -projPath $dst -basePkg $base }
    "topic4-3" { Scaff-Part2-EntitiesAndRepo -projPath $dst -basePkg $base; Scaff-Part3-Overrides -projPath $dst -basePkg $base }
  }
  Write-Host ">> Scaffolded $($p.Name) under base package '$base'"
}

Write-Host "=== DONE ==="
Write-Host "Next steps:"
Write-Host "1) Import the 'CST-339-RS-Activity 4 MySQL' DDL into MySQL (Workbench -> Admin -> Data Import/Restore)."
Write-Host "2) Start MySQL and verify database '$DbName' plus ORDERS table exists."
Write-Host "3) For each project (topic4-1, topic4-2, topic4-3): cd .\<proj>; .\mvnw spring-boot:run"
Write-Host "4) Log in at http://localhost:8080/login/ and screenshot the Orders page."

