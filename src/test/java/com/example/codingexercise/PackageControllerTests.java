package com.example.codingexercise;

import com.example.codingexercise.gateway.CurrencyServiceGateway;
import com.example.codingexercise.gateway.ProductServiceGateway;
import com.example.codingexercise.gateway.dto.Product;
import com.example.codingexercise.model.PackageDTO;
import com.example.codingexercise.model.ProductPackage;
import com.example.codingexercise.repository.PackageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PackageControllerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PackageRepository packageRepository;

    @MockBean
    private ProductServiceGateway productServiceGateway;

    @MockBean
    private CurrencyServiceGateway currencyServiceGateway;

    @Test
    void createPackage() {
        ProductPackage newPackage = new ProductPackage(null, "Test Name", "Test Desc", List.of("prod1"));
        ResponseEntity<ProductPackage> created = restTemplate.postForEntity("/packages", newPackage, ProductPackage.class);
        
        assertEquals(HttpStatus.CREATED, created.getStatusCode(), "Unexpected status code");
        ProductPackage createdBody = created.getBody();
        assertNotNull(createdBody, "Unexpected body");
        assertNotNull(createdBody.getId());
        assertEquals("Test Name", createdBody.getName());
        assertEquals("Test Desc", createdBody.getDescription());
        assertEquals(List.of("prod1"), createdBody.getProductIds());

        ProductPackage saved = packageRepository.findById(createdBody.getId()).orElse(null);
        assertNotNull(saved);
        assertEquals("Test Name", saved.getName());
    }

    @Test
    void getPackage() {
        String id = UUID.randomUUID().toString();
        ProductPackage productPackage = new ProductPackage(id, "Test Name 2", "Test Desc 2", List.of("prod2"));
        packageRepository.save(productPackage);

        when(productServiceGateway.getProducts(anyList())).thenReturn(List.of(new Product("prod2", "Product 2", 100)));
        when(currencyServiceGateway.getExchangeRate(anyString(), anyString())).thenReturn(1.0);

        ResponseEntity<PackageDTO> fetched = restTemplate.getForEntity("/packages/{id}", PackageDTO.class, id);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
        PackageDTO fetchedBody = fetched.getBody();
        assertNotNull(fetchedBody);
        assertEquals(id, fetchedBody.getId());
        assertEquals("Test Name 2", fetchedBody.getName());
        assertEquals(100.0, fetchedBody.getPrice());
    }

    @Test
    void listPackages() {
        packageRepository.deleteAll();
        packageRepository.save(new ProductPackage(UUID.randomUUID().toString(), "P1", "D1", List.of("p1")));
        packageRepository.save(new ProductPackage(UUID.randomUUID().toString(), "P2", "D2", List.of("p2")));

        when(productServiceGateway.getProducts(anyList())).thenReturn(List.of(new Product("p", "Product", 50)));
        when(currencyServiceGateway.getExchangeRate(anyString(), anyString())).thenReturn(1.0);

        ResponseEntity<Object> fetched = restTemplate.getForEntity("/packages", Object.class);
        assertEquals(HttpStatus.OK, fetched.getStatusCode());
    }
}
