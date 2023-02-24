package com.food.ordering.system.payemnt.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.payemnt.service.domain.entity.CreditEntry;
import com.food.ordering.system.payemnt.service.domain.entity.CreditHistory;
import com.food.ordering.system.payemnt.service.domain.entity.Payment;
import com.food.ordering.system.payemnt.service.domain.event.PaymentCancelledEvent;
import com.food.ordering.system.payemnt.service.domain.event.PaymentCompletedEvent;
import com.food.ordering.system.payemnt.service.domain.event.PaymentEvent;
import com.food.ordering.system.payemnt.service.domain.event.PaymentFailedEvent;

import java.util.List;

public interface PaymentDomainService {

    PaymentEvent validateAndInitiatePayment(Payment payment,
                                            CreditEntry creditEntry,
                                            List<CreditHistory> creditHistories,
                                            List<String> failureMessages,
                                            DomainEventPublisher<PaymentCompletedEvent> paymentCompletedEventDomainEventPublisher, DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);

    PaymentEvent validateAndCancelPayment(Payment payment,
                                          CreditEntry creditEntry,
                                          List<CreditHistory> creditHistories,
                                          List<String> failureMessages, DomainEventPublisher<PaymentCancelledEvent> paymentCancelledEventDomainEventPublisher, DomainEventPublisher<PaymentFailedEvent> paymentFailedEventDomainEventPublisher);
}
