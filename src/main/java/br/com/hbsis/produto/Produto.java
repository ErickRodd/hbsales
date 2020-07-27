package br.com.hbsis.produto;

import br.com.hbsis.linha.categoria.LinhaCategoria;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produto")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", length = 10, unique = true, nullable = false)
    private String codigo;

    @Column(name = "nome", length = 200, nullable = false)
    private String nome;

    @Column(name = "preco", nullable = false)
    private Double preco;

    @ManyToOne
    @JoinColumn(name = "linha", referencedColumnName = "id", nullable = false)
    private LinhaCategoria linhaCategoria;

    @Column(name = "un_caixa", nullable = false)
    private Integer unCaixa;

    @Column(name = "un_peso", nullable = false)
    private Double unPeso;

    @Column(name = "medida_peso", nullable = false)
    private String medidaPeso;

    @Column(name = "validade", nullable = false)
    private LocalDate validade;

    public Produto() {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public LinhaCategoria getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(LinhaCategoria linhaCategoria) {
        this.linhaCategoria = linhaCategoria;
    }

    public Integer getUnCaixa() {
        return unCaixa;
    }

    public void setUnCaixa(Integer unCaixa) {
        this.unCaixa = unCaixa;
    }

    public Double getUnPeso() {
        return unPeso;
    }

    public void setUnPeso(Double unPeso) {
        this.unPeso = unPeso;
    }

    public String getMedidaPeso() {
        return medidaPeso;
    }

    public void setMedidaPeso(String medidaPeso) {
        this.medidaPeso = medidaPeso;
    }

    public LocalDate getValidade() {
        return validade;
    }

    public void setValidade(LocalDate validade) {
        this.validade = validade;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", codigoProduto='" + codigo + '\'' +
                ", nomeProduto='" + nome + '\'' +
                ", precoProduto=" + preco +
                ", linhaCategoria=" + linhaCategoria +
                ", unCaixa=" + unCaixa +
                ", unPeso=" + unPeso +
                ", medidaPeso='" + medidaPeso + '\'' +
                ", valProduto=" + validade +
                '}';
    }
}
