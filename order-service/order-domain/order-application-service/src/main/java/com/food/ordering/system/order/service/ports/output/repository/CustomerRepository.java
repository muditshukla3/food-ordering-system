package com.food.ordering.system.order.service.ports.output.repository;

import com.food.ordering.system.order.domain.entity.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Optional<Customer> findCustomer(UUID customerId);

    Customer save(Customer customer);
}
