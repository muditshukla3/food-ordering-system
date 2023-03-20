insert into "order".orders(id, customer_id, restaurant_id, tracking_id, price, order_status, failure_messages)
values('337489df-5f26-48f4-869d-0c6641f8aad3', 'a6c2cd40-f1ef-46f9-a5f0-3e2ea4039ec5','fd755187-7e95-41d7-b552-9b7e1d144e26',
            '3df9ebe7-3b53-40e8-97ca-4b634dc99ce6', 100.00, 'PENDING','');

insert into "order".orders_items(id, order_id, product_id, price, quantity, sub_total)
values(1,'337489df-5f26-48f4-869d-0c6641f8aad3','3d6a6310-a8b2-46b0-b1f2-fab4121dc4b2', 100.00, 1, 100.00);

insert into "order".order_address(id, order_id, street, postal_code, city)
values('9d8b889c-8978-4940-a07c-e6d45cb2060c','337489df-5f26-48f4-869d-0c6641f8aad3','test_street','100AA', 'test_city');

insert into "order".payment_outbox(id, saga_id, created_at, type, payload, outbox_status, saga_status, order_status, version)
values('02ab47c5-3836-4971-b53b-227904fa06b3','881da7d1-d225-4c86-b168-23512c28fbcc',current_timestamp, 'OrderProcessingSaga',
'{"price": 100, "orderId":"337489df-5f26-48f4-869d-0c6641f8aad3","createdAt":"2022-03-20T16:21:42.917756+01:00","customerId":"a6c2cd40-f1ef-46f9-a5f0-3e2ea4039ec5","paymentOrderStatus":"PENDING"}'
,'STARTED','STARTED','PENDING', 0);