-- Recria as tabelas com o padrão tb_ (reset completo de estrutura e dados)
DROP TABLE IF EXISTS tb_confirmacao_email;
DROP TABLE IF EXISTS tb_usuario;

DROP TABLE IF EXISTS confirmacao_email;
DROP TABLE IF EXISTS usuario;

CREATE TABLE tb_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) UNIQUE,
    email VARCHAR(100) UNIQUE,
    senha VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE tb_confirmacao_email (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    codigo VARCHAR(6) NOT NULL,
    confirmado BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expira_em TIMESTAMP
);

