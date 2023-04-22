package com.desafio.desafio.dominio;


import lombok.Getter;

public enum ClassificacaoProjetoEnum {
    BAIXO("Baixo Risco"),
    MEDIO("Médio Risco"),
    ALTO("Alto Risco");

    @Getter
    private String label;

    private ClassificacaoProjetoEnum(String label) {
        this.label = label;
    }

    
}
