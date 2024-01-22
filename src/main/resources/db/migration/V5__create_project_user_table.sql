CREATE TABLE users_projects (
    user_id     BIGINT      NOT NULL,
    project_id  BIGINT      NOT NULL
);

ALTER TABLE projects DROP COLUMN user_id;

ALTER TABLE users_projects
    ADD CONSTRAINT users_fk FOREIGN KEY (user_id)
    REFERENCES users (user_id);

ALTER TABLE users_projects
    ADD CONSTRAINT projects_fk FOREIGN KEY (project_id)
    REFERENCES projects (project_id);