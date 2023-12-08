CREATE TABLE projects(
                         project_id    BIGSERIAL NOT NULL,
                         oem           BIGINT not null,
                         supplier      BIGINT not null,
                         start_date    DATE,
                         description   VARCHAR(150),
                         manager       VARCHAR(30)
);
ALTER TABLE projects ADD CONSTRAINT projects_pk PRIMARY KEY (project_id);

ALTER TABLE projects ADD CONSTRAINT project_oem_id_fk FOREIGN KEY (oem) REFERENCES users (user_id);
ALTER TABLE projects ADD CONSTRAINT project_supplier_id_fk FOREIGN KEY (supplier) REFERENCES users (user_id);
