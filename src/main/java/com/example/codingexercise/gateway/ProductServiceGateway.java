package com.example.codingexercise.gateway;

import com.example.codingexercise.gateway.dto.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductServiceGateway {

    private final RestTemplate restTemplate;

    public ProductServiceGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Product getProduct(String id) {
        return restTemplate.getForObject("https://product-service.herokuapp.com/api/v1/products/{id}", Product.class, id);
    }

    public List<Product> getProducts(List<String> ids) {
        List<Product> products = new ArrayList<>();
        for (String id : ids) {
            products.add(getProduct(id));
        }
        return products;
    }
}
