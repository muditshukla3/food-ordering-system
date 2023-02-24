INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
    VALUES('ce34f412-0196-43ef-8827-ece76dea542a','f003e21b-9dc1-41ef-89bb-0895a6043c2a', 500.00);

INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('71ff6f1c-907a-4ad4-9eea-ace98f2a7ef7','f003e21b-9dc1-41ef-89bb-0895a6043c2a', 100.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('77713485-9292-4522-99b9-895fcba1777a','f003e21b-9dc1-41ef-89bb-0895a6043c2a', 600.00, 'CREDIT');
INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('7d6bc930-d7af-446a-aef1-beed96d2ad8a','f003e21b-9dc1-41ef-89bb-0895a6043c2a', 200.00, 'DEBIT');

INSERT INTO payment.credit_entry(id, customer_id, total_credit_amount)
    VALUES('4899c778-f680-4ade-8687-9062b9a9015c','32800f12-47db-4bcf-b9ef-e726a7b111d0', 100.00);

INSERT INTO payment.credit_history(id, customer_id, amount, type)
    VALUES('d8753ba6-8076-48d3-82c7-933e1f700692','32800f12-47db-4bcf-b9ef-e726a7b111d0', 100.00, 'CREDIT');
