-- USUÁRIOS
INSERT INTO T_USER (id, login, password, name, is_admin) VALUES (1000, '11111111111', '123456', 'Tiago', FALSE);
INSERT INTO T_USER (id, login, password, name, is_admin) VALUES (1001, '99999999999', '999999', 'Admin', TRUE);

-- PRODUTOS
INSERT INTO PRODUCT (id, name, barcode, price, quantity) VALUES (1000, 'Bolacha Danix', '7896058257274', 2.00, 22);
INSERT INTO PRODUCT (id, name, barcode, price, quantity) VALUES (1001, 'Bolacha Danix', '7896058257275', 2.00, 22);
INSERT INTO PRODUCT (id, name, barcode, price, quantity) VALUES (1002, 'Bolacha Danix', '7896058257276', 2.00, 22);
INSERT INTO PRODUCT (id, name, barcode, price, quantity) VALUES (1003, 'Bolacha Danix', '7896058257277', 2.00, 22);
INSERT INTO PRODUCT (id, name, barcode, price, quantity) VALUES (1004, 'Bolacha Danix', '7896058257278', 2.00, 22);
INSERT INTO PRODUCT (id, name, barcode, price, quantity) VALUES (1005, 'Bolacha Danix', '7896058257279', 2.00, 22);

-- ENDEREÇOS
INSERT INTO ADDRESS (id, street, number, neighborhood, city, t_state) VALUES (1000, 'Rua', 10, 'Bairro', 'Cidade', 'Estado');

-- CLIENTES
INSERT INTO CUSTOMER (id, name, cpf, address_id, phone) VALUES (1000, 'Tiago', '12345678910', 1000, '999999999');

-- 