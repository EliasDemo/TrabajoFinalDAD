package com.example.msclient.service;


import com.example.msclient.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    public List<Client> listar();
    public Optional<Client> listarPorId(Integer id);
    public Client guardar(Client client);
    public Client actualizar(Client client);
    public void eliminar(Integer id);
}
