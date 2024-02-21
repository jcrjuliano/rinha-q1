package br.com.rinhaq1.service;

import br.com.rinhaq1.domain.entity.ClienteEntity;
import br.com.rinhaq1.domain.repository.ClienteRepository;
import br.com.rinhaq1.model.ClienteReturnDto;
import br.com.rinhaq1.model.CreateClienteDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteReturnDto execute(CreateClienteDto clienteClienteDto) {
        return clienteRepository.save(ClienteEntity.fromDto(clienteClienteDto)).toDto();
    }
}