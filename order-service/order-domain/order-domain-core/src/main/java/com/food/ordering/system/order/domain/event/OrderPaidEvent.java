package com.food.ordering.system.order.domain.event;

import com.food.ordering.system.domain.event.DomainEvent;
import com.food.ordering.system.order.domain.entity.Order;

import java.time.ZonedDateTime;

public class OrderPaidEvent extends OrderEvent {

    public OrderPaidEvent(Order order, ZonedDateTime createdAt) {
        super(order, createdAt);
    }
}
