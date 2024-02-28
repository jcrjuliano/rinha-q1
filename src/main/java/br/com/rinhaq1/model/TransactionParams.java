package br.com.rinhaq1.model;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record TransactionParams(
        @PositiveOrZero
        @NotNull
        @Min(0)
        String valor,

        @NotBlank
        @Pattern(regexp = "^[cC|dD]$", message = "O campo deve ser 'c' ou 'd'.")
        String tipo,

        @Size(min = 1, max = 10)
        String descricao) {
}
