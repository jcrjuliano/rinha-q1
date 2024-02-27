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

    @Override
    public TransactionDTO execute(ClienteEntity cliente, TransactionParams params) {
        Long valor = Long.parseLong(params.valor());
        log.info("iniciado verificação para fazer débito de valor: {}", valor);
        if (valor < 0) {
            throw new UnprocessableEntity("Valor inválido para débito.");
        }
        if (!verifyIfHasLimit(cliente, valor)) {
            throw new UnprocessableEntity("Limite insuficiente para realizar a transação.");
        }
        try {
            log.info("atualizado o saldo do cliente de: {} para: {}", cliente.getSaldo(), cliente.getSaldo() - valor);
            cliente.setSaldo(cliente.getSaldo() - valor);
            clienteRepository.save(cliente);
            log.info("salvo novo saldo do cliente.");

            OffsetDateTime data = OffsetDateTime.now(ZoneOffset.UTC);
            TransactionEntity transaction = new TransactionEntity(cliente, valor, 'd', params.descricao(), data);
            log.info("criado nova transação do tipo débico para o cliente: {} com valor: {}", cliente.getId(), valor);
            TransactionEntity savedTransaction = transactionRepository.save(transaction);
            log.info("salva nova transação com id: {}", savedTransaction.getId());
        }catch (Exception e){
            log.error("Erro ao realizar débito para o cliente: {}", cliente.getId(), e);
            throw new UnprocessableEntity("Erro ao realizar débito.");
        }
        return new TransactionDTO(cliente.getLimite(), cliente.getSaldo());
    }

    private boolean verifyIfHasLimit(ClienteEntity cliente, Long valor) {
        Long saldoMinusValue = cliente.getSaldo() - valor;
        return saldoMinusValue >= -cliente.getLimite();
    }
}
