package com.ecommerce.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.ProductRepository;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductRepository productRepository;

	// Constructor injection of ProductRepository to ensure dependency injection.
	public ProductController(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	// Get all products.
	@GetMapping
	public List<Product> getAllProducts() {
		return productRepository.findAll();
	}

	// Get a product by its ID.
	@GetMapping("/{id}")
	public ResponseEntity<Product> getProductById(@PathVariable Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		return productOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Search products by name (case-insensitive).
	// Example usage: /products/search?query=productName
	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProductsByName(@RequestParam String query) {
		List<Product> products = productRepository.findByNameContainingIgnoreCase(query);
		return ResponseEntity.ok(products);
	}

	// Create a new product.
	@PostMapping
	public Product createProduct(@RequestBody Product product) {
		return productRepository.save(product);
	}

	// Update an existing product by its ID.
	@PutMapping("/{id}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
		Optional<Product> productOptional = productRepository.findById(id);
		if (productOptional.isPresent()) {
			Product existingProduct = productOptional.get();
			existingProduct.setName(updatedProduct.getName());
			existingProduct.setPrice(updatedProduct.getPrice());
			existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
			return ResponseEntity.ok(productRepository.save(existingProduct));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Delete a product by its ID.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
		Optional<Product> productOptional = productRepository.findById(id);
		if (productOptional.isPresent()) {
			productRepository.deleteById(id);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
