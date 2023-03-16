package com.food.ordering.system.order.service;

import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.domain.valueobject.OrderStatus;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderSagaHelper {

    private final OrderRepository orderRepository;

    public OrderSagaHelper(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    Order findOrder(String orderId) {
        Optional<Order> orderResponse = orderRepository.findById(new OrderId(UUID.fromString(orderId)));
        if(orderResponse.isEmpty()){
            log.error("Order with id: {} could not be found", orderId);
            throw new OrderNotFoundException("Order with id "+orderId+ " could not be found");
        }
        return orderResponse.get();
    }

    void saveOrder(Order order){
        orderRepository.save(order);
    }

    SagaStatus orderStatusToSagaStatus(OrderStatus orderStatus){
        switch (orderStatus){
            case PAID:
                return SagaStatus.PROCESSING;
            case APPROVED:
                return SagaStatus.SUCCEEDED;
            case CANCELLING:
                return SagaStatus.COMPENSATING;
            case CANCELLED:
                return SagaStatus.COMPENSATED;
            default:
                return SagaStatus.STARTED;
        }
    }
}
