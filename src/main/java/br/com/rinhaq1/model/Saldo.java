package br.com.rinhaq1.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class Saldo {
    private Long total = 0L;

    @JsonProperty("data_extrato")
    private OffsetDateTime dataExtrato;

    private Long limite = 0L;


    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public OffsetDateTime getDataExtrato() {
        return dataExtrato;
    }

    public void setDataExtrato(OffsetDateTime dataExtrato) {
        this.dataExtrato = dataExtrato;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }

    public Saldo(Long total, OffsetDateTime dataExtrato, Long limite) {
        this.total = total;
        this.dataExtrato = dataExtrato;
        this.limite = limite;
    }
}
