package br.com.rinhaq1.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
public class Transaction {
    private Long valor;

    private char tipo;

    private String descricao;

    private OffsetDateTime realizada_em;
}
