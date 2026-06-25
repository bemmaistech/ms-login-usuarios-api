CREATE TABLE usuario (
                          id BIGSERIAL PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          telefone VARCHAR(20),
                          email VARCHAR(100),
                          senha VARCHAR(255) NOT NULL,
                          data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE confirmacao_email (
                                   id BIGSERIAL PRIMARY KEY,
                                   email VARCHAR(100) NOT NULL,
                                   codigo VARCHAR(6) NOT NULL,
                                   confirmado BOOLEAN DEFAULT FALSE,
                                   data_expiracao TIMESTAMP NOT NULL,
                                   data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
