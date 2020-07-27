create table funcionario(
    id                  BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    nome_funcionario                          VARCHAR(50) NOT NULL,
    email_funcionario                         VARCHAR(50) NOT NULL,
    uuid                                      VARCHAR(36) NOT NULL
);