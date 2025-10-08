package models.pessoa.medico;

import java.util.ArrayList;
import java.util.List;
import models.consulta.Consulta; 
import models.pessoa.Pessoa;


public class Medico extends Pessoa {
    private int CRM;
    private List<Especialidade> especialidades;
    private double custoConsulta; 
    
    private final List<Consulta> agendaConsultas; 


    public Medico(String nome, String cpf, int CRM, Especialidade especialidade, double custoConsulta) {
        
        super(nome, cpf);
        this.CRM = CRM;

        this.especialidades = new ArrayList<>();
        this.especialidades.add(especialidade);

        this.custoConsulta = custoConsulta;
        
        // inicializa a lista de consultas
        this.agendaConsultas = new ArrayList<>();

    }
    
    // adiciona Especialidade
    public void adicionarEspecialidade(Especialidade especialidade){
        this.especialidades.add(especialidade);
    }

    // adiciona consulta na agenda.
    public void adicionarConsulta(Consulta consulta){
        this.agendaConsultas.add(consulta);
    }


    // Getters e Setters

    public int getCRM() {
        return CRM;
    }

    public void setCRM(int CRM) {
        this.CRM = CRM;
    }

    public double getCustoConsulta(){
        return custoConsulta;
    }

    public void setCustoConsulta(double custoConsulta){
        this.custoConsulta = custoConsulta;
    }


    // Retorna cópia da lista de especialidades
    public List<Especialidade> getEspecialidades() {
        return new ArrayList<>(this.especialidades);
    }
    
    public List<Consulta> getAgendaConsultas() {
        return new ArrayList<>(this.agendaConsultas);
    }

    public void setEspecialidades(List<Especialidade> especialidades) {
        this.especialidades = especialidades;
    }
}