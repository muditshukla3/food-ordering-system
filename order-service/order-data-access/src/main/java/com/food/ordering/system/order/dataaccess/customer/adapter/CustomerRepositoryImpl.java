package com.food.ordering.system.order.dataaccess.customer.adapter;

import com.food.ordering.system.order.dataaccess.customer.mapper.CustomerDataAccessMapper;
import com.food.ordering.system.order.dataaccess.customer.repository.CustomerJPARepository;
import com.food.ordering.system.order.domain.entity.Customer;
import com.food.ordering.system.order.service.ports.output.repository.CustomerRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJPARepository customerJPARepository;

    private final CustomerDataAccessMapper customerDataAccessMapper;

    public CustomerRepositoryImpl(CustomerJPARepository customerJPARepository,
                                  CustomerDataAccessMapper customerDataAccessMapper) {
        this.customerJPARepository = customerJPARepository;
        this.customerDataAccessMapper = customerDataAccessMapper;
    }

    @Override
    public Optional<Customer> findCustomer(UUID customerId) {
        return customerJPARepository.findById(customerId).map(customerDataAccessMapper::customerEntityToCustomer);
    }

    @Override
    @Transactional
    public Customer save(Customer customer) {
        return customerDataAccessMapper.customerEntityToCustomer(
                customerJPARepository.save(
                        customerDataAccessMapper.customerToCustomerEntity(customer)));
    }
}
