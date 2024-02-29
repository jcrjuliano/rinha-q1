package br.com.rinhaq1.controllers;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.exception.NotFoundException;
import br.com.rinhaq1.exception.UnprocessableEntity;
import br.com.rinhaq1.model.ClienteWithTransactionsDto;
import br.com.rinhaq1.model.TransactionDTO;
import br.com.rinhaq1.model.TransactionParams;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;

@RestController
@RequestMapping("/clientes")
public class ClientController {
    private final ClienteRepository clientRepository;

    public ClientController(ClienteRepository clienteRepository) {
        this.clientRepository = clienteRepository;
    }

    @PostMapping("/{id}/transacoes")
    public ResponseEntity<TransactionDTO> addTransaction(@RequestBody @Valid TransactionParams params,
                                                         @PathVariable Long id) {
        if (!validateParams(params)) {
            return ResponseEntity.unprocessableEntity().build();
        }
        try {
            ClienteEntity client = clientRepository.findById(id);
            long valor = Long.parseLong(params.valor());
            if (valor < 0) {
                return ResponseEntity.unprocessableEntity().build();
            }

            OffsetDateTime currentDate = OffsetDateTime.now(ZoneOffset.UTC);
            String transactionData = buildTransactionData(params.tipo(), valor, params.descricao(), currentDate);

            return ResponseEntity.ok(clientRepository.searchAndUpdateClient(params, transactionData, id));
        } catch (UnprocessableEntity | IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/{id}/extrato")
    public ResponseEntity<ClienteWithTransactionsDto> getClienteWithTransactions(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        if (id > 5){
            return ResponseEntity.notFound().build();
        }

        try {
            ClienteEntity client = clientRepository.findById(id);

            ClienteWithTransactionsDto clientsWithTransactions = ClienteWithTransactionsDto.fromEntity(client);

            return ResponseEntity.ok(clientsWithTransactions);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    private boolean validateParams(TransactionParams params) {
        return
                params.descricao() != null
                        && !params.descricao().isBlank()
                        && params.descricao().length() <= 10
                        && params.tipo() != null
                        && params.tipo().trim().length() == 1
                        && (params.tipo().contains("c") || params.tipo().contains("d"));
    }

    private String buildTransactionData(String tipo, long valor, String descricao, OffsetDateTime realizadaEm) {
        return "{\"tipo\":\"" + tipo + "\", \"valor\":" + valor + ", \"descricao\":\"" + descricao +
                "\", \"realizadaEm\":\"" + realizadaEm.toString() + "\"}";
    }
}
