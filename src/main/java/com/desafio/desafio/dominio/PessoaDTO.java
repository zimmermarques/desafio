package com.desafio.desafio.dominio;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class PessoaDTO implements Serializable{

    private Integer id;

    private String nome;

    private Date datanascimento;

    private String cpf;

    private boolean funcionario;
    
}
