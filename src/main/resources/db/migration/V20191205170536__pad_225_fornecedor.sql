create table fornecedor(
    id                 BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    razao_social                            VARCHAR(100) NOT NULL,
    cnpj                                    VARCHAR(14)  NOT NULL,
    nome_fantasia                           VARCHAR(100) NOT NULL,
    endereco                                VARCHAR(100) NOT NULL,
    tel_contato                             VARCHAR(14)  NOT NULL,
    email_contato                           VARCHAR(50)  NOT NULL
);
create unique index ix_fornecedor_01 on fornecedor (cnpj asc);