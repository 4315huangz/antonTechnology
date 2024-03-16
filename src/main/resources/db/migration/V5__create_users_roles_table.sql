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