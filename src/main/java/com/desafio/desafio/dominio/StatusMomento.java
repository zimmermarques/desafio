package com.desafio.desafio.dominio;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
public class StatusMomento implements Serializable{
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.TIMESTAMP)
    private Date data;

    @Enumerated(EnumType.STRING)
    private StatusProjetoEnum status;

    public StatusMomento(Date data, StatusProjetoEnum status) {
        this.data = data;
        this.status = status;
    }

    public StatusMomento() {
    }
    

}
