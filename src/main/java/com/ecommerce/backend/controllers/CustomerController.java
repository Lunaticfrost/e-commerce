package com.ecommerce.backend.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.entity.Customer;
import com.ecommerce.backend.repository.CustomerRepository;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerRepository customerRepository;

	// Constructor injection of CustomerRepository to ensure dependency injection.
	public CustomerController(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	// Get all customers.
	@GetMapping
	public List<Customer> getAllCustomers() {
		return customerRepository.findAll();
	}

	// Get a customer by their ID.
	@GetMapping("/{id}")
	public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		return customerOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	// Create a new customer.
	@PostMapping
	public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
		// Check if a customer with the given email already exists.
		Customer existingCustomerByEmail = customerRepository.findByEmail(customer.getEmail());
		if (existingCustomerByEmail != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Customer with the given email already exists.");
		}

		// Check if a customer with the given phone number already exists.
		Customer existingCustomerByPhoneNumber = customerRepository.findByPhoneNumber(customer.getPhoneNumber());
		if (existingCustomerByPhoneNumber != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Customer with the given phone number already exists.");
		}

		try {
			Customer savedCustomer = customerRepository.save(customer);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedCustomer);
		} catch (Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while saving customer.");
		}
	}

	// Update an existing customer by their ID.
	@PutMapping("/{id}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			Customer existingCustomer = customerOptional.get();
			existingCustomer.setName(updatedCustomer.getName());
			existingCustomer.setEmail(updatedCustomer.getEmail());
			existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
			return ResponseEntity.ok(customerRepository.save(existingCustomer));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Delete a customer by their ID.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
		Optional<Customer> customerOptional = customerRepository.findById(id);
		if (customerOptional.isPresent()) {
			customerRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
