package com.desafio.desafio.dominio.repositorio;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.desafio.desafio.dominio.Projeto;



@RepositoryRestResource(collectionResourceRel = "projetosCollection", path = "projetos")
public interface ProjetoRepositorio extends CrudRepository<Projeto, Integer>{
    
}
