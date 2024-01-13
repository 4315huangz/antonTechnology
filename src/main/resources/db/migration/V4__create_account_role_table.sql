CREATE TABLE accounts (
                       id              BIGSERIAL NOT NULL,
                       name            VARCHAR(30) not null unique,
                       password        VARCHAR(64),
                       secret_key      varchar(512),
                       first_name      VARCHAR(30),
                       last_name       VARCHAR(30),
                       email           VARCHAR(50) not null unique
);

ALTER TABLE accounts ADD CONSTRAINT accounts_pk PRIMARY KEY ( id );

CREATE TABLE roles (
                       id                   BIGSERIAL NOT NULL,
                       name                 VARCHAR(30) not null unique,
                       allowed_resource     VARCHAR(200),
                       allowed_read         BOOLEAN not null default FALSE,
                       allowed_create       BOOLEAN not null default FALSE,
                       allowed_update       BOOLEAN not null default FALSE,
                       allowed_delete       BOOLEAN not null default FALSE
);

ALTER TABLE roles ADD CONSTRAINT role_pk PRIMARY KEY ( id );

CREATE TABLE accounts_roles (
    account_id     BIGINT      NOT NULL,
    role_id     BIGINT      NOT NULL
);

ALTER TABLE accounts_roles
    ADD CONSTRAINT accounts_fk FOREIGN KEY (account_id)
    REFERENCES accounts (id);

ALTER TABLE accounts_roles
    ADD CONSTRAINT roles_fk FOREIGN KEY (role_id)
    REFERENCES roles (id);