package com.food.ordering.system.payment.service.domain.exception;

import com.food.ordering.system.domain.exception.DomainException;

public class PaymentApplicationServiceException  extends DomainException {

    public PaymentApplicationServiceException() {
    }

    public PaymentApplicationServiceException(String message) {
        super(message);
    }
}
