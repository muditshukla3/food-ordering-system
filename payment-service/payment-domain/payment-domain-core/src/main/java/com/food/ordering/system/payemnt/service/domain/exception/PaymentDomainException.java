package com.food.ordering.system.payemnt.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class PaymentDomainException extends DomainException {

    public PaymentDomainException() {
    }

    public PaymentDomainException(String message) {
        super(message);
    }
}
