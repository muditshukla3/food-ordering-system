package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RestaurantNotFoundException extends DomainException {
    public RestaurantNotFoundException() {
    }

    public RestaurantNotFoundException(String message) {
        super(message);
    }
}
