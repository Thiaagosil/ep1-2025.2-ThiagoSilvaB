package services;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import models.pessoa.medico.Especialidade;
import models.pessoa.medico.Medico;

public class MedicoService {
    private static final String ARQUIVO_MEDICOS = "data/medicos.csv";

    public boolean cadastrarMedico(Medico medico){
       
       //inserção de dados
        if(medico == null || medico.getNome() == null || medico.getNome().trim().isEmpty() ||
           medico.getCpf() == null ||  medico.getCpf().trim().isEmpty() || medico.getCRM() <= 0) 
        {
            System.out.println("Erro! Dados inseridos inválidos!");
            return false;
        }

        
        
        //captando as especialidades

        String especialidadesStr = "";

        for(Especialidade esp : medico.getEspecialidades()){
            especialidadesStr += esp.name() + ";" ;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_MEDICOS, true))) {
            writer.println(medico.getNome() + "," + medico.getCpf() + "," + medico.getCRM() + "," + medico.getCustoConsulta() + "," + especialidadesStr);
            System.out.println("Médico " + medico.getNome() + " cadastrado com sucesso!");
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao salvar médico no arquivo: " + e.getMessage());
            return false;
        }



    }

    Medico buscarMedicoPorCpf(String cpfMedico) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
