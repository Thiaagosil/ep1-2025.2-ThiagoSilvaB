package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.consulta.Consulta;
import models.consulta.ConsultaEspecial; 
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;
import models.pessoa.paciente.PacienteEspecial; 

public class ConsultaService {

    private final List<Consulta> todasConsultas; 
    
    private final PacienteService pacienteService;
    private final MedicoService medicoService;

    public ConsultaService(PacienteService pacienteService, MedicoService medicoService) {
        this.todasConsultas = new ArrayList<>();
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
    }
    

    private void verificarConflito(Medico medico, LocalDateTime dataHora) {
        //verificar disponibilidade
        boolean temConflito = medico.getAgendaConsultas().stream()
            .anyMatch(c -> c.getDataHora().isEqual(dataHora)); 
        
        if (temConflito) {
            throw new IllegalArgumentException("O médico " + medico.getNome() + " já possui uma consulta agendada para este horário: " + dataHora);
                                               
        }
    }



    //agendamento para Pacientes Comuns
    public Consulta agendarConsulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        
        verificarConflito(medico, dataHora); // verificar disp
        
        //criar Consulta Comum
        Consulta novaConsulta = new Consulta(paciente, medico, dataHora); 

        this.todasConsultas.add(novaConsulta);
        medico.adicionarConsulta(novaConsulta);
        paciente.adicionarConsulta(novaConsulta);
        
        return novaConsulta;
    }
    
    
    //agendamento para Pacientes Especiais, usando a lógica de desconto.
     
    public ConsultaEspecial agendarConsultaEspecial(PacienteEspecial paciente, Medico medico, LocalDateTime dataHora) {
        
        verificarConflito(medico, dataHora); //verificar Conflito
        
        
        ConsultaEspecial novaConsultaEspecial = new ConsultaEspecial(paciente, medico, dataHora, paciente.getPlanoSaude());

        // consultaEspecial (que herda de Consulta) nas listas
        this.todasConsultas.add(novaConsultaEspecial);
        medico.adicionarConsulta(novaConsultaEspecial);
        paciente.adicionarConsulta(novaConsultaEspecial);
        
        return novaConsultaEspecial;
    }
 
    



    //finalização da consulta 
    public Consulta finalizarConsulta(String cpfPaciente, LocalDateTime dataHora, String diagnostico, String prescricao) {
    
    //buscar a consulta
    java.util.Optional<Consulta> consultaOpt = this.todasConsultas.stream()
        .filter(c -> c.getPaciente().getCpf().equals(cpfPaciente))
        .filter(c -> c.getDataHora().isEqual(dataHora))
        .findFirst();

    if (consultaOpt.isEmpty()) {
        throw new IllegalArgumentException("Consulta não encontrada para o paciente e horário informados.");
    }
    
    Consulta consulta = consultaOpt.get();


    //verifica o status
    if (consulta.getStatus() != models.consulta.ConsultaStatus.AGENDADA) {
        throw new IllegalArgumentException("A consulta já foi finalizada ou cancelada (Status: " + consulta.getStatus() + ").");
    }

    //atualiza a prescricao e o diagnostico
    consulta.setDiagnostico(diagnostico);
    consulta.setPrescricao(prescricao);
    
    //usando o enum 'Concluida'
    consulta.setStatus(models.consulta.ConsultaStatus.CONCLUIDA); 
    
    return consulta;
}







    //cancelar consulta
    public Consulta cancelarConsulta(String cpfPaciente, LocalDateTime dataHora) {
    
    //buscar consulta
    java.util.Optional<Consulta> consultaOpt = this.todasConsultas.stream()
        .filter(c -> c.getPaciente().getCpf().equals(cpfPaciente))
        .filter(c -> c.getDataHora().isEqual(dataHora))
        .findFirst();

    if (consultaOpt.isEmpty()) {
        throw new IllegalArgumentException("Consulta não encontrada para o paciente e horário informados.");
    }
    
    Consulta consulta = consultaOpt.get();

    //só podemos cancelar caso a consulta esteja AGENDADA.
    if (consulta.getStatus() != models.consulta.ConsultaStatus.AGENDADA) {
        throw new IllegalArgumentException("A consulta não pode ser cancelada. Status atual: " + consulta.getStatus());
    }

    // att e cancela
    consulta.setStatus(models.consulta.ConsultaStatus.CANCELADA); 
    
    return consulta;
}


    //retorna todas as consultas do sistema
    public List<Consulta> listarTodasConsultas() {
        return new ArrayList<>(this.todasConsultas);
    }

}