package com.example.codingexercise.service;

import com.example.codingexercise.gateway.CurrencyServiceGateway;
import com.example.codingexercise.gateway.ProductServiceGateway;
import com.example.codingexercise.gateway.dto.Product;
import com.example.codingexercise.model.PackageDTO;
import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PackageService {

    private final PackageRepository packageRepository;
    private final ProductServiceGateway productServiceGateway;
    private final CurrencyServiceGateway currencyServiceGateway;

    public PackageService(PackageRepository packageRepository,
                          ProductServiceGateway productServiceGateway,
                          CurrencyServiceGateway currencyServiceGateway) {
        this.packageRepository = packageRepository;
        this.productServiceGateway = productServiceGateway;
        this.currencyServiceGateway = currencyServiceGateway;
    }

    public ProductPackage createPackage(ProductPackage productPackage) {
        // Validate products exist (optional, but good practice)
        // For now, just save
        if (productPackage.getId() == null) {
            productPackage.setId(UUID.randomUUID().toString());
        }
        return packageRepository.save(productPackage);
    }

    public PackageDTO getPackage(String id, String currency) {
        ProductPackage productPackage = packageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        List<Product> products = productServiceGateway.getProducts(productPackage.getProductIds());
        
        double totalUsdPrice = products.stream()
                .mapToDouble(Product::usdPrice)
                .sum();

        String targetCurrency = currency != null ? currency : "USD";
        double exchangeRate = currencyServiceGateway.getExchangeRate("USD", targetCurrency);
        double totalPrice = totalUsdPrice * exchangeRate;

        return PackageDTO.builder()
                .id(productPackage.getId())
                .name(productPackage.getName())
                .description(productPackage.getDescription())
                .products(products)
                .price(totalPrice)
                .currency(targetCurrency)
                .build();
    }

    public ProductPackage updatePackage(String id, ProductPackage updatedPackage) {
        return packageRepository.findById(id)
                .map(existing -> {
                    existing.setName(updatedPackage.getName());
                    existing.setDescription(updatedPackage.getDescription());
                    existing.setProductIds(updatedPackage.getProductIds());
                    return packageRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    public void deletePackage(String id) {
        packageRepository.deleteById(id);
    }

    public List<PackageDTO> getAllPackages(String currency) {
        return packageRepository.findAll().stream()
                .map(p -> getPackage(p.getId(), currency))
                .collect(Collectors.toList());
    }
}
