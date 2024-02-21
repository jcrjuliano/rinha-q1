package br.com.rinhaq1.domain.entity;

import br.com.rinhaq1.model.ClienteReturnDto;
import br.com.rinhaq1.model.CreateClienteDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "cliente")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "limite")
    private Long limite;

    @Column(name = "saldo")
    private Long saldo;

    public ClienteEntity(Long limite, Long saldo) {
        this.limite = limite;
        this.saldo = saldo;
    }
    public ClienteEntity() {
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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


    public static ClienteEntity fromDto(CreateClienteDto dto) {
        return new ClienteEntity(dto.limite(), dto.saldo());
    }

    public ClienteReturnDto toDto() {
        return new ClienteReturnDto(id, limite, saldo);
    }
}
