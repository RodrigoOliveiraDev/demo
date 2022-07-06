package com.tarefas.demo.application.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TarefaRepository extends JpaRepository<Tarefa, Long>{

    @Query("SELECT t FROM Tarefa t ORDER BY t.ordem")
    List<Tarefa> buscaTarefas();
    @Query("SELECT t FROM Tarefa t WHERE t.concluido = :concluido")
    List<Tarefa> buscaTarefaConcluida(@Param("concluido") boolean concluido);
    @Query("SELECT t FROM Tarefa t WHERE t.dataTarefa BETWEEN :dataIni  AND :dataFim")
    List<Tarefa> buscaTarefaDiaAtual(@Param("dataIni") Date dataIni, @Param("dataFim")Date dataFim);
//    List<Tarefa> buscaTarefaConcluida(@Param("concluido") boolean concluido);
}
