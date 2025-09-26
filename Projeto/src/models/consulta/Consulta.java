package models.consulta;

import java.time.LocalDateTime;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;

public class Consulta {

private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private ConsultaStatus status;
    private String diagnostico;
    private String prescricao;

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora){
        this.paciente = paciente;
        this.medico = medico;
        this.dataHora = dataHora;
        this.status = ConsultaStatus.AGENDADA;
    }

    //getters e setters 

    public Paciente getPaciente(){
        return paciente;
    }

    public Medico getMedico(){
        return medico;
    }

    public LocalDateTime getDataHora(){
        return dataHora;
    }

    public ConsultaStatus getStatus(){
        return status;
    }

    public void setStatus(ConsultaStatus status){
        this.status = status;
    }


    public String getDiagnostico(){
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico){
        this.diagnostico = diagnostico;
    }

    public String getPrescricao(){
        return prescricao;
    }
    
    public void setPrescricao(String prescricao){
        this.prescricao = prescricao;
    }





}
