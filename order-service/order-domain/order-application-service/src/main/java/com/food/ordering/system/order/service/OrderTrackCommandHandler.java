package com.food.ordering.system.order.service;

import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.domain.valueobject.TrackingId;
import com.food.ordering.system.order.service.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class OrderTrackCommandHandler {

    private final OrderDataMapper orderDataMapper;

    private final OrderRepository orderRepository;

    public OrderTrackCommandHandler(OrderDataMapper orderDataMapper, OrderRepository orderRepository) {
        this.orderDataMapper = orderDataMapper;
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery){
        Optional<Order> orderResult = orderRepository.findByTrackingId(new TrackingId(trackOrderQuery.getOrderTrackingId()));
        if(orderResult.isEmpty()){
            log.warn("Could not find order with tracking id: {}", trackOrderQuery.getOrderTrackingId());
            throw new OrderNotFoundException("Could not find order with tracking id: "+ trackOrderQuery.getOrderTrackingId());
        }
        return orderDataMapper.orderToTrackOrderResponse(orderResult.get());
    }
}
