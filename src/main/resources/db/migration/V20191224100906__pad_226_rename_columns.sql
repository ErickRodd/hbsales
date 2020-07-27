ALTER TABLE categoria
    DROP CONSTRAINT UC_categoria_fornecedor;

ALTER TABLE categoria
    ALTER COLUMN codigo_categoria VARCHAR(10) NOT NULL;

EXEC sp_rename 'categoria.codigo_categoria', 'codigo', 'COLUMN';

EXEC sp_rename 'categoria.nome_categoria', 'nome', 'COLUMN';

ALTER TABLE categoria
    ADD CONSTRAINT UC_categoria_fornecedor UNIQUE (fornecedor, nome);