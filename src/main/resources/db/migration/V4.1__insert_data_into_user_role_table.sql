insert into roles (name, allowed_resource, allowed_read, allowed_create, allowed_update, allowed_delete) values
                                                                                                             ('Admin', '/', TRUE , TRUE, TRUE, TRUE),
                                                                                                             ('Broker', '/projects,/project', TRUE , TRUE, TRUE, TRUE),
                                                                                                             ('Broker', '/products,/product,/users,/user', TRUE , FALSE, FALSE, FALSE),
                                                                                                             ('OEM', '/products,/product,/projects,/project,/users,/user', TRUE, FALSE, FALSE, FALSE),
                                                                                                             ('Supplier', '/products,/product', TRUE, TRUE, TRUE, TRUE),
                                                                                                             ('Supplier', '/projects,/project', TRUE, FALSE, FALSE, FALSE),
                                                                                                             ('Supplier', '/users,/user', FALSE, FALSE, FALSE, FALSE)

;
commit;

insert into users (user_name, password, first_name, last_name, email,company_name,company_type) values
                                                                     ('zhuang', '25f9e794323b453885f5181f1b624d0b', 'Ziwei', 'Huang', 'zhuang@antontechnology.com','Anton Technology',null),
                                                                     ('dwang', '25f9e794323b453885f5181f1b624d0b', 'David', 'Wang', 'dwang@antontechnology.com','Anton Technology','Broker'),
                                                                     ('rhang', '25f9e794323b453885f5181f1b624d0b', 'Ryo', 'Hang', 'rhang@antontechnology.com','Anton Technology','OEM'),
                                                                     ('xyhuang', '25f9e794323b453885f5181f1b624d0b', 'Xinyue', 'Huang', 'xyhuang@antontechnology.com','Anton Technology','Supplier')
;
commit;

insert into users_roles values
                            (1, 1),
                            (2, 2),
                            (2, 3),
                            (3, 4),
                            (4, 5),
                            (4, 6),
                            (4, 7)
;
commit;