package br.com.hbsis.periodo.vendas;

import java.time.LocalDate;

public class PeriodoVendasDTO {
    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Long fornecedor;
    private LocalDate dataRetirada;
    private String descricao;

    public PeriodoVendasDTO(Long id, LocalDate dataInicio, LocalDate dataFim, Long fornecedor, LocalDate dataRetirada, String descricao) {
        this.id = id;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.fornecedor = fornecedor;
        this.dataRetirada = dataRetirada;
        this.descricao = descricao;
    }

    public static PeriodoVendasDTO of (PeriodoVendas periodoVendas){
        return new PeriodoVendasDTO(
                periodoVendas.getId(),
                periodoVendas.getDataInicio(),
                periodoVendas.getDataFim(),
                periodoVendas.getFornecedor().getId(),
                periodoVendas.getDataRetirada(),
                periodoVendas.getDescricao()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public Long getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Long fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LocalDate getDataRetirada() {
        return dataRetirada;
    }

    public void setDataRetirada(LocalDate dataRetirada) {
        this.dataRetirada = dataRetirada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "PeriodoVendasDTO{" +
                "id=" + id +
                ", dataInicio=" + dataInicio +
                ", dataFim=" + dataFim +
                ", fornecedor=" + fornecedor +
                ", dataRetirada=" + dataRetirada +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
