package repository; 

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.PlanosSaude.PlanoDeSaude;
import models.pessoa.paciente.Paciente;
import models.pessoa.paciente.PacienteEspecial;
import services.PlanoSaudeService; 

public class PacienteRepositoryCSV {

    private static final String ARQUIVO_PACIENTES = "data/pacientes.csv";
    
    
    public void salvarPacientes(List<Paciente> pacientes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES, false))) { 
            for (Paciente paciente : pacientes) {
                if (paciente == null || paciente.getNome() == null) {
                    continue; 
                }
                
                if (paciente instanceof PacienteEspecial pe) {
                    // condicaoEspecial não pode ser null ao salvar
                    String condicao = pe.getCondicaoEspecial() != null ? pe.getCondicaoEspecial() : "nenhuma";
                    
                    writer.println(pe.getNome() + "," 
                                 + pe.getCpf() + "," 
                                 + pe.getIdade() + "," 
                                 + "ESPECIAL," 
                                 + condicao + "," 
                                 + pe.getPlanoSaude().getCodigo());
                } else {
                    writer.println(paciente.getNome() + "," 
                                 + paciente.getCpf() + "," 
                                 + paciente.getIdade() + "," 
                                 + "COMUM");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar lista de pacientes no arquivo: " + e.getMessage());
        }
    }
    
    
    public List<Paciente> carregarPacientes(PlanoSaudeService planoSaudeService) {
        List<Paciente> pacientes = new ArrayList<>();

        new java.io.File("data").mkdirs();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PACIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
                
                if (dados.length >= 4) { 
                    try {
                        String nome = dados[0].trim();
                        String cpf = dados[1].trim();
                        int idade = Integer.parseInt(dados[2].trim());
                        String tipo = dados[3].trim(); 
                        
                        // Garante que nome e cpf não são vazios, para evitar objetos Paciente sem dados
                        if (nome.isEmpty() || cpf.isEmpty()) {
                            continue;
                        }
                        
                        if (tipo.equals("ESPECIAL") && dados.length >= 6) { 
                            String condicaoEspecial = dados[4].trim(); 
                            String codigoPlano = dados[5].trim();
                            
                            // busca o Plano de Saúde pelo código para recriar o objeto PacienteEspecial
                            Optional<PlanoDeSaude> planoOpt = planoSaudeService.buscarPlanoPorCodigo(codigoPlano);

                            if (planoOpt.isPresent()) {
                                pacientes.add(new PacienteEspecial(nome, cpf, idade, condicaoEspecial, planoOpt.get()));
                            } else {
                                // se o plano não for encontrado, trata como Paciente Comum 
                                System.err.println("AVISO: Plano de Saúde " + codigoPlano + " não encontrado. Paciente " + nome + " carregado como COMUM.");
                                pacientes.add(new Paciente(nome, cpf, idade)); 
                            }
                        } else {
                            // paciente Comum
                            pacientes.add(new Paciente(nome, cpf, idade));
                        }
                    } catch (NumberFormatException e) {
                        // Linha mal formatada 
                    }
                }
            }
        } catch (IOException e) {
            // arquivo não existe ou erro de leitura. A lista é retornada vazia para iniciar.
        } 
        
        return pacientes;
    }
}