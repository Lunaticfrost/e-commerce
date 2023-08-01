package com.ecommerce.backend.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderResponseDTO {
    private Long id;
    private CustomerDTO customer;
    private LocalDate orderDate;
    private List<OrderItemResponseDTO> orderItems;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public CustomerDTO getCustomer() {
		return customer;
	}
	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}
	public LocalDate getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(LocalDate orderDate) {
		this.orderDate = orderDate;
	}
	public List<OrderItemResponseDTO> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItemResponseDTO> orderItems) {
		this.orderItems = orderItems;
	}
    


    
    
}
