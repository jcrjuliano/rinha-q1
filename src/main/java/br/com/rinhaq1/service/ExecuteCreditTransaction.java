package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.entity.TransactionEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.domain.repository.TransactionRepository;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class ExecuteCreditTransaction implements ExecuteTransactionInterface {

    private static final String CREDIT_TYPE = "c";

    private final ClienteRepository clienteRepository;

    private final TransactionRepository transactionRepository;

    public ExecuteCreditTransaction(ClienteRepository clienteRepository, TransactionRepository transactionRepository) {
        this.clienteRepository = clienteRepository;
        this.transactionRepository = transactionRepository;
    }
    @Override
    public boolean verify(TransactionParams params) {
        return params.tipo().equalsIgnoreCase(CREDIT_TYPE);
    }

    @Override
    public TransactionDTO execute(ClienteEntity cliente, TransactionParams params) throws UnprocessableEntity {
        Long valor = Long.parseLong(params.valor());
        if (valor < 0) {
            throw new UnprocessableEntity("Valor inválido para crédito.");
        }
        try {
            log.info("atualiando o saldo do cliente de: {} para: {}", cliente.getSaldo(), cliente.getSaldo() + valor);
            cliente.setSaldo(cliente.getSaldo() + valor);
            clienteRepository.save(cliente);
            log.info("salvo novo saldo do cliente.");

            OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
            TransactionEntity transaction = new TransactionEntity(cliente, valor, 'c', params.descricao(), data);
            log.info("criado nova transação do tipo crédito para o cliente: {} com valor: {}", cliente.getId(), valor);
            TransactionEntity savedTransaction = transactionRepository.save(transaction);
            log.info("salva nova transação com id: {}", savedTransaction.getId());
        }catch (Exception e){
            log.error("Erro ao realizar crédito para o cliente: {}", cliente.getId(), e);
            throw new UnprocessableEntity("Erro ao realizar crédito.");
        }

        return new TransactionDTO(cliente.getLimite(), cliente.getSaldo());
    }
}
