package com.tarefas.demo.application.controller;

import com.tarefas.demo.application.model.Tarefa;
import com.tarefas.demo.application.model.TarefaRepository;
import lombok.*;
import org.bouncycastle.asn1.cms.CMSObjectIdentifiers;
import org.primefaces.component.api.UIColumn;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.RowEditEvent;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named(value = "tarefa")
@ViewScoped
public class TarefaController implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(TarefaController.class);
    @Autowired
    TarefaRepository tarefaRepository;

    @Getter
    @Setter
    private List<Tarefa> tarefas = new ArrayList<>();
    @Getter
    @Setter
    private Tarefa tarefa;
    @Getter
    @Setter
    private String filtroListarTarefa;

    @PostConstruct
    public List<Tarefa> listarTodos() {
        tarefas = tarefaRepository.buscaTarefas();
        tarefa = new Tarefa();
        return tarefas;
    }

    public void inserir(){
        try {
            if(validaInsert(tarefa)){
                DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("formPrinc:dataTable");
                tarefa.setOrdem(dataTable.getRowCount()+1);
                tarefa.setConcluido(false);
                tarefaRepository.save(tarefa);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso:", "Tarefa criada!"));
                resetDialog();
            }
            else{
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Inserir todos os campos!"));
            }
        }
        catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void excluir(Tarefa tarefa){
       try{
           tarefaRepository.delete(tarefa);
           tarefas = tarefaRepository.buscaTarefas();
       }catch(Exception e){
           logger.error(e.getMessage());
       }
    }

    public void editar(RowEditEvent event) {
        try {
            Tarefa tarefa = (Tarefa)event.getObject();
            tarefaRepository.save(tarefa);
        }
        catch(Exception e)
        {
            logger.error(e.getMessage());
        }
    }
    public void resetDialog(){
        tarefas = tarefaRepository.buscaTarefas();
        tarefa = new Tarefa();
    }

    public boolean validaInsert(Tarefa tarefa){
        return !strEmpty(tarefa.getNome()) && !strEmpty(tarefa.getDescricao()) && tarefa.getDataTarefa() != null;
    }

    public boolean strEmpty(String str){
        return str == null || str.isEmpty();
    }

    public void concluir(Tarefa tarefa){
        try {
            tarefaRepository.save(tarefa);
            tarefas = tarefaRepository.buscaTarefas();
        }
        catch(Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void filtrar(){
        try {
            if(filtroListarTarefa == null || filtroListarTarefa.equals("0"))
                tarefas = tarefaRepository.buscaTarefas();
            else if(filtroListarTarefa.equals("D")){
                LocalDateTime now = LocalDateTime.now();// acho que não foi a melhor solução com certeza, mas não consegui pensar muito nessa
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String dataIni = dtf.format(now)+" 00:00:00";
                String dataFim = dtf.format(now)+" 23:59:59";
                tarefas = tarefaRepository.buscaTarefaDiaAtual(dateFormat.parse(dataIni),dateFormat.parse(dataFim));
            }else{
                tarefas = tarefaRepository.buscaTarefaConcluida(filtroListarTarefa.equals("C"));//para buscar concluidos se por C
            }
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
    // feito dessa forma, não consegui achar forma mais logica, com um fluxo bem maior de tarefas
    // provavelmente travaria
    public void salvarOrdem() {
        try {
            int i = 1;//vi que isso pode dar errado mudando o filtro, já que poderia sobrescrever duas ordem, já que
                     //o tamanho da lista vai mudar a solução talvez seria colocar 1 ordem para cada filtro.
            for (Tarefa tarefa: tarefas) {
                tarefa.setOrdem(i);
                i++;
            }
            tarefaRepository.saveAll(tarefas);
            tarefas = tarefaRepository.buscaTarefas();
        }catch(Exception e){
            logger.error(e.getMessage());
        }
    }
}
