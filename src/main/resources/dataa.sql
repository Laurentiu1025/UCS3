use ucs3;

--INSERTING ROLES
INSERT INTO role(name) VALUES ('ROLE_USER');
INSERT INTO role(name) VALUES ('ROLE_ADMIN');

INSERT INTO my_user(user_id, enabled, email, full_name, username, password, account_non_expired, account_non_locked, credentials_non_expired) VALUES (1, 1, 'admin_email', 'admin_lastname', 'admin_name', '$2a$04$aAiATQxdXgbVhzF3Eujl1umIGVzZUTYN4m6phRLUQdu.M.1ZS/Sye', true, true, true)
INSERT INTO my_user(user_id, enabled, email, full_name, username, password, account_non_expired, account_non_locked, credentials_non_expired) VALUES (2, 1, 'user_email@email.ro', 'user_lastName', 'user_name', '$2a$04$6BqxSLynCIDsRQdXPbO65e7ifpTaLY.IAu.ZFYIOkF63ABCGxp6lW', true, true, true)

--INSERTING RELATIONSHIP BETWEEN USER AND ROLES
    --USER IS A BASIC USER
    INSERT INTO `users_roles`(`user_id`, `role_id`) VALUES (1,1);

    --ADMIN IS AN USER, BUT ALSO AN ADMIN
    INSERT INTO `users_roles`(`user_id`, `role_id`) VALUES (2, 1);
    INSERT INTO `users_roles`(`user_id`, `role_id`) VALUES (2, 2);

    INSERT INTO PRODUCT (id, name, quantity) VALUES (null, 'Mouse', 50);
    INSERT INTO PRODUCT (id, name, quantity) VALUES (null, 'Keyboard', 5);
    INSERT INTO PRODUCT (id, name, quantity) VALUES (null, 'Monitor', 35);
