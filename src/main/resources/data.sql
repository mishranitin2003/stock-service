INSERT INTO retailer (id, retailer_name) values (1, 'One-Retailer');
INSERT INTO retailer (id, retailer_name) values (2, 'Two-Retailer');

INSERT INTO product (id, product, retailer_id, current_stock, min_stock, one_off_order, stock_blocked)
values (1, 'a', 1, 5, 4, null, false);

INSERT INTO product (id, product, retailer_id, current_stock, min_stock, one_off_order, stock_blocked)
values (2, 'b', 1, 8, 4, null, false);

INSERT INTO product (id, product, retailer_id, current_stock, min_stock, one_off_order, stock_blocked)
values (3, 'c', 1, 2, 4, null, true);

INSERT INTO product (id, product, retailer_id, current_stock, min_stock, one_off_order, stock_blocked)
values (4, 'd', 2, 0, 8, 15, false);

INSERT INTO product (id, product, retailer_id, current_stock, min_stock, one_off_order, stock_blocked)
values (5, 'e', 2, 1, 4, null, false);

