package com.food.ordering.system.restaurant.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class RestaurantApplicationServiceException extends DomainException {

    public RestaurantApplicationServiceException() {
    }

    public RestaurantApplicationServiceException(String message) {
        super(message);
    }
}
