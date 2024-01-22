CREATE TABLE roles (
                       role_id              BIGSERIAL NOT NULL,
                       name                 VARCHAR(30) not null,
                       allowed_resource     VARCHAR(200),
                       allowed_read         BOOLEAN not null default FALSE,
                       allowed_create       BOOLEAN not null default FALSE,
                       allowed_update       BOOLEAN not null default FALSE,
                       allowed_delete       BOOLEAN not null default FALSE
);

ALTER TABLE roles ADD CONSTRAINT role_pk PRIMARY KEY ( role_id );

CREATE TABLE users_roles (
    user_id     BIGINT      NOT NULL,
    role_id     BIGINT      NOT NULL
);

ALTER TABLE users_roles
    ADD CONSTRAINT users_fk FOREIGN KEY (user_id)
    REFERENCES users (user_id);

ALTER TABLE users_roles
    ADD CONSTRAINT roles_fk FOREIGN KEY (role_id)
    REFERENCES roles (role_id);