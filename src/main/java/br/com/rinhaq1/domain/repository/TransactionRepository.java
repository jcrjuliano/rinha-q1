package br.com.rinhaq1.domain.repository;

import br.com.rinhaq1.domain.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{

    @Query("SELECT t FROM TransactionEntity t WHERE t.cliente.id = :clienteId ORDER BY t.realizadaEm DESC LIMIT 10")
    List<TransactionEntity> findTop10ByClienteIdOrderByRealizadaEmDesc(Long clienteId);

}
