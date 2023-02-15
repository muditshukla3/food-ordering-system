package com.food.ordering.system.order.service.ports.output.message.publisher.restaurant;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.domain.event.OrderPaidEvent;

public interface OrderPaidResturantRequestMessagePublisher extends DomainEventPublisher<OrderPaidEvent> {
}
