package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional; 
import models.pessoa.paciente.Paciente;

public class PacienteService {
    private static final String ARQUIVO_PACIENTES = "data/pacientes.csv";

    public PacienteService() {
        new java.io.File("data").mkdirs();
    }

    public boolean cadastrarPaciente(Paciente paciente){
        if (paciente == null){
            System.out.println("Erro! o Paciente não pode ser nulo!");
            return false;
        }
        
        if(paciente.getNome() == null || paciente.getNome().trim().isEmpty() || paciente.getCpf() == null || paciente.getCpf().trim().isEmpty()){
            System.out.println("Erro! Nome e CPF não podem ser nulos!");
            return false;
        }    

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PACIENTES, true))) {
            writer.println(paciente.getNome() + "," + paciente.getCpf() + "," + paciente.getIdade());
            System.out.println("Paciente " + paciente.getNome() + " cadastrado com sucesso!");
            
            return true;
        }
        catch (IOException e){
            System.out.println("Erro ao salvar paciente no arquivo: " + e.getMessage());
            return false;
        }
        
    }
    

    public Optional<Paciente> buscarPacientePorCpf(String cpfPaciente) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PACIENTES))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
                // nome [0], CPF[1] , idade[2]
                
                if (dados.length > 1 && dados[1].trim().equals(cpfPaciente)) {
                    
                    String nome = dados[0].trim();
                    String cpf = dados[1].trim();
                    int idade = 0;
                    
                    if (dados.length > 2) {
                        try {
                            idade = Integer.parseInt(dados[2].trim());
                        } catch (NumberFormatException e) {
                    }
                }
                    
                    return Optional.of(new Paciente(nome, cpf, idade)); 
                }
            }
        } catch (IOException e) {
            
        } 
        
        return Optional.empty();
    }
}