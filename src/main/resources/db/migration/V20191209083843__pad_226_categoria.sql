create table categoria(
    id                                   BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    codigo_categoria                                           VARCHAR(10) NOT NULL,
    fornecedor                                                      BIGINT NOT NULL,
    nome_categoria                                             VARCHAR(50) NOT NULL,
    CONSTRAINT fk_fornecedor_cat FOREIGN KEY (fornecedor) REFERENCES fornecedor (id)
);

create unique index ix_categoria_01 on categoria (codigo_categoria asc);