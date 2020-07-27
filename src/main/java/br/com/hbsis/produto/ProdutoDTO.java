package br.com.hbsis.produto;

import java.time.LocalDate;

public class ProdutoDTO {
    private Long id;
    private String codigo;
    private String nome;
    private Double preco;
    private Long linhaCategoria;
    private Integer unCaixa;
    private Double unPeso;
    private String medidaPeso;
    private LocalDate validade;

    public ProdutoDTO(Long id, String codigo, String nome, Double preco, Long linhaCategoria, Integer unCaixa, Double unPeso, String medidaPeso, LocalDate validade) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.linhaCategoria = linhaCategoria;
        this.unCaixa = unCaixa;
        this.unPeso = unPeso;
        this.medidaPeso = medidaPeso;
        this.validade = validade;
    }

    public static ProdutoDTO of (Produto produto){
        return new ProdutoDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getNome(),
                produto.getPreco(),
                produto.getLinhaCategoria().getId(),
                produto.getUnCaixa(),
                produto.getUnPeso(),
                produto.getMedidaPeso(),
                produto.getValidade()
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

    public Long getLinhaCategoria() {
        return linhaCategoria;
    }

    public void setLinhaCategoria(Long linhaCategoria) {
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
        return "ProdutoDTO{" +
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
