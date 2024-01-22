CREATE TABLE users (
    user_id         BIGSERIAL NOT NULL,
    user_name       VARCHAR(30) not null unique,
    password        VARCHAR(64),
    secret_key      varchar(512),
    first_name      VARCHAR(30),
    last_name       VARCHAR(30),
    email           VARCHAR(50) not null unique,
    company_name    VARCHAR(30) not null,
    address         VARCHAR(150),
    industry        VARCHAR(30),
    title           VARCHAR(30),
    phone           VARCHAR(50),
    company_type    VARCHAR(50) CHECK (company_type IN ('OEM', 'Supplier', 'Broker'))
);
ALTER TABLE users ADD CONSTRAINT user_pk PRIMARY KEY (user_id);

