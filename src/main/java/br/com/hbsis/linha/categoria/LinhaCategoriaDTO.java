package br.com.hbsis.linha.categoria;

public class LinhaCategoriaDTO {
    private Long id;
    private String codigo;
    private Long categoria;
    private String nome;

    public LinhaCategoriaDTO(Long id, String codigo, Long categoria, String nome) {
        this.id = id;
        this.codigo = codigo;
        this.categoria = categoria;
        this.nome = nome;
    }

    public static LinhaCategoriaDTO of (LinhaCategoria linhaCategoria){
        return new LinhaCategoriaDTO(
                linhaCategoria.getId(),
                linhaCategoria.getCodigo(),
                linhaCategoria.getCategoria().getId(),
                linhaCategoria.getNome()
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

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "LinhaCategoriaDTO{" +
                "id=" + id +
                ", codigoLinha='" + codigo + '\'' +
                ", categoria=" + categoria +
                ", nomeLinha='" + nome + '\'' +
                '}';
    }
}
