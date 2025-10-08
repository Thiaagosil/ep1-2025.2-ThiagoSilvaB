package models.pessoa.medico;

import java.util.ArrayList;
import java.util.List;
import models.consulta.Consulta; 
import models.pessoa.Pessoa;


public class Medico extends Pessoa {
    private int crm;
    private List<Especialidade> especialidades;
    private double CustoConsulta;
    
    private final List<Consulta> agendaConsultas; 


    public Medico(String nome, String cpf, int CRM, Especialidade especialidade, double CustoConsulta) {
        
        super(nome, cpf);
        this.crm = CRM;

        this.especialidades = new ArrayList<>();
        this.especialidades.add(especialidade);

        this.CustoConsulta = CustoConsulta;
        
        //inicializa a lista de consultas
        this.agendaConsultas = new ArrayList<>();

    }
    
    //adiciona Especialidade
    public void adicionarEspecialidade(Especialidade especialidade){
        this.especialidades.add(especialidade);
    }

        //adiciona consulta na agenda.
    public void adicionarConsulta(Consulta consulta){
        this.agendaConsultas.add(consulta);
    }


    // Getters e Setters

    public int getCRM() {
        return crm;
    }

    public void setCRM(int CRM) {
        this.crm = CRM;
    }

    public double getCustoConsulta(){
        return CustoConsulta;
    }

    public void setCustoConsulta(double CustoConsulta){
        this.CustoConsulta = CustoConsulta;
    }


    // Retorna cópia da lista de especialidades
    public List<Especialidade> getEspecialidades() {
        return new ArrayList<>(this.especialidades);
    }
    
    public List<Consulta> getAgendaConsultas() {
        return new ArrayList<>(this.agendaConsultas);
    }

}