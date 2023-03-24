package com.food.ordering.system.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.domain.entity.Customer;
import com.food.ordering.system.order.domain.entity.Order;
import com.food.ordering.system.order.domain.entity.Product;
import com.food.ordering.system.order.domain.entity.Restaurant;
import com.food.ordering.system.order.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.dto.create.OrderAddress;
import com.food.ordering.system.order.service.dto.create.OrderItem;
import com.food.ordering.system.order.service.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.outbox.model.payment.OrderPaymentEventPayload;
import com.food.ordering.system.order.service.outbox.model.payment.OrderPaymentOutboxMessage;
import com.food.ordering.system.order.service.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.ports.output.repository.*;
import com.food.ordering.system.outbox.OutboxStatus;
import com.food.ordering.system.saga.SagaStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.food.ordering.system.saga.order.SagaConstants.ORDER_SAGA_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
public class OrderApplicationServiceTest {

    @Autowired
    private OrderApplicationService orderApplicationService;

    @Autowired
    private OrderDataMapper orderDataMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private PaymentOutboxRepository paymentOutboxRepository;

    @Autowired
    private ApprovalOutboxRepository approvalOutboxRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateOrderCommand createOrderCommand;
    private CreateOrderCommand createOrderCommandWrongPrice;
    private CreateOrderCommand createOrderCommandWrongProductPrice;

    private final UUID CUSTOMER_ID = UUID.fromString("19c0d970-6f99-48ea-b04f-1f31c29589ac");
    private final UUID RESTAURANT_ID= UUID.fromString("0f9894b1-7496-4e0c-b43e-78df5699ad44");
    private final UUID PRODUCT_ID=UUID.fromString("307f96e5-39b7-47da-8432-2765171d0b2f");
    private final UUID ORDER_ID=UUID.fromString("55260d06-6c14-43f3-8242-dfd852725ab8");
    private final UUID SAGA_ID = UUID.fromString("55260d06-6c14-43f3-8242-dfd852725ab8");
    private final BigDecimal PRICE=new BigDecimal("200.00");

    @BeforeAll
    public void init(){
        createOrderCommand = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1").postalCode("1111AB").city("Paris").build())
                .price(PRICE)
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                               OrderItem.builder()
                                       .productId(PRODUCT_ID)
                                       .quantity(3)
                                       .price(new BigDecimal("50.00"))
                                       .subTotal(new BigDecimal("150.00"))
                                        .build()))
                            .build();

        createOrderCommandWrongPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1").postalCode("1111AB").city("Paris").build())
                .price(new BigDecimal("250.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("50.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
                .customerId(CUSTOMER_ID)
                .restaurantId(RESTAURANT_ID)
                .address(OrderAddress.builder()
                        .street("street_1").postalCode("1111AB").city("Paris").build())
                .price(new BigDecimal("210.00"))
                .items(List.of(OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(1)
                                .price(new BigDecimal("60.00"))
                                .subTotal(new BigDecimal("60.00"))
                                .build(),
                        OrderItem.builder()
                                .productId(PRODUCT_ID)
                                .quantity(3)
                                .price(new BigDecimal("50.00"))
                                .subTotal(new BigDecimal("150.00"))
                                .build()))
                .build();

        Customer customer = new Customer(new CustomerId(CUSTOMER_ID));

        Restaurant restaurant =  Restaurant.builder()
                .restaurantId(new RestaurantId(RESTAURANT_ID))
                .products(List.of(new Product(new ProductId(PRODUCT_ID),"product_1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),"product_2",
                                new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
        when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurant));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(paymentOutboxRepository.save(any(OrderPaymentOutboxMessage.class))).thenReturn(getOrderPaymentOutboxMessage());
    }

    private OrderPaymentOutboxMessage getOrderPaymentOutboxMessage() {
        OrderPaymentEventPayload orderPaymentEventPayload = OrderPaymentEventPayload.builder()
                    .orderId(ORDER_ID.toString())
                    .customerId(CUSTOMER_ID.toString())
                    .price(PRICE)
                    .createdAt(ZonedDateTime.now())
                    .paymentOrderStatus(PaymentOrderStatus.PENDING.name())
                    .build();

        return OrderPaymentOutboxMessage.builder()
                .id(UUID.randomUUID())
                .sagaId(SAGA_ID)
                .createdAt(ZonedDateTime.now())
                .type(ORDER_SAGA_NAME)
                .payload(createPayload(orderPaymentEventPayload))
                .orderStatus(OrderStatus.PENDING)
                .sagaStatus(SagaStatus.STARTED)
                .outboxStatus(OutboxStatus.STARTED)
                .version(0)
                .build();
    }

    private String createPayload(OrderPaymentEventPayload orderPaymentEventPayload) {
        try {
            return objectMapper.writeValueAsString(orderPaymentEventPayload);
        } catch (JsonProcessingException e) {
            throw new OrderDomainException("Cannot create OrderPaymentEventPayload object!");
        }
    }

    @Test
    public void testCreateOrder(){
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        Assertions.assertEquals(OrderStatus.PENDING,createOrderResponse.getOrderStatus());
        Assertions.assertEquals("Order Created Successfully",createOrderResponse.getMessage());
        Assertions.assertNotNull(createOrderResponse.getOrderTrackingId());
    }

    @Test
    public void testCreateOrderWrongTotalPrice(){
        OrderDomainException ode = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
        Assertions.assertEquals("Total price 250.00 is not equal to Order items total: 200.00", ode.getMessage());

    }

    @Test
    public void testCreateOrderWrongProductPrice(){
        OrderDomainException ode = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
        Assertions.assertEquals("Order item price: 60.00 is not valid for product "+PRODUCT_ID,ode.getMessage());
    }

    @Test
    public void testCreateOrderWithPassiveRestaurant(){
        Restaurant restaurantResponse =  Restaurant.builder()
                .restaurantId(new RestaurantId(RESTAURANT_ID))
                .products(List.of(new Product(new ProductId(PRODUCT_ID),"product_1",
                                new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID),"product_2",
                                new Money(new BigDecimal("50.00")))))
                .active(false)
                .build();
        when(restaurantRepository.findRestaurantInformation(
                orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .thenReturn(Optional.of(restaurantResponse));

        OrderDomainException ode = Assertions.assertThrows(OrderDomainException.class,
                () -> orderApplicationService.createOrder(createOrderCommand));
        Assertions.assertEquals("Restaurant with id "+RESTAURANT_ID+" is not active",ode.getMessage());
    }
}
