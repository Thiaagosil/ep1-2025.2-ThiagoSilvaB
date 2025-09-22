package models.medico;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Medico {
    private String nome;
    private int CRM;
    private List<Especialidade> especialidades;
    private double CustoConsulta;
    private List<LocalDateTime> agenda;


    public Medico(String nome, int CRM, Especialidade especialidade, double CustoConsulta) {
        this.nome = nome;
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

    //Marcar horário na agenda
    // public void adicionarHorario(LocalDateTime horario){
    //     if (horario != null && !agenda.contains(horario)) {
    //         agenda.add(horario);
    //     }
    // }




    // Getters e Setters

     public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCRM() {
        return CRM;
    }

    public void setCRM(int CRM) {
        this.CRM = CRM;
    }


    // cópia da lista de especialidades
    public List<Especialidade> getEspecialidades() {
        return new ArrayList<>(this.especialidades);
    }



}