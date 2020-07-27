create table produto(
    id                                  BIGINT IDENTITY(1, 1) PRIMARY KEY NOT NULL,
    codigo_produto                                     VARCHAR(10) UNIQUE NOT NULL,
    nome_produto                                             VARCHAR(200) NOT NULL,
    preco_produto                                          DECIMAL(10, 2) NOT NULL,
    linha                                                          BIGINT NOT NULL,
    un_caixa                                                      INTEGER NOT NULL,
    un_peso                                                DECIMAL(10, 3) NOT NULL,
    medida_peso                                                   VARCHAR NOT NULL,
    val_produto                                                      DATE NOT NULL,
    CONSTRAINT fk_produto_linha FOREIGN KEY (linha) REFERENCES linha_categoria (id)
);