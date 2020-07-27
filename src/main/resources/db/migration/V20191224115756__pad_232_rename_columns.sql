EXEC sp_rename 'produto.codigo_produto', 'codigo', 'COLUMN';

EXEC sp_rename 'produto.val_produto', 'validade', 'COLUMN';

EXEC sp_rename 'produto.preco_produto', 'preco', 'COLUMN';

EXEC sp_rename 'produto.nome_produto', 'nome', 'COLUMN';