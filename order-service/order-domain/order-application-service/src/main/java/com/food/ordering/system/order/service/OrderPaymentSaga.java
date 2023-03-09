package com.food.ordering.system.order.service;

import com.food.ordering.system.domain.event.EmptyEvent;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.domain.OrderDomainService;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.event.OrderPaidEvent;
import com.food.ordering.system.order.domain.exception.OrderNotFoundException;
import com.food.ordering.system.order.service.dto.message.PaymentResponse;
import com.food.ordering.system.order.service.ports.output.message.publisher.restaurant.OrderPaidResturantRequestMessagePublisher;
import com.food.ordering.system.order.service.ports.output.repository.OrderRepository;
import com.food.ordering.system.saga.SagaStep;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderPaymentSaga implements SagaStep<PaymentResponse, OrderPaidEvent, EmptyEvent> {

    private final OrderDomainService orderDomainService;
    private final OrderPaidResturantRequestMessagePublisher orderPaidResturantRequestMessagePublisher;

    private final OrderSagaHelper orderSagaHelper;

    public OrderPaymentSaga(OrderDomainService orderDomainService,
                            OrderPaidResturantRequestMessagePublisher orderPaidResturantRequestMessagePublisher,
                            OrderSagaHelper orderSagaHelper) {
        this.orderDomainService = orderDomainService;
        this.orderPaidResturantRequestMessagePublisher = orderPaidResturantRequestMessagePublisher;
        this.orderSagaHelper = orderSagaHelper;
    }

    @Override
    @Transactional
    public OrderPaidEvent process(PaymentResponse paymentResponse) {
        log.info("Completing payment for order with id {}",paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        OrderPaidEvent orderPaidEvent = orderDomainService.payOrder(order,
                orderPaidResturantRequestMessagePublisher);
        orderSagaHelper.saveOrder(order);
        log.info("Order with order id: {} is paid", order.getId().getValue());
        return orderPaidEvent;
    }

    @Override
    @Transactional
    public EmptyEvent rollback(PaymentResponse paymentResponse) {
        log.info("Cancelling order with order id: {}", paymentResponse.getOrderId());
        Order order = orderSagaHelper.findOrder(paymentResponse.getOrderId());
        orderDomainService.cancelOrder(order, paymentResponse.getFailureMessages());
        orderSagaHelper.saveOrder(order);
        log.info("Order with id: {} is cancelled", paymentResponse.getOrderId());
        return EmptyEvent.INSTANCE;
    }
}
