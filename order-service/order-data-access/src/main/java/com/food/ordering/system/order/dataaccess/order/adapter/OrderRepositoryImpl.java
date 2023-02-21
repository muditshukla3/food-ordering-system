package com.food.ordering.system.order.dataaccess.order.adapter;

import com.food.ordering.system.order.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.dataaccess.order.repository.OrderJPARepository;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.valueobject.TrackingId;
import com.food.ordering.system.order.service.ports.output.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJPARepository orderJPARepository;

    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJPARepository orderJPARepository, OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJPARepository = orderJPARepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJPARepository.
                save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJPARepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }
}
