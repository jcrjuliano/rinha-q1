package br.com.rinhaq1.model;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
public class ClienteWithTransactionsDto {
    private Saldo saldo;
    @JsonProperty("ultimas_transacoes")
    private List<Transaction> lastTransactions;

    public ClienteWithTransactionsDto(Saldo saldo, List<Transaction> lastTransactions) {
        this.saldo = saldo;
        this.lastTransactions = lastTransactions;
    }

    public static ClienteWithTransactionsDto fromEntity(ClienteEntity cliente, List<TransactionEntity> transactions) {
        OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
        Long clientSaldo = cliente.getSaldo() == null ? 0L : cliente.getSaldo();
        Saldo saldo = new Saldo(
                clientSaldo,
                data,
                cliente.getLimite()
        );

        List<Transaction> returnTransactions = transactions.isEmpty() ? List.of() :
                transactions.stream()
                        .map(it ->
                                new Transaction(
                                        it.getValor(),
                                        it.getTipo(),
                                        it.getDescricao(),
                                        it.getRealizadaEm()))
                        .toList();

        return new ClienteWithTransactionsDto(saldo, returnTransactions);
    }
}
