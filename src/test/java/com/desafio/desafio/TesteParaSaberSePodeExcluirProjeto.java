package com.desafio.desafio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.desafio.desafio.dominio.Projeto;

public class TesteParaSaberSePodeExcluirProjeto {
    
    @Test
    public void test(){
        Projeto projeto = new Projeto();
        projeto.colocar_EM_ANALISE();
        projeto.darContinuidade();
        projeto.darContinuidade();
        projeto.darContinuidade();

        assertEquals(false, projeto.isPodeExcluir()); 

    }


}
