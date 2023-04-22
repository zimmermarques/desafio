package com.desafio.desafio.dominio;


import lombok.Getter;

public enum StatusProjetoEnum {
    EM_ANALISE("Em Análise", 1),
    ANALISE_REALIZADA("Análise realizada",2), 
    ANALISE_APROVADA("Análise aprovada",3), 
    INICIADO("Iniciado",4), 
    PLANEJADO("Planejado",5), 
    EM_ANDAMENTO("Em andamento",6), 
    ENCERRADO("Encerrado",7), 
    CANCELADO("Cancelado",8);

    @Getter
    private String label;

    @Getter
    private Integer indice;

    private StatusProjetoEnum(String label, Integer indice) {
        this.label = label;
        this.indice = indice;        
    }
}
