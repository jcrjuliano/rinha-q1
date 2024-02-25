package br.com.rinhaq1.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "tb_transactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = ClienteEntity.class)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    private ClienteEntity cliente;

    @Column(name = "valor")
    private Long valor;

    @Column(name = "tipo")
    private char tipo;

    @Column(name = "descricao")
     private String descricao;

    @Column(name = "realizada_em")
    private OffsetDateTime realizadaEm;

    public TransactionEntity(ClienteEntity cliente, Long valor, char tipo, String descricao, OffsetDateTime data) {
        this.cliente = cliente;
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.realizadaEm = data;
    }
}
