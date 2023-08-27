INSERT INTO roles VALUES (1, 'ROLE_ADMIN');
INSERT INTO roles VALUES(2,'ROLE_USER');

INSERT INTO users VALUES(1,'admin@gmail.com','$2a$12$UQBpJuYn9nhq/MDdWBgw.OmcTrcaDhve03L0Tf89iemyxoZRrByIy','admin');
INSERT INTO users VALUES(2,'seller@gmail.com','$2a$12$UQBpJuYn9nhq/MDdWBgw.OmcTrcaDhve03L0Tf89iemyxoZRrByIy','user');

INSERT INTO user_roles values(1,1);
INSERT INTO user_roles values(2,2);