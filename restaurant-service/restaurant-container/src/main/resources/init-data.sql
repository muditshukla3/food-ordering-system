INSERT INTO restaurant.restaurants(id, name, active)
    VALUES('03f3a46a-4738-4081-85f5-b060892663d4', 'restaurant_1', TRUE);
INSERT INTO restaurant.restaurants(id, name, active)
    VALUES('d99eb44e-2ec9-4c92-a737-c2fb457d7e42', 'restaurant_2', FALSE);

INSERT INTO restaurant.products(id, name, price, available)
    VALUES('0bdaba0e-433f-4e5b-81e3-7665c5bfc6dd', 'product_1', 25.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
    VALUES('aa8149ff-0c7e-4d20-b66d-37de6f426e76', 'product_2', 50.00, TRUE);
INSERT INTO restaurant.products(id, name, price, available)
    VALUES('e4de53cd-d5e9-4dcc-8499-4c5a9db8dcd5', 'product_3', 20.00, FALSE);
INSERT INTO restaurant.products(id, name, price, available)
    VALUES('b776db1c-4e0d-4abc-a016-a031b03f3072', 'product_4', 40.00, TRUE);

INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES('e4ff557b-95fb-469a-8811-e5bf1a5377ea', '03f3a46a-4738-4081-85f5-b060892663d4', '0bdaba0e-433f-4e5b-81e3-7665c5bfc6dd');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES('9c87531b-d4f7-4f24-b8f8-6e7cb63c4ef3', '03f3a46a-4738-4081-85f5-b060892663d4', 'aa8149ff-0c7e-4d20-b66d-37de6f426e76');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES('fe689ae7-8eaf-49eb-80a2-083bfeb6e427', 'd99eb44e-2ec9-4c92-a737-c2fb457d7e42', 'e4de53cd-d5e9-4dcc-8499-4c5a9db8dcd5');
INSERT INTO restaurant.restaurant_products(id, restaurant_id, product_id)
    VALUES('5e543b50-82d7-43c8-8a79-b0336718d49e', 'd99eb44e-2ec9-4c92-a737-c2fb457d7e42', 'b776db1c-4e0d-4abc-a016-a031b03f3072');