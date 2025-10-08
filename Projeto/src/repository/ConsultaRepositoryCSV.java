package repository; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.consulta.Consulta;
import models.consulta.ConsultaEspecial;
import models.consulta.ConsultaStatus;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;
import models.pessoa.paciente.PacienteEspecial;
import services.MedicoService;
import services.PacienteService;

public class ConsultaRepositoryCSV {

    private static final String ARQUIVO_CONSULTAS = "data/consultas.csv";

    public void salvarConsultas(List<Consulta> todasConsultas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_CONSULTAS, false))) {
            for (Consulta c : todasConsultas) {
                String linha = c.getDataHora() + ","
                             + c.getPaciente().getCpf() + ","
                             + c.getMedico().getCRM() + ","
                             + c.getStatus();

                if (c instanceof ConsultaEspecial ce) {
                    linha += ",ESPECIAL,"
                           + (ce.getDiagnostico() != null ? ce.getDiagnostico().replace(",", ";") : "nenhuma") + ","
                           + (ce.getPrescricao() != null ? ce.getPrescricao().replace(",", ";") : "nenhuma") + ","
                           + ce.getPlanoSaude().getCodigo(); 
                } else {
                    linha += ",COMUM,"
                           + (c.getDiagnostico() != null ? c.getDiagnostico().replace(",", ";") : "nenhuma") + ","
                           + (c.getPrescricao() != null ? c.getPrescricao().replace(",", ";") : "nenhuma") + ",nenhuma";
                }
                writer.println(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar lista de consultas: " + e.getMessage());
        }
    }

    //carregar dados consultas
    public List<Consulta> carregarConsultas(PacienteService pacienteService, MedicoService medicoService) {
        List<Consulta> consultas = new ArrayList<>();
        new java.io.File("data").mkdirs(); 
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CONSULTAS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(",");
                if (dados.length >= 7) { 
                    try {
                        LocalDateTime dataHora = LocalDateTime.parse(dados[0].trim());
                        String cpfPaciente = dados[1].trim();
                        String crmMedico = dados[2].trim();
                        ConsultaStatus status = ConsultaStatus.valueOf(dados[3].trim());
                        String tipo = dados[4].trim();
                        String diagnostico = dados[5].trim().equals("nenhuma") ? null : dados[5].trim().replace(";", ",");
                        String prescricao = dados[6].trim().equals("nenhuma") ? null : dados[6].trim().replace(";", ",");

                        Optional<Paciente> pacienteOpt = pacienteService.buscarPacientePorCpf(cpfPaciente);
                        Optional<Medico> medicoOpt = medicoService.buscarMedicoPorCrm(crmMedico);

                        if (pacienteOpt.isPresent() && medicoOpt.isPresent()) {
                            Paciente paciente = pacienteOpt.get();
                            Medico medico = medicoOpt.get();
                            Consulta consulta;

                            if (tipo.equals("ESPECIAL") && paciente instanceof PacienteEspecial pe) {
                                consulta = new ConsultaEspecial(pe, medico, dataHora, pe.getPlanoSaude()); 
                            } else {
                                consulta = new Consulta(paciente, medico, dataHora);
                            }

                            consulta.setStatus(status);
                            consulta.setDiagnostico(diagnostico);
                            consulta.setPrescricao(prescricao);
                            
                            consultas.add(consulta);
                            medico.adicionarConsulta(consulta); 

                        } else {
                            System.err.println("AVISO: Paciente ou Médico não encontrado ao carregar consulta: " + linha);
                        }
                    } catch (Exception e) {
                        System.err.println("Erro ao processar linha de consulta: " + linha + " -> " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Arquivo de consultas não encontrado. Iniciando lista vazia.");
        }
        return consultas;
    }
}