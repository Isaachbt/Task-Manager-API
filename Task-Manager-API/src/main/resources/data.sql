CREATE TABLE TB_USERS (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(5) NOT NULL
);
INSERT INTO TB_USERS (id, nome, email, senha, role) VALUES
    (1, 'Gui', 'gui@gmail.com', '$2a$10$pNEiUh7lxS.PWREcCvnzzemGLeZ5aTHuOXZsRBR47QoDb/5XUBFFy', 'USER');

-- Senha do usuário 'gui@gmail.com' é: senha123, utilizada para testes no postman.

