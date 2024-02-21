package br.com.rinhaq1.controllers;

import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.ClienteReturnDto;
import br.com.rinhaq1.model.ClienteWithTransactionsDto;
import br.com.rinhaq1.model.CreateClienteDto;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import br.com.rinhaq1.service.GetClienteWithTransactionsService;
import br.com.rinhaq1.service.SaveClienteService;
import br.com.rinhaq1.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/clientes")
public class ClientController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private SaveClienteService saveClienteService;

    @Autowired
    private GetClienteWithTransactionsService getClienteWithTransactionsService;


    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody TransactionParams params, @PathVariable Long id) {
        try {
            TransactionDTO foundTransactions = transactionService.execute(id, params);
            return ResponseEntity.ok(foundTransactions);
        }catch (UnprocessableEntity e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
        }
        catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping()
    public ResponseEntity<ClienteReturnDto> saveCliente(@RequestBody CreateClienteDto clienteClienteDto) {
        ClienteReturnDto savedCliente = saveClienteService.execute(clienteClienteDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedCliente);
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<ClienteWithTransactionsDto> getClienteWithTransactions(@PathVariable Long id) {
        try {
            ClienteWithTransactionsDto foundCliente = getClienteWithTransactionsService.execute(id);

            return ResponseEntity.ok(foundCliente);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
