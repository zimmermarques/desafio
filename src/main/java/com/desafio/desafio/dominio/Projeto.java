package com.desafio.desafio.dominio;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Projeto implements Serializable{
        
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String nome;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataInicio;

    private String gerenteResponsavel;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date previsaoTermino;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date dataRealTermino;
    
    private BigDecimal orcamentoTotal;
    
    private String descricao;

    @Enumerated(EnumType.STRING)
    private ClassificacaoProjetoEnum classificacao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "projeto_id")
    private List<StatusMomento> statusMomentoLista = new ArrayList<StatusMomento>();
   
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Membro> membroLista = new ArrayList<Membro>();

    public void adicionarMembro(Membro membro) throws Exception{
        for(Membro m: this.membroLista){
            if(m.getId().equals(membro.getId())){
                throw new Exception("Membro já faz parte do projeto.");
            }
        }
        this.membroLista.add(membro);
    }

    public StatusProjetoEnum getProximoStatusPossivel(){
        StatusMomento ultimoStatusMomento = getUltimoStatusMomento();

        if(ultimoStatusMomento.getStatus().getIndice()==7) return ultimoStatusMomento.getStatus();

        Integer proximoIndice = ultimoStatusMomento.getStatus().getIndice() + 1;

        for(StatusProjetoEnum status: StatusProjetoEnum.values()){
            if(status.getIndice()==proximoIndice){
                return status;
            }
        }
        return null;
    }

    public StatusMomento getUltimoStatusMomento(){
        if(!this.getStatusMomentoLista().isEmpty()){
            return this.getStatusMomentoLista().get(this.getStatusMomentoLista().size()-1);
        }else{
            return null;
        }
    }

    public void colocar_EM_ANALISE(){
        if(existeNaListaDeStatusMomento(StatusProjetoEnum.EM_ANALISE)) return;
        statusMomentoLista.add(new StatusMomento(new Date(), StatusProjetoEnum.EM_ANALISE));
    }

    private boolean existeNaListaDeStatusMomento(StatusProjetoEnum status){
        for(StatusMomento s: this.statusMomentoLista){
            if(s.getStatus().equals(status)){
                return true;
            }
        }
        return false;
    }

    public boolean isPodeExcluir() {
        for(StatusMomento statusMomento: this.statusMomentoLista){
            if(statusMomento.getStatus().equals(StatusProjetoEnum.INICIADO) ||
                statusMomento.getStatus().equals(StatusProjetoEnum.EM_ANDAMENTO) ||
                statusMomento.getStatus().equals(StatusProjetoEnum.ENCERRADO) ){
                    return false;
            }
        }
        return true;
    }

    public void darContinuidade() {
        StatusProjetoEnum proximoStatusPossivel = getProximoStatusPossivel();
        this.adicionarStatus(proximoStatusPossivel, new Date());
    }

    private void adicionarStatus(StatusProjetoEnum proximoStatusPossivel, Date date) {
        this.statusMomentoLista.add(new StatusMomento(date, proximoStatusPossivel));
    }

    public void removerMembro(Membro membro) {
        this.getMembroLista().remove(membro);
    }
    
}
