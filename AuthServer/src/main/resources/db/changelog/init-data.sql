--liquibase formatted sql

--changeset dmitri:create_table_with_init_data_for_auth

CREATE TABLE users (
 id bigserial PRIMARY KEY,
 username VARCHAR(255),
 password VARCHAR(255),
 email VARCHAR(255)
);

CREATE TABLE roles (
 id bigserial PRIMARY KEY,
 role VARCHAR(100)
);

CREATE TABLE users_roles (
	id bigserial PRIMARY KEY,
	user_id bigint REFERENCES users(id),
	role_id bigint REFERENCES roles(id)
);

INSERT INTO roles (role) VALUES ('ROLE_ADMIN'),
('ROLE_JOURNALIST'),
('ROLE_SUBSCRIBER');

INSERT INTO users (username, password, email) VALUES ('admin', '$2a$10$pRaYduxPeaKD9omvbg6HVuLwm.42Jab4ZrN1R8LThiqpd8GiLwYgq', 'admin@mail.com'),
('Mark', '$2a$10$2bmSXKEN49zfUEJcvQS8F.n4.SsZcaXO7jkH2O98ua16dUQZXDid6', 'mark@mail.com'),
('Sema', '$2a$10$foiPj4ELEcDdu3.RiB75jeCMzuQu/EvkHf5CfjKHBICywKIUIK5kC', 'sema@mail.com'),
('David', '$2a$12$82gzZYshFrjh11xZHKTpcO67LnF2Ervl4xqeY4N0//pXUO1ayaCGe', 'david@mail.com');

INSERT INTO users_roles (user_id, role_id) VALUES (1,1), (2,2), (3,3), (4, 2);