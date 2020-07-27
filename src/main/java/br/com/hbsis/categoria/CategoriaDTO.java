package br.com.hbsis.categoria;

public class CategoriaDTO {
    private Long id;
    private String codigo;
    private Long fornecedor;
    private String nome;

    public CategoriaDTO(Long id, String codigo, Long fornecedor, String nome) {
        this.id = id;
        this.codigo = codigo;
        this.fornecedor = fornecedor;
        this.nome = nome;
    }

    public static CategoriaDTO of (Categoria categoria){
        return new CategoriaDTO(
                categoria.getId(),
                categoria.getCodigo(),
                categoria.getFornecedor().getId(),
                categoria.getNome()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "CategoriaDTO{" +
                "id=" + id +
                ", codigoCategoria=" + codigo +
                ", nomeCategoria='" + nome + '\'' +
                ", fornecedor=" + fornecedor +
                '}';
    }
}
