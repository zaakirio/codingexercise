package com.example.codingexercise.model;

import com.example.codingexercise.gateway.dto.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PackageDTO {
    private String id;
    private String name;
    private String description;
    private List<Product> products;
    private double price;
    private String currency;
}
