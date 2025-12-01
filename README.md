# Package Management Service

A RESTful web service for managing packages of products, built with Java and Spring Boot.

## Features
- **Create, Retrieve, Update, Delete (CRUD)** packages.
- **Currency Conversion**: Calculate total package price in different currencies (default: USD).
- **External Integration**: Fetches product details from an external Product Service and exchange rates from `frankfurter.app`.
- **Validation**: Ensures packages have valid names and at least one product.

## Prerequisites
- Java 17 or higher
- Maven (wrapper included)

## Getting Started

### Build the project
```bash
./mvnw clean install
```

### Run the application
```bash
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`.

## API Usage

### Create a Package
```bash
curl -X POST -H "Content-Type: application/json" -d '{"name":"Hero Set","description":"Starter kit","productIds":["VqKb4tyj9V6i","DXSQpv6XVeJm"]}' http://localhost:8080/packages
```

### Get a Package
**Default (USD):**
```bash
curl http://localhost:8080/packages/{id}
```

**Specific Currency (e.g., EUR):**
```bash
curl "http://localhost:8080/packages/{id}?currency=EUR"
```

### List All Packages
```bash
curl http://localhost:8080/packages
```

### Update a Package
```bash
curl -X PUT -H "Content-Type: application/json" -d '{"name":"Updated Set","description":"Updated","productIds":["7dgX6XzU3Wds"]}' http://localhost:8080/packages/{id}
```

### Delete a Package
```bash
curl -X DELETE http://localhost:8080/packages/{id}
```

## Architecture
- **Controller**: Handles HTTP requests.
- **Service**: Business logic, price calculation, currency conversion.
- **Repository**: Data access (H2 in-memory database).
- **Gateway**: Integration with external APIs (Product Service, Frankfurter API).
