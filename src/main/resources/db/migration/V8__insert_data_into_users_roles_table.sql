insert into roles
    (name, allowed_resource, allowed_read, allowed_create, allowed_update, allowed_delete)
values
    ('Admin', '/', TRUE , TRUE, TRUE, TRUE),
    ('Broker', '/projects,/project', TRUE , TRUE, TRUE, TRUE),
    ('Broker', '/products,/product,/users,/user', TRUE , FALSE, FALSE, FALSE),
    ('OEM', '/products,/product,/projects,/project,/users,/user', TRUE, FALSE, FALSE, FALSE),
    ('Supplier', '/products,/product', TRUE, TRUE, TRUE, TRUE),
    ('Supplier', '/projects,/project', TRUE, FALSE, FALSE, FALSE),
    ('Supplier', '/users,/user', FALSE, FALSE, FALSE, FALSE);
commit;

insert into
    users (user_name, password, first_name, last_name, email, company_name, company_type)
values
    ('testAdmin', '25f9e794323b453885f5181f1b624d0b', 'Administer', 'Test', 'testAdmin@antontechnology.com','Anton Technology',null),
    ('testBroker', '25f9e794323b453885f5181f1b624d0b', 'Broker', 'Test', 'testBroker@antontechnology.com','Anton Technology','Broker'),
    ('testOem', '25f9e794323b453885f5181f1b624d0b', 'OEM', 'Test', 'testOem@antontechnology.com','Anton Technology','OEM'),
    ('testSupplier', '25f9e794323b453885f5181f1b624d0b', 'Supplier', 'Test', 'testSupplier@antontechnology.com','Anton Technology','Supplier');
commit;

insert into users_roles
values
    (1, 1),
    (2, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (4, 6),
    (4, 7);
commit;