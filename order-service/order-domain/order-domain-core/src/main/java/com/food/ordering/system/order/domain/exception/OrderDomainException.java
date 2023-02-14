package com.food.ordering.system.order.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class OrderDomainException extends DomainException {
    public OrderDomainException() {
    }

    public OrderDomainException(String message) {
        super(message);
    }
}
