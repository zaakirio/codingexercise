package com.example.codingexercise.controller;

import com.example.codingexercise.model.PackageDTO;
import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/packages")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductPackage create(@Valid @RequestBody ProductPackage newProductPackage) {
        return packageService.createPackage(newProductPackage);
    }

    @GetMapping("/{id}")
    public PackageDTO get(@PathVariable String id, @RequestParam(required = false) String currency) {
        return packageService.getPackage(id, currency);
    }

    @PutMapping("/{id}")
    public ProductPackage update(@PathVariable String id, @Valid @RequestBody ProductPackage updatedPackage) {
        return packageService.updatePackage(id, updatedPackage);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        packageService.deletePackage(id);
    }

    @GetMapping
    public List<PackageDTO> getAll(@RequestParam(required = false) String currency) {
        return packageService.getAllPackages(currency);
    }
}
