package models.pessoa.medico;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import models.pessoa.Pessoa;


public class Medico extends Pessoa {
    private int CRM;
    private List<Especialidade> especialidades;
    private double CustoConsulta;
    private List<LocalDateTime> agenda;


    public Medico(String nome, String cpf, int CRM, Especialidade especialidade, double CustoConsulta) {
        
        super(nome, cpf);
        this.CRM = CRM;

        this.especialidades = new ArrayList<>();
        this.especialidades.add(especialidade);

        this.CustoConsulta = CustoConsulta;
        this.agenda = new ArrayList<>();

    }
    
    //Adicionar Especialidade
    public void adicionarEspecialidade(Especialidade especialidade){
        this.especialidades.add(especialidade);
    }



    // Getters e Setters


    public int getCRM() {
        return CRM;
    }

    public void setCRM(int CRM) {
        this.CRM = CRM;
    }

    public double getCustoConsulta(){
        return CustoConsulta;
    }

    public void setCustoConsulta(double CustoConsulta){
        this.CustoConsulta = CustoConsulta;
    }


    // cópia da lista de especialidades
    public List<Especialidade> getEspecialidades() {
        return new ArrayList<>(this.especialidades);
    }

}