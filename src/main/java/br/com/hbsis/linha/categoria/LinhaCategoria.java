package br.com.hbsis.linha.categoria;

import br.com.hbsis.categoria.Categoria;

import javax.persistence.*;

@Entity
@Table(name = "linha_categoria")
public class LinhaCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 10, unique = true, nullable = false)
    private String codigo;

    @ManyToOne
    @JoinColumn(name = "categoria", referencedColumnName = "id", nullable = false)
    private Categoria categoria;

    @Column(name = "nome", length = 50, nullable = false)
    private String nome;

    public LinhaCategoria() {
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
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
        return "LinhaCategoria{" +
                "id=" + id +
                ", codigoLinha='" + codigo + '\'' +
                ", categoria=" + categoria +
                ", nomeLinha='" + nome + '\'' +
                '}';
    }
}
