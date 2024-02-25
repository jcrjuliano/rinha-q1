package br.com.rinhaq1.model;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
public class ClienteWithTransactionsDto {
    private Saldo saldo;
    private List<Transaction> lastTransactions;

    public ClienteWithTransactionsDto(Saldo saldo, List<Transaction> lastTransactions) {
        this.saldo = saldo;
        this.lastTransactions = lastTransactions;
    }

    public static ClienteWithTransactionsDto fromEntity(ClienteEntity cliente, List<TransactionEntity> transactions) {
        OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
        Saldo saldo = new Saldo(
                cliente.getSaldo(),
                data,
                cliente.getLimite()
        );

        List<Transaction> returnTransactions = transactions.isEmpty() ? List.of() :
                transactions.stream().map(it -> new Transaction(it.getValor(), it.getTipo(), it.getDescricao(), it.getRealizadaEm())).toList();

        return new ClienteWithTransactionsDto(saldo, returnTransactions);
    }
}
