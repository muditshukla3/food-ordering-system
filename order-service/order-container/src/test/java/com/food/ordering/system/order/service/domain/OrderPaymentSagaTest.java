package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.PaymentStatus;
import com.food.ordering.system.order.dataaccess.outbox.payment.entity.PaymentOutboxEntity;
import com.food.ordering.system.order.dataaccess.outbox.payment.repository.PaymentOutboxJpaRepository;
import com.food.ordering.system.order.service.OrderPaymentSaga;
import com.food.ordering.system.order.service.dto.message.PaymentResponse;
import com.food.ordering.system.saga.SagaStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static com.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@SpringBootTest(classes = OrderServiceApplication.class)
@Sql(value = {"classpath:sql/OrderPaymentSagaTestSetUp.sql"})
@Sql(value = {"classpath:sql/OrderPaymentSagaTestCleanUp.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class OrderPaymentSagaTest {

    @Autowired
    private OrderPaymentSaga orderPaymentSaga;

    @Autowired
    private PaymentOutboxJpaRepository paymentOutboxJpaRepository;

    private final UUID SAGA_ID = UUID.fromString("881da7d1-d225-4c86-b168-23512c28fbcc");
    private final UUID ORDER_ID = UUID.fromString("337489df-5f26-48f4-869d-0c6641f8aad3");
    private final UUID CUSTOMER_ID = UUID.fromString("a6c2cd40-f1ef-46f9-a5f0-3e2ea4039ec5");
    private final UUID PAYMENT_ID = UUID.randomUUID();
    private final BigDecimal PRICE = new BigDecimal("100");

    @Test
    void testDoublePayment(){
        orderPaymentSaga.process(getPaymentResponse());
        orderPaymentSaga.process(getPaymentResponse());
    }

    @Test
    void testDoublePaymentWithThread() throws InterruptedException {
        Thread thread1 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
        Thread thread2 = new Thread(() -> orderPaymentSaga.process(getPaymentResponse()));
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        assertPaymentOutbox();
    }

    @Test
    void testDoublePaymentWithLatch() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        Thread thread1 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            }catch (OptimisticLockingFailureException e){
                log.error("OptimisticLockingFailureException occurred in thread1");
            }finally {
                latch.countDown();
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                orderPaymentSaga.process(getPaymentResponse());
            }catch (OptimisticLockingFailureException e){
                log.error("OptimisticLockingFailureException occurred in thread2");
            }finally {
                latch.countDown();
            }
        });

        thread1.start();
        thread2.start();

        latch.await();

        assertPaymentOutbox();
    }

    private void assertPaymentOutbox() {
        Optional<PaymentOutboxEntity> paymentOutboxEntity =
                paymentOutboxJpaRepository.findByTypeAndSagaIdAndSagaStatusIn(ORDER_SAGA_NAME, SAGA_ID,
                        List.of(SagaStatus.PROCESSING));

        assertTrue(paymentOutboxEntity.isPresent());
    }

    private PaymentResponse getPaymentResponse() {
        return PaymentResponse.builder()
                .id(UUID.randomUUID().toString())
                .sagaId(SAGA_ID.toString())
                .paymentStatus(PaymentStatus.COMPLETED)
                .paymentId(PAYMENT_ID.toString())
                .orderId(ORDER_ID.toString())
                .customerId(CUSTOMER_ID.toString())
                .price(PRICE)
                .createdAt(Instant.now())
                .failureMessages(new ArrayList<>())
                .build();
    }
}
