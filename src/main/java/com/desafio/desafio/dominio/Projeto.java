package com.desafio.desafio.dominio;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import org.springframework.format.annotation.DateTimeFormat;

import com.desafio.desafio.DesafioException;

import lombok.Data;

@Entity
@Data
public class Projeto implements Serializable{
        
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;

    @Column(name="data_inicio")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "idgerente")
    private Pessoa gerente;

    @Transient
    private Integer gerenteId;
    
    @Column(name="data_previsao_fim")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date previsaoTermino;
    
    @Column(name="data_fim")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataRealTermino;
    
    @Digits(message="Number should contain 10 digits.", fraction = 2, integer = 10)
    private BigDecimal orcamento;
    
    private String descricao;

    @Enumerated(EnumType.STRING)
    private ClassificacaoProjetoEnum risco;

    @Enumerated(EnumType.STRING)
    private StatusProjetoEnum status;
   
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "membros", 
    joinColumns= {@JoinColumn(name="idprojeto")}, 
    inverseJoinColumns={@JoinColumn(name="idpessoa")})
    private List<Pessoa> membroLista = new ArrayList<>();

    @PostLoad
    public void init(){
        this.gerenteId = gerente.getId();
    }

    public void adicionarMembro(Pessoa membro) throws DesafioException{
        for(Pessoa m: this.membroLista){
            if(m.getId().equals(membro.getId())){
                throw new DesafioException("Membro já faz parte do projeto.");
            }
        }
        this.membroLista.add(membro);
    }

    public StatusProjetoEnum getProximoStatusPossivel(){
        StatusProjetoEnum ultimoStatusMomento = getStatus();

        if(ultimoStatusMomento.getIndice()==7) return ultimoStatusMomento;

        Integer proximoIndice = ultimoStatusMomento.getIndice() + 1;

        for(StatusProjetoEnum statusAux: StatusProjetoEnum.values()){
            if(statusAux.getIndice().equals(proximoIndice)){
                return statusAux;
            }
        }
        return null;
    }
    
    public void colocarEmAnalise(){
        this.status = StatusProjetoEnum.EM_ANALISE;
    }

    public boolean isPodeExcluir() {
        if(getStatus().equals(StatusProjetoEnum.INICIADO) ||
            getStatus().equals(StatusProjetoEnum.EM_ANDAMENTO) ||
            getStatus().equals(StatusProjetoEnum.ENCERRADO) ){
                return false;
        }
        
        return true;
    }

    public void darContinuidade() {
        StatusProjetoEnum proximoStatusPossivel = getProximoStatusPossivel();
        this.adicionarStatus(proximoStatusPossivel);
    }

    private void adicionarStatus(StatusProjetoEnum proximoStatusPossivel) {
        this.status = proximoStatusPossivel;
    }

    public void removerMembro(Pessoa membro) {
        this.getMembroLista().remove(membro);
    }
    
}
