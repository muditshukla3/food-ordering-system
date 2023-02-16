package com.food.ordering.system.order.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class OrderNotFoundException extends DomainException {

    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String message) {
        super(message);
    }
}
