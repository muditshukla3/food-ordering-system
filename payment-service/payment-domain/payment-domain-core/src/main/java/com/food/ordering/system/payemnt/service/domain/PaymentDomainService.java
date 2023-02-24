package com.food.ordering.system.payemnt.service.domain;

import com.food.ordering.system.payemnt.service.domain.entity.CreditEntry;
import com.food.ordering.system.payemnt.service.domain.entity.CreditHistory;
import com.food.ordering.system.payemnt.service.domain.entity.Payment;
import com.food.ordering.system.payemnt.service.domain.event.PaymentEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages);
}
