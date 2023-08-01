package com.ecommerce.backend.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.backend.dto.CustomerDTO;
import com.ecommerce.backend.dto.OrderItemResponseDTO;
import com.ecommerce.backend.dto.OrderResponseDTO;
import com.ecommerce.backend.dto.ProductDTO;
import com.ecommerce.backend.entity.Customer;
import com.ecommerce.backend.entity.Order;
import com.ecommerce.backend.entity.OrderItem;
import com.ecommerce.backend.entity.Product;
import com.ecommerce.backend.repository.CustomerRepository;
import com.ecommerce.backend.repository.OrderRepository;
import com.ecommerce.backend.repository.ProductRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderRepository orderRepository;
	private final CustomerRepository customerRepository;
	private final ProductRepository productRepository;

	// Constructor injection of OrderRepository, CustomerRepository, and
	// ProductRepository to ensure dependency injection.
	public OrderController(OrderRepository orderRepository, CustomerRepository customerRepository,
			ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.customerRepository = customerRepository;
		this.productRepository = productRepository;
	}

	// Create a new order.
	@PostMapping
	public ResponseEntity<?> createOrder(@RequestBody Order order) {
		// Check if the customer exists with the given customer ID.
		Customer customer = customerRepository.findById(order.getCustomer().getId()).orElse(null);

		if (customer == null) {
			return ResponseEntity.badRequest().body("Customer not found with id: " + order.getCustomer().getId());
		}

		// Check and set products for each order item by their IDs.
		for (OrderItem orderItem : order.getOrderItems()) {
			Product product = productRepository.findById(orderItem.getProduct().getId()).orElse(null);
			if (product == null) {
				return ResponseEntity.badRequest().body("Product not found with id: " + orderItem.getProduct().getId());
			}
			orderItem.setProduct(product);
		}

		order.setCustomer(customer);
		order.setOrderDate(LocalDate.now());
		order = orderRepository.save(order);
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}

	// Get all orders with details.
	@GetMapping
	public List<OrderResponseDTO> getAllOrders() {
		List<Order> orders = orderRepository.findAll();
		List<OrderResponseDTO> orderResponseDTOs = orders.stream().map(this::convertToDTO).collect(Collectors.toList());
		return orderResponseDTOs;
	}

	// Get orders for a specific customer by their customer ID.
	@GetMapping("/{customerId}/customer")
	public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
		Customer customer = customerRepository.findById(customerId).orElse(null);
		if (customer == null) {
			return ResponseEntity.notFound().build();
		}

		List<Order> orders = orderRepository.findByCustomer(customer);
		List<OrderResponseDTO> orderResponseDTOs = orders.stream().map(this::convertToDTO).collect(Collectors.toList());

		return ResponseEntity.ok(orderResponseDTOs);
	}

	// Delete an order by its ID.
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
		if (orderRepository.existsById(id)) {
			orderRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Convert an Order entity to an OrderResponseDTO.
	private OrderResponseDTO convertToDTO(Order order) {
		OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
		orderResponseDTO.setId(order.getId());
		orderResponseDTO.setOrderDate(order.getOrderDate());

		CustomerDTO customerDTO = new CustomerDTO();
		Customer customer = order.getCustomer();
		customerDTO.setId(customer.getId());
		customerDTO.setName(customer.getName());
		customerDTO.setEmail(customer.getEmail());
		customerDTO.setPhoneNumber(customer.getPhoneNumber());
		orderResponseDTO.setCustomer(customerDTO);

		List<OrderItemResponseDTO> orderItemResponseDTOs = order.getOrderItems().stream().map(orderItem -> {
			OrderItemResponseDTO itemResponseDTO = new OrderItemResponseDTO();
			itemResponseDTO.setProduct(convertToDTO(orderItem.getProduct()));
			itemResponseDTO.setQuantity(orderItem.getQuantity());
			return itemResponseDTO;
		}).collect(Collectors.toList());
		orderResponseDTO.setOrderItems(orderItemResponseDTOs);

		return orderResponseDTO;
	}

	// Convert a Product entity to a ProductDTO.
	private ProductDTO convertToDTO(Product product) {
		ProductDTO productDTO = new ProductDTO();
		productDTO.setId(product.getId());
		productDTO.setName(product.getName());
		productDTO.setPrice(product.getPrice());
		return productDTO;
	}
}
