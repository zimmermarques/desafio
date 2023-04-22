package com.desafio.desafio.dominio.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.desafio.desafio.dominio.Pessoa;


@RepositoryRestResource(collectionResourceRel = "pessoasCollection", path = "pessoas")
public interface PessoaRepositorio extends CrudRepository<Pessoa, Integer>{
    
    public List<Pessoa> findByNomeIgnoreCase(String nome);

    public List<Pessoa> findByCpfIgnoreCase(String cpf);

}
