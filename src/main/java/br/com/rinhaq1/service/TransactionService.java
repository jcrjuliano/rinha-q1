package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.domain.repository.TransactionRepository;
import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TransactionService {

    private char DEBIT_TYPE = 'd';
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public TransactionDTO execute(Long Id, TransactionParams params) throws RuntimeException {
        Optional<ClienteEntity> cliente = clienteRepository.findById(Id);

        if(cliente.isPresent()) {
            return params.tipo() == DEBIT_TYPE ?
                    validateAndSaveDebitTransaction(cliente.get(), params) : validateAndSaveCreditTransaction(cliente.get(), params);
        } else {
            throw new NotFoundException("Cliente não encontrado.");
        }
    }

    private TransactionDTO validateAndSaveCreditTransaction(ClienteEntity cliente, TransactionParams params) {
        Long saldoToAdd = params.valor() > 0 ? params.valor() : 0;
        cliente.setSaldo(cliente.getSaldo() + saldoToAdd);
        clienteRepository.save(cliente);
        saveTransaction(cliente, params);

        return new TransactionDTO(cliente.getLimite(), cliente.getSaldo());
    }

    private TransactionDTO validateAndSaveDebitTransaction(ClienteEntity cliente, TransactionParams params) throws UnprocessableEntity {
        Long newFunds = cliente.getSaldo() - params.valor();
        if (newFunds >= cliente.getLimite() * -1) {
            cliente.setSaldo(newFunds);
            clienteRepository.save(cliente);
            saveTransaction(cliente, params);
        } else throw new UnprocessableEntity("Saldo insuficiente para realizar a transação.");

        return new TransactionDTO(cliente.getLimite(), cliente.getSaldo());
    }

    private void saveTransaction(ClienteEntity cliente, TransactionParams params) {
        OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
        TransactionEntity transactionToSave = new TransactionEntity(cliente, params.valor(), params.tipo(), params.descricao(), data);

        transactionRepository.save(transactionToSave);
    }
}
