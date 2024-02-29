package br.com.rinhaq1.model;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@NoArgsConstructor
public class ClienteWithTransactionsDto {
    private Saldo saldo;
    @JsonProperty("ultimas_transacoes")
    private List<Transaction> lastTransactions;

    public ClienteWithTransactionsDto(Saldo saldo, List<Transaction> lastTransactions) {
        this.saldo = saldo;
        this.lastTransactions = lastTransactions;
    }

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

    public static ClienteWithTransactionsDto fromEntity(ClienteEntity client) {
        OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
        Saldo saldo = new Saldo(
                client.getSaldo(),
                data,
                client.getLimite()
        );
        List<String> transactions = client.getTransactions() == null ?
                List.of() :
                client.getTransactions().subList(0, Math.min(client.getTransactions().size(), 10));

        List<Transaction> returnTransactions = transactions.isEmpty() ? List.of() :
                transactions.stream()
                        .map(jsonString -> {
                            try {
                                JSONObject jsonObject = new JSONObject(jsonString);

                                return new Transaction(
                                        jsonObject.getLong("valor"),
                                        jsonObject.getString("tipo").charAt(0),
                                        jsonObject.getString("descricao"),
                                        OffsetDateTime.parse(jsonObject.getString("realizadaEm"))
                                );
                            } catch (Exception e) {
                                throw new RuntimeException("Erro ao converter JSON para Transaction", e);
                            }
                        })
                        .toList();

        return new ClienteWithTransactionsDto(saldo, returnTransactions);


    }
}
