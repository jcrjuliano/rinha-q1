package br.com.rinhaq1.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class Transaction {
    private Long valor;

    private char tipo;

    private String descricao;

    @JsonProperty("realizada_em")
    private OffsetDateTime realizadaEm;
}
