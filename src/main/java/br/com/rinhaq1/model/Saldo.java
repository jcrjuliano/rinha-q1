package br.com.rinhaq1.model;

import java.sql.Date;

public class Saldo {
    private Long total;
    private Date dataExtrato;
    private Long limite;

    public Saldo(Long total, Date dataExtrato, Long limite) {
        this.total = total;
        this.dataExtrato = dataExtrato;
        this.limite = limite;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Date getDataExtrato() {
        return dataExtrato;
    }

    public void setDataExtrato(Date dataExtrato) {
        this.dataExtrato = dataExtrato;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }
}
