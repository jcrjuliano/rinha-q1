package br.com.rinhaq1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Saldo {
    private Long total;

    @JsonProperty("data_extrato")
    private OffsetDateTime dataExtrato;
    private Long limite;

}
