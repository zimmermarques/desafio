package com.desafio.desafio.dominio.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.desafio.desafio.dominio.Membro;


@RepositoryRestResource(collectionResourceRel = "membrosCollection", path = "membros")
public interface MembroRepositorio extends CrudRepository<Membro, Integer>{
    
    public List<Membro> findByNome(String nome);

}
