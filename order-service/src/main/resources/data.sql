INSERT INTO orders (order_status, total_price)
VALUES
    ('PENDING', 150.75),
    ('CONFIRMED', 320.00),
    ('PENDING', 540.99),
    ('CONFIRMED', 120.00),
    ('PENDING', 89.50),
    ('CONFIRMED', 250.00),
    ('PENDING', 430.10),
    ('CONFIRMED', 799.99),
    ('PENDING', 99.99),
    ('CONFIRMED', 140.45);


INSERT INTO order_item (product_id, quantity)
VALUES
    (1, 5001),
    (2, 5001),
    (3, 5001),
    (4, 5002),
    (5, 5002),
    (6, 5002),
    (8, 5003),
    (7, 5003),
    (9, 5003),
    (10, 5003);