create table periodo_vendas(
    id                                     BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    data_inicio                                                         DATE NOT NULL,
    data_fim                                                            DATE NOT NULL,
    fornecedor                                                        BIGINT NOT NULL,
    data_retirada                                                       DATE NOT NULL,
    descricao                                                    VARCHAR(50) NOT NULL,
    CONSTRAINT FK_periodo_fornecedor FOREIGN KEY (fornecedor) REFERENCES fornecedor (id)
);