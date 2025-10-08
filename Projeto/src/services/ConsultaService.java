package services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.consulta.Consulta;
import models.consulta.ConsultaEspecial; 
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;
import models.pessoa.paciente.PacienteEspecial; 
import repository.ConsultaRepositoryCSV; 

public class ConsultaService {

    private final List<Consulta> todasConsultas; 
    
   
    private final ConsultaRepositoryCSV repository; 

    public ConsultaService(PacienteService pacienteService, MedicoService medicoService, ConsultaRepositoryCSV repository) {
      
        this.repository = repository; 
        
        //repositório usa os serviços para carregar os dados iniciais
        this.todasConsultas = repository.carregarConsultas(pacienteService, medicoService); 
    }

    private void verificarConflito(Medico medico, LocalDateTime dataHora) {
        boolean temConflito = medico.getAgendaConsultas().stream()
            .anyMatch(c -> c.getDataHora().isEqual(dataHora)); 
        
        if (temConflito) {
            throw new IllegalArgumentException("O médico " + medico.getNome() + " já possui uma consulta agendada para este horário: " + dataHora);
        }
    }

    public Consulta agendarConsulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        
        verificarConflito(medico, dataHora); 
        
        Consulta novaConsulta = new Consulta(paciente, medico, dataHora); 

        this.todasConsultas.add(novaConsulta);
        medico.adicionarConsulta(novaConsulta);
        paciente.adicionarConsulta(novaConsulta); 
        
        repository.salvarConsultas(this.todasConsultas); 
        
        return novaConsulta;
    }
    
    
    public ConsultaEspecial agendarConsultaEspecial(PacienteEspecial paciente, Medico medico, LocalDateTime dataHora) {
        
        verificarConflito(medico, dataHora);
        
        ConsultaEspecial novaConsultaEspecial = new ConsultaEspecial(paciente, medico, dataHora, paciente.getPlanoSaude());

        this.todasConsultas.add(novaConsultaEspecial);
        medico.adicionarConsulta(novaConsultaEspecial);
        paciente.adicionarConsulta(novaConsultaEspecial); 
        
        repository.salvarConsultas(this.todasConsultas); 
        
        return novaConsultaEspecial;
    }
 
    public Consulta finalizarConsulta(String cpfPaciente, LocalDateTime dataHora, String diagnostico, String prescricao) {
    
        Optional<Consulta> consultaOpt = this.todasConsultas.stream()
            .filter(c -> c.getPaciente().getCpf().equals(cpfPaciente))
            .filter(c -> c.getDataHora().isEqual(dataHora))
            .findFirst();

        if (consultaOpt.isEmpty()) {
            throw new IllegalArgumentException("Consulta não encontrada para o paciente e horário informados.");
        }
        
        Consulta consulta = consultaOpt.get();

        if (consulta.getStatus() != models.consulta.ConsultaStatus.AGENDADA) {
            throw new IllegalArgumentException("A consulta já foi finalizada ou cancelada (Status: " + consulta.getStatus() + ").");
        }

        consulta.setDiagnostico(diagnostico);
        consulta.setPrescricao(prescricao);
        consulta.setStatus(models.consulta.ConsultaStatus.CONCLUIDA); 
        
        repository.salvarConsultas(this.todasConsultas); 
        
        return consulta;
    }

    public Consulta cancelarConsulta(String cpfPaciente, LocalDateTime dataHora) {
    
        Optional<Consulta> consultaOpt = this.todasConsultas.stream()
            .filter(c -> c.getPaciente().getCpf().equals(cpfPaciente))
            .filter(c -> c.getDataHora().isEqual(dataHora))
            .findFirst();

        if (consultaOpt.isEmpty()) {
            throw new IllegalArgumentException("Consulta não encontrada para o paciente e horário informados.");
        }
        
        Consulta consulta = consultaOpt.get();

        if (consulta.getStatus() != models.consulta.ConsultaStatus.AGENDADA) {
            throw new IllegalArgumentException("A consulta não pode ser cancelada. Status atual: " + consulta.getStatus());
        }

        consulta.setStatus(models.consulta.ConsultaStatus.CANCELADA); 
        
        repository.salvarConsultas(this.todasConsultas); 
        
        return consulta;
    }


    public List<Consulta> listarTodasConsultas() {
        return new ArrayList<>(this.todasConsultas);
    }
}