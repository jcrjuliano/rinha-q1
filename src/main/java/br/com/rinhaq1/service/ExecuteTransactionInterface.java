package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;

public interface ExecuteTransactionInterface {
    boolean verify(TransactionParams params);

    TransactionDTO execute(ClienteEntity cliente, TransactionParams params);
}
