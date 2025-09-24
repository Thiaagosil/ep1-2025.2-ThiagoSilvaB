package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import models.pessoa.paciente.Paciente;

public class PacienteService {
    private static final String ARQUIVO_PACIENTES = "data/pacientes.csv";


    /* realizando o cadastro */
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
}