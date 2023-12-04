CREATE TABLE products(
    id          BIGSERIAL NOT NULL,
    name        VARCHAR(30) not null,
    description VARCHAR(150),
    company    VARCHAR(150),
    price       NUMERIC(10, 2)
);
ALTER TABLE products ADD CONSTRAINT product_pk PRIMARY KEY (id);


