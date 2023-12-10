package com.devsuperior.aulacrudclient.service;

import com.devsuperior.aulacrudclient.dto.ClientDTO;
import com.devsuperior.aulacrudclient.entity.Client;
import com.devsuperior.aulacrudclient.exception.DatabaseException;
import com.devsuperior.aulacrudclient.exception.ResourceNotFoundException;
import com.devsuperior.aulacrudclient.repository.ClientRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {


    private static String RESOUCE_NOT_FOUND = "Recurso não encontrado";
    private static String DATABASE_INTEGRITY = "Falha de integridade referencial";
    @Autowired
    private ClientRepository repository;


    @Transactional(readOnly = true)
    public Page<ClientDTO> findClientByPage(Pageable pageable) {
        Page<Client> clients = repository.findAll(pageable);
        return clients.map(ClientDTO::new);
    }

    @Transactional(readOnly = true)
    public ClientDTO findClientById(Long id) {
        Client client = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOUCE_NOT_FOUND));
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO) {
        Client client = new Client();
        copyDtoToEntity(clientDTO, client);
        client = repository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        try {
            Client client = repository.getReferenceById(id);
            copyDtoToEntity(clientDTO, client);
            client = repository.save(client);
            return new ClientDTO(client);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException(RESOUCE_NOT_FOUND);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (!repository.existsById(id)) {
                throw new ResourceNotFoundException(RESOUCE_NOT_FOUND);
            }
            repository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(DATABASE_INTEGRITY);
        }
    }


    private void copyDtoToEntity(ClientDTO dto, Client entity) {
        entity.setName(dto.getName());
        entity.setCpf(dto.getCpf());
        entity.setIncome(dto.getIncome());
        entity.setBirthDate(dto.getBirthDate());
        entity.setChildren(dto.getChildren());
    }


}
