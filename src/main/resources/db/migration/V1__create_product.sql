CREATE TABLE products(
    product_id          BIGSERIAL NOT NULL,
    name        VARCHAR(30) not null,
    description VARCHAR(300),
    user_id     BIGINT NOT NULL
);
ALTER TABLE products ADD CONSTRAINT product_pk PRIMARY KEY (product_id);


