
CREATE TABLE users(
                      id          BIGSERIAL NOT NULL,
                      address     VARCHAR(150),
                      title       VARCHAR(30),
                      email       VARCHAR(50),
                      type        VARCHAR(50),
                      product_id  BIGINT NOT NULL
);
ALTER TABLE users ADD CONSTRAINT user_pk PRIMARY KEY (id);

ALTER TABLE users
    ADD CONSTRAINT user_product_fk FOREIGN KEY (product_id)
        REFERENCES products (id);
