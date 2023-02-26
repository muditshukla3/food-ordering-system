package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RestaurantDomainException extends DomainException {

    public RestaurantDomainException() {
    }

    public RestaurantDomainException(String message) {
        super(message);
    }
}
