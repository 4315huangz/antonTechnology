CREATE TABLE projects(
                         project_id    BIGSERIAL NOT NULL,
                         user_id       BIGINT not null,
                         start_date    DATE,
                         description   VARCHAR(150),
                         manager       VARCHAR(30)
);
ALTER TABLE projects ADD CONSTRAINT projects_pk PRIMARY KEY (project_id);

ALTER TABLE projects
    ADD CONSTRAINT project_user_id_fk FOREIGN KEY (user_id)
        REFERENCES users (id);
