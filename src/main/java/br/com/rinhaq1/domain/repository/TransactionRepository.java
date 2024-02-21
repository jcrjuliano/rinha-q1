package br.com.rinhaq1.domain.repository;

import br.com.rinhaq1.domain.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long>{

    public List<TransactionEntity> findByClienteId(Long clientId);
}
