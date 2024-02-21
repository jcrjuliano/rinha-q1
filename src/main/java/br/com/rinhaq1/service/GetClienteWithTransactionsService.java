package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.domain.repository.TransactionRepository;
import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.model.ClienteWithTransactionsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetClienteWithTransactionsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClienteRepository clientRepository;

    public ClienteWithTransactionsDto execute(Long id) {
        ClienteEntity cliente =
                clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        List<TransactionEntity> transactions = transactionRepository.findByClienteId(id);


        return ClienteWithTransactionsDto.fromEntity(cliente, transactions);
    }
}
