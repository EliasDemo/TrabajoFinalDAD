package com.example.msclient.controller;

import com.example.msclient.entity.Client;
import com.example.msclient.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping()
    public ResponseEntity<List<Client>> list(){
        return ResponseEntity.ok().body(clientService.listar());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Client> findById(@PathVariable(required = true) Integer id){
        return ResponseEntity.ok().body(clientService.listarPorId(id).get());
    }
    @PostMapping()
    public ResponseEntity<Client> save(@RequestBody Client client){
        return ResponseEntity.ok().body(clientService.guardar(client));
    }
    @PutMapping()
    public ResponseEntity<Client> update(@RequestBody Client client){
        return ResponseEntity.ok().body(clientService.actualizar(client));
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable(required = true) Integer id){
        clientService.eliminar(id);
        return "eliminacion correcta";
    }
}
