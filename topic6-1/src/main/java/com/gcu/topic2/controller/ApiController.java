package com.gcu.topic2.controller;

import com.gcu.topic2.model.Product;
import com.gcu.topic2.model.ProductList;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ApiController {

    private List<Product> sample() {
        return Arrays.asList(
                new Product(1, "Laptop", 999.99),
                new Product(2, "Headphones", 59.99),
                new Product(3, "Mouse", 24.99)
        );
    }

    @GetMapping(value = "/getjson", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> getJson() {
        return sample();
    }

    @GetMapping(value = "/getxml", produces = MediaType.APPLICATION_XML_VALUE)
    public ProductList getXml() {
        return new ProductList(sample());
    }
}