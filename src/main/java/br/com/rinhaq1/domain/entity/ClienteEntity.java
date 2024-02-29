package br.com.rinhaq1.domain.entity;

import br.com.rinhaq1.model.ClienteReturnDto;
import br.com.rinhaq1.model.CreateClienteDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;


@Entity
@Table(name = "tb_clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String nome;

    @Column(name = "limite")
    private Long limite;

    @Column(name = "saldo")
    private Long saldo;

    @Column(columnDefinition = "text[]")
    private List<String> transactions = List.of();

    public ClienteEntity(String nome, Long limite, Long saldo) {
        this.nome = nome;
        this.limite = limite;
        this.saldo = saldo;
    }

    public static ClienteEntity fromDto(CreateClienteDto dto) {
        return new ClienteEntity(dto.nome(), dto.limite(), dto.saldo());
    }

    public ClienteReturnDto toDto() {
        return new ClienteReturnDto(id, nome, limite, saldo);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getLimite() {
        return limite;
    }

    public void setLimite(Long limite) {
        this.limite = limite;
    }

    public Long getSaldo() {
        return saldo;
    }

    public void setSaldo(Long saldo) {
        this.saldo = saldo;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    public ClienteEntity() {

    }

    public ClienteEntity(Long id, String nome, Long limite, Long saldo, List<String> transactions) {
        this.id = id;
        this.nome = nome;
        this.limite = limite;
        this.saldo = saldo;
        this.transactions = transactions;
    }
}
