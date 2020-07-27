alter table categoria
add constraint UC_categoria_fornecedor unique (fornecedor, nome_categoria);