package com.desafio.desafio;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.desafio.desafio.dominio.Projeto;
import com.desafio.desafio.dominio.StatusProjetoEnum;

@SpringBootTest
public class TesteParaSaberQualProximoStatusProjeto {
    
    @Test
    public void test(){
        Projeto projeto = new Projeto();
        projeto.colocarEmAnalise();

        assertEquals(StatusProjetoEnum.ANALISE_REALIZADA.toString(), projeto.getProximoStatusPossivel().toString());
    }

}
