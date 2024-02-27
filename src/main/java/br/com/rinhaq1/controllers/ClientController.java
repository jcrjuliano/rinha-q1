package br.com.rinhaq1.controllers;

import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.ClienteWithTransactionsDto;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import br.com.rinhaq1.service.GetClienteWithTransactionsService;
import br.com.rinhaq1.service.TransactionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clientes")
@Slf4j
public class ClientController {

    private final TransactionService transactionService;

    private final GetClienteWithTransactionsService getClienteWithTransactionsService;

    public ClientController(TransactionService transactionService, GetClienteWithTransactionsService getClienteWithTransactionsService) {
        this.transactionService = transactionService;
        this.getClienteWithTransactionsService = getClienteWithTransactionsService;
    }

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody TransactionParams params, @PathVariable Long id) {
        try {
            log.info("Recebendo requisição para adicionar transação para o cliente com id: {} e parâmetros: {}", id, params);
            TransactionDTO foundTransactions = transactionService.execute(id, params);
            log.info("Transação adicionada com sucesso para o cliente com id: {}", id);
            return ResponseEntity.ok(foundTransactions);
        } catch (UnprocessableEntity e) {
            log.error("Erro ao adicionar transação para o cliente com id: {}", id, e);
            return ResponseEntity.unprocessableEntity().build();
        } catch (NotFoundException e) {
            log.error("Cliente nao encontrado com id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Erro ao adicionar transação para o cliente com id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}/extrato")
    public ResponseEntity<ClienteWithTransactionsDto> getClienteWithTransactions(@PathVariable Long id) {
        try {
            log.info("Recebendo requisição para buscar cliente com transações com id: {}", id);
            ClienteWithTransactionsDto foundCliente = getClienteWithTransactionsService.execute(id);
            log.info("Cliente com transações encontrado com sucesso com id: {}", id);
            return ResponseEntity.ok(foundCliente);
        } catch (NotFoundException e) {
            log.error("Cliente nao encontrado com id: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Erro ao buscar cliente com transações com id: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
