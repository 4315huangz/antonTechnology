CREATE TABLE projects(
                         project_id    BIGSERIAL NOT NULL,
                         start_date    DATE,
                         description   VARCHAR(150),
                         manager       VARCHAR(30),
);
ALTER TABLE projects ADD CONSTRAINT projects_pk PRIMARY KEY (project_id);
