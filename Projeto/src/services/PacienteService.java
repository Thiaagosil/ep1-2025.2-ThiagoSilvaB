package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional; 
import models.pessoa.paciente.Paciente;
//paciente especial
import models.pessoa.paciente.PacienteEspecial; 
import models.PlanosSaude.PlanosDeSaude; 

public class PacienteService {
    private static final String ARQUIVO_PACIENTES = "data/pacientes.csv";
    private final PlanoSaudeService planoSaudeService; 

    public PacienteService(PlanoSaudeService planoSaudeService) {
        new java.io.File("data").mkdirs();
        this.planoSaudeService = planoSaudeService; // Inicializa o serviço
    }

  // Em PacienteService.java

    private void salvarTodosPacientes(List<Paciente> pacientes) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES, false))) { 
            for (Paciente paciente : pacientes) {
                if (paciente instanceof PacienteEspecial) {
                    PacienteEspecial pe = (PacienteEspecial) paciente;
                    writer.println(pe.getNome() + "," + pe.getCpf() + "," + pe.getIdade() + ",ESPECIAL," + pe.getCondicaoEspecial() + "," + pe.getPlanoSaude().getCodigo());
                } else {
                    writer.println(paciente.getNome() + "," + paciente.getCpf() + "," + paciente.getIdade() + ",COMUM");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar lista de pacientes no arquivo: " + e.getMessage());
        }
    }
    
    //método para cadastrar Paciente Comum ou Especial.
    public boolean cadastrarPaciente(Paciente paciente){
        if (paciente == null || paciente.getNome() == null || paciente.getNome().trim().isEmpty() || paciente.getCpf() == null || paciente.getCpf().trim().isEmpty()){
             System.out.println("Erro! Nome e CPF não podem ser nulos!");
             return false;
        }

        List<Paciente> pacientes = listarTodosPacientes();
        if (pacientes.stream().anyMatch(p -> p.getCpf().equals(paciente.getCpf()))) {
            System.out.println("Erro! Já existe um paciente cadastrado com este CPF.");
            return false;
        }

        pacientes.add(paciente);
        salvarTodosPacientes(pacientes); 
        

        System.out.println("Paciente " + paciente.getNome() + " cadastrado com sucesso!");
        return true;
    }
    
    public boolean cadastrarPacienteEspecial(String nome, String cpf, int idade, String codigoPlano) {
        Optional<PlanosDeSaude> planoOpt = planoSaudeService.buscarPlanoPorCodigo(codigoPlano);
        
        if (planoOpt.isEmpty()) {
            throw new IllegalArgumentException("Erro! Plano de Saúde com código " + codigoPlano + " não encontrado.");
        }
        
        PacienteEspecial novoPaciente = new PacienteEspecial(nome, cpf, idade, "", planoOpt.get());
        
        //reusa o método de cadastro para salvar na lista
        return cadastrarPaciente(novoPaciente); 
    }
    
    public Optional<Paciente> buscarPacientePorCpf(String cpfPaciente) {
        return listarTodosPacientes().stream()
                .filter(p -> p.getCpf().equals(cpfPaciente))
                .findFirst();
    }


    //metodo para listar todos os pacientes (lendo e recriando objetos)
    public List<Paciente> listarTodosPacientes() {
        List<Paciente> pacientes = new ArrayList<>();
        
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
                        
                        if (tipo.equals("ESPECIAL") && dados.length >= 6) { 

                            String condicaoEspecial = dados[4].trim(); 
                            String codigoPlano = dados[5].trim();
                            
                            Optional<PlanosDeSaude> planoOpt = planoSaudeService.buscarPlanoPorCodigo(codigoPlano);

                            if (planoOpt.isPresent()) {
                                //paciente especial
                                pacientes.add(new PacienteEspecial(nome, cpf, idade, condicaoEspecial, planoOpt.get()));
                            } else {

                                pacientes.add(new Paciente(nome, cpf, idade)); 
                            }
                        } else {
                            // Paciente Comum
                            pacientes.add(new Paciente(nome, cpf, idade));
                        }
                    } catch (NumberFormatException e) {
                        //ignora linhas mal formatadas
                    }
                }
            }
        } catch (IOException e) {
            // caso haja erro, retorna lista vazia
        } 
        
        return pacientes;
    }
}