package com.desafio.desafio.dominio;


import lombok.Getter;

public enum AtribuicaoMembroEnum {
    FUNCIONARIO("Funcionário");

    @Getter
    private String label;

    private AtribuicaoMembroEnum(String label) {
        this.label = label;
    }

    
}
