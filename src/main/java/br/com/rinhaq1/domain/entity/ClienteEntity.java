package br.com.rinhaq1.domain.entity;

import br.com.rinhaq1.model.ClienteReturnDto;
import br.com.rinhaq1.model.CreateClienteDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_clientes")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
