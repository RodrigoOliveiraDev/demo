package com.tarefas.demo.application.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "tarefa")
public class Tarefa{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "codigo_interno")
    private Long codInterno;
    private String nome;
    private String descricao;
    @Column(name = "data_tarefa")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dataTarefa;
    private int prioridade;
    @Column(name = "concluido",nullable = false)
    private Boolean concluido;

    private Integer ordem;

    public Long getCodInterno() {
        return codInterno;
    }

    public void setCodInterno(Long codInterno) {
        this.codInterno = codInterno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataTarefa() {
        return dataTarefa;
    }

    public void setDataTarefa(Date dataTarefa) {
        this.dataTarefa = dataTarefa;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public String getFormatDataTarefa(){
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(dataTarefa);
    }

}
