package com.food.ordering.system.payemnt.service.domain.event;

import com.food.ordering.system.payemnt.service.domain.entity.Payment;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class PaymentCancelledEvent extends PaymentEvent{
    public PaymentCancelledEvent(Payment payment, ZonedDateTime createdAt) {
        super(payment, createdAt, Collections.emptyList());
    }
}
