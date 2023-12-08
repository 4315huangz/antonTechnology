CREATE TABLE users (
    user_id          BIGSERIAL NOT NULL,
    company_name VARCHAR(30) not null,
    address     VARCHAR(150),
    Industry    VARCHAR(30),
    manager_name  VARCHAR(30) not null,
    title       VARCHAR(30),
    email       VARCHAR(50),
    phone        VARCHAR(50),
    TYPE VARCHAR(20) NOT NULL CHECK (TYPE IN ('OEM', 'Supplier')),
    product_id  BIGINT
);
ALTER TABLE users ADD CONSTRAINT user_pk PRIMARY KEY (user_id);

ALTER TABLE users ADD CONSTRAINT user_product_fk FOREIGN KEY (product_id) REFERENCES products(id);