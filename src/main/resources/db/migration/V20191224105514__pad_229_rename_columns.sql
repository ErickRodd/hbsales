ALTER TABLE linha_categoria
    DROP CONSTRAINT fk_categoria_linha;

ALTER TABLE linha_categoria
    DROP CONSTRAINT uc_nome_categoria;

ALTER TABLE linha_categoria
    ALTER COLUMN codigo_linha VARCHAR(10) NOT NULL;

EXEC sp_rename 'linha_categoria.codigo_linha', 'codigo', 'COLUMN';

EXEC sp_rename 'linha_categoria.categoria_linha', 'categoria', 'COLUMN';

EXEC sp_rename 'linha_categoria.nome_linha', 'nome', 'COLUMN';

ALTER TABLE linha_categoria
    ADD CONSTRAINT fk_categoria_linha FOREIGN KEY (categoria) REFERENCES categoria(id);

ALTER TABLE linha_categoria
    ADD CONSTRAINT uc_nome_categoria UNIQUE (nome, categoria);