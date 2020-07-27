create table linha_categoria(
    id                                         BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    codigo_linha                                                     VARCHAR(10) NOT NULL,
    categoria_linha                                                       BIGINT NOT NULL,
    nome_linha                                                       VARCHAR(50) NOT NULL,
    CONSTRAINT fk_categoria_linha FOREIGN KEY (categoria_linha) REFERENCES categoria (id),
    CONSTRAINT uc_nome_categoria UNIQUE (nome_linha, categoria_linha)
);

create unique index ix_linha_categoria_01 on linha_categoria (codigo_linha asc);