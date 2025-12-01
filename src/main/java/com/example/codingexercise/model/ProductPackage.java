package com.example.codingexercise.model;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPackage {
    @Id
    private String id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @ElementCollection
    @jakarta.validation.constraints.NotEmpty(message = "Package must contain at least one product")
    private List<String> productIds;
}
