package com.food.ordering.system.payment.service.domain;

import com.food.ordering.system.domain.valueobject.PaymentOrderStatus;
import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.payment.service.dataaccess.outbox.entity.OrderOutboxEntity;
import com.food.ordering.system.payment.service.dataaccess.outbox.repository.OrderOutboxJpaRepository;
import com.food.ordering.system.payment.service.domain.dto.PaymentRequest;
import com.food.ordering.system.payment.service.domain.outbox.model.OrderOutboxMessage;
import com.food.ordering.system.payment.service.domain.ports.input.message.listener.PaymentRequestMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = PaymentServiceApplication.class)
public class PaymentRequestMessageListenerTest {

    @Autowired
    private PaymentRequestMessageListener paymentRequestMessageListener;
    @Autowired
    private OrderOutboxJpaRepository orderOutboxJpaRepository;

    private final static String CUSTOMER_ID = "f003e21b-9dc1-41ef-89bb-0895a6043c2a";
    private final static BigDecimal PRICE = new BigDecimal("100");

    @Test
    void testDoublePayment(){
        String sagaId = UUID.randomUUID().toString();
        paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        try {
            paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
        }catch (DataAccessException e){
            log.error("DataAccessException occurred with sql state: {}",
                    ((PSQLException)Objects.requireNonNull(e.getRootCause())).getSQLState());
        }

        assertOrderOutbox(sagaId);
    }

    @Test
    void testDoublePaymentWithThread() {
        String sagaId = UUID.randomUUID().toString();
        ExecutorService executorService = null;

        try {
            executorService = Executors.newFixedThreadPool(2);

            List<Callable<Object>> tasks = new ArrayList<>();

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 1 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            tasks.add(Executors.callable(() -> {
                try {
                    paymentRequestMessageListener.completePayment(getPaymentRequest(sagaId));
                } catch (DataAccessException e) {
                    log.error("DataAccessException occurred for thread 2 with sql state: {}",
                            ((PSQLException) Objects.requireNonNull(e.getRootCause())).getSQLState());
                }
            }));

            executorService.invokeAll(tasks);
        }catch (InterruptedException e){
            log.error("Error calling complete payment!");
        }finally {
            if(executorService!=null){
                executorService.shutdown();
            }
        }
        assertOrderOutbox(sagaId);
    }

    private void assertOrderOutbox(String sagaId) {
        Optional<OrderOutboxEntity> orderOutboxEntity = orderOutboxJpaRepository.
                findByTypeAndSagaIdAndPaymentStatusAndOutboxStatus(
                ORDER_SAGA_NAME,UUID.fromString(sagaId), PaymentStatus.COMPLETED, OutboxStatus.STARTED);

        assertTrue(orderOutboxEntity.isPresent());
        assertEquals(orderOutboxEntity.get().getSagaId().toString(), sagaId);
    }

    private PaymentRequest getPaymentRequest(String sagaId){
        return PaymentRequest.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(sagaId)
                .orderId(UUID.randomUUID().toString())
                .paymentOrderStatus(PaymentOrderStatus.PENDING)
                .customerId(CUSTOMER_ID)
                .price(PRICE)
                .createdAt(Instant.now())
                .build();
    }
}