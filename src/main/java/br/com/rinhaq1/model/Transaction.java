package br.com.rinhaq1.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

import java.time.OffsetDateTime;


@AllArgsConstructor
public class Transaction {
    private Long valor;

    private char tipo;

    private String descricao;

    @JsonProperty("realizada_em")
    private OffsetDateTime realizadaEm;

    public Transaction(Long valor, char tipo, String descricao, OffsetDateTime realizadaEm) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = realizadaEm;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OffsetDateTime getRealizadaEm() {
        return realizadaEm;
    }

    public void setRealizadaEm(OffsetDateTime realizadaEm) {
        this.realizadaEm = realizadaEm;
    }
}
