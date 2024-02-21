package br.com.rinhaq1.model;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;

import java.sql.Date;
import java.util.List;

public class ClienteWithTransactionsDto {
    private Saldo saldo;
    private List<Transaction> lastTransactions;

    public ClienteWithTransactionsDto(Saldo saldo, List<Transaction> lastTransactions) {
        this.saldo = saldo;
        this.lastTransactions = lastTransactions;
    }

    public ClienteWithTransactionsDto(){}

    public Saldo getSaldo() {
        return saldo;
    }

    public void setSaldo(Saldo saldo) {
        this.saldo = saldo;
    }

    public List<Transaction> getLastTransactions() {
        return lastTransactions;
    }

    public void setLastTransactions(List<Transaction> lastTransactions) {
        this.lastTransactions = lastTransactions;
    }

    public static ClienteWithTransactionsDto fromEntity(ClienteEntity cliente, List<TransactionEntity> transactions) {
        Saldo saldo = new Saldo(
                cliente.getSaldo(),
                new Date(System.currentTimeMillis()),
                cliente.getLimite()
        );

        List<Transaction> returnTransactions = transactions.isEmpty() ? List.of() :
                transactions.stream().map(it -> new Transaction(it.getValor(), it.getTipo(), it.getDescricao(), it.getRealizadaEm())).toList();

        return new ClienteWithTransactionsDto(saldo, returnTransactions);
    }
}
