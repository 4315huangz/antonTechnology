insert into roles (id, name, allowed_resource, allowed_read, allowed_create, allowed_update, allowed_delete) values
                                                                                                             (1, 'Admin', '/', TRUE , TRUE, TRUE, TRUE),
                                                                                                             (2, 'Broker', '/projects,/project', TRUE , TRUE, TRUE, TRUE),
                                                                                                             (3, 'Broker', '/products,/product,/users,/user', TRUE , FALSE, FALSE, FALSE),
                                                                                                             (4, 'OEM', '/products,/product,/projects,/project,/users,/user', TRUE, FALSE, FALSE, FALSE),
                                                                                                             (5, 'Supplier', '/products,/product', TRUE, TRUE, TRUE, TRUE),
                                                                                                             (6, 'Supplier', '/projects,/project,/users,/user', TRUE, FALSE, FALSE, FALSE),

;
commit;

insert into accounts (id, name, password, first_name, last_name, email) values
                                                                     (1,'zhuang', '25f9e794323b453885f5181f1b624d0b', 'Ziwei', 'Huang', 'zhuang@antontechnology.com'),
                                                                     (2, 'dwang', '25f9e794323b453885f5181f1b624d0b', 'David', 'Wang', 'dwang@antontechnology.com'),
                                                                     (3, 'rhang', '25f9e794323b453885f5181f1b624d0b', 'Ryo', 'Hang', 'rhang@antontechnology.com'),
                                                                     (4, 'xyhuang', '25f9e794323b453885f5181f1b624d0b', 'Xinyue', 'Huang', 'xyhuang@antontechnology.com')
;
commit;

insert into accounts_roles values
                            (1, 1),
                            (2, 2),
                            (2, 3),
                            (2, 4),
                            (3, 5),
                            (3, 6),
                            (3, 7),
                            (4, 8),
                            (4, 9),
                            (4,10)
;
commit;