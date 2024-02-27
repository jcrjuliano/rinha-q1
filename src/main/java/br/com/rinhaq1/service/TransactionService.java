package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    public TransactionService(ClienteRepository clienteRepository, List<ExecuteTransactionInterface> transactionExecutors) {
        this.clienteRepository = clienteRepository;
        this.transactionExecutors = transactionExecutors;
    }

    private final ClienteRepository clienteRepository;

    private final List<ExecuteTransactionInterface> transactionExecutors;

    public TransactionDTO execute(Long Id, TransactionParams params) throws NotFoundException, UnprocessableEntity {
        Optional<ClienteEntity> cliente = clienteRepository.findById(Id);

        if (!validateParams(params)) {
            throw new UnprocessableEntity("Parâmetros inválidos.");
        } else if (cliente.isPresent()) {
            try {
                return transactionExecutors.stream()
                        .filter(transactionExecutor -> transactionExecutor.verify(params))
                        .findFirst()
                        .orElseThrow(() -> new UnprocessableEntity("Tipo de transação inválido."))
                        .execute(cliente.get(), params);
            } catch (UnprocessableEntity exception) {
                throw exception;
            }
        } else {
            throw new NotFoundException("Cliente não encontrado.");
        }
    }

    private boolean validateParams(TransactionParams params) {
        return params.descricao() != null && !params.descricao().isEmpty() && params.valor() > 0;
    }
}
