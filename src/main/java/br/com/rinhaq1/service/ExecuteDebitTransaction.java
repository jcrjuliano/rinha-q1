package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.domain.repository.TransactionRepository;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class ExecuteDebitTransaction implements ExecuteTransactionInterface {

    private static final String DEBIT_TYPE = "d";

    private final ClienteRepository clienteRepository;

    private final TransactionRepository transactionRepository;

    public ExecuteDebitTransaction(ClienteRepository clienteRepository, TransactionRepository transactionRepository) {
        this.clienteRepository = clienteRepository;
        this.transactionRepository = transactionRepository;
    }
    @Override
    public boolean verify(TransactionParams params) {
        return params.tipo().equalsIgnoreCase(DEBIT_TYPE);
    }

    @Transactional
    @Override
    public TransactionDTO execute(ClienteEntity cliente, TransactionParams params) {
        Long valor = (long) Math.max(params.valor(), 0);

        if (!verifyIfHasLimit(cliente, valor)) {
            throw new UnprocessableEntity("Limite insuficiente para realizar a transação.");
        }

        cliente.setSaldo(cliente.getSaldo() + valor);
        clienteRepository.save(cliente);

        OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
        TransactionEntity transaction = new TransactionEntity(cliente, valor, params.tipo().charAt(0), params.descricao(), data);
        transactionRepository.save(transaction);

        return new TransactionDTO(cliente.getLimite(), cliente.getSaldo());
    }

    private boolean verifyIfHasLimit(ClienteEntity cliente, Long valor) {
        Long saldoMinusValue = cliente.getSaldo() - valor;
        return saldoMinusValue >= -cliente.getLimite();
    }
}
