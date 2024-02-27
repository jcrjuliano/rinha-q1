package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.domain.repository.TransactionRepository;
import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.model.ClienteWithTransactionsDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class GetClienteWithTransactionsService {

    private final TransactionRepository transactionRepository;

    private final ClienteRepository clientRepository;

    public GetClienteWithTransactionsService(TransactionRepository transactionRepository, ClienteRepository clientRepository) {
        this.transactionRepository = transactionRepository;
        this.clientRepository = clientRepository;
    }

    public ClienteWithTransactionsDto execute(Long id) {
        ClienteEntity cliente =
                clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        log.info("Buscando transações para o cliente com id: {}", id);
        List<TransactionEntity> transactions = transactionRepository.findTop10ByClienteIdOrderByRealizadaEmDesc(id);
        log.info("Foram encontradas: {} transações para o cliente com id: {}", transactions.size(), id);


        return ClienteWithTransactionsDto.fromEntity(cliente, transactions);
    }
}
