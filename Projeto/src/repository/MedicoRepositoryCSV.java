package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import models.pessoa.medico.Especialidade;
import models.pessoa.medico.Medico;

public class MedicoRepositoryCSV {
    
    private static final String ARQUIVO_MEDICOS = "data/medicos.csv";

    public void salvarMedicos(List<Medico> medicos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_MEDICOS, false))) { // Usa 'false' para sobrescrever tudo
            for (Medico medico : medicos) {

                String especialidadesStr = "";
                for(Especialidade esp : medico.getEspecialidades()){
                    especialidadesStr += esp.name() + (medico.getEspecialidades().indexOf(esp) < medico.getEspecialidades().size() - 1 ? ";" : "");
                }

                writer.println(medico.getNome() + "," 
                             + medico.getCpf() + "," 
                             + medico.getCRM() + "," 
                             + medico.getCustoConsulta() + "," 
                             + especialidadesStr);
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar lista de médicos no arquivo: " + e.getMessage());
        }
    }
    
    // MÉTODO PARA CARREGAR TODOS OS MÉDICOS 
    public List<Medico> carregarMedicos() {
        List<Medico> medicos = new ArrayList<>();
        new java.io.File("data").mkdirs();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MEDICOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
                
                //Nome[0], CPF[1], CRM[2], Custo[3], Especialidades[4]
                if (dados.length >= 5) {
                    try {
                        String nome = dados[0].trim();
                        String cpf = dados[1].trim();
                        int crm = Integer.parseInt(dados[2].trim());
                        double custoConsulta = Double.parseDouble(dados[3].trim());
                        
                        List<Especialidade> especialidadesLidas = new ArrayList<>();
                        Especialidade primeiraEspecialidade = null;
                        
                        //processa especialidades
                        if (!dados[4].trim().isEmpty()) {
                            String[] espArray = dados[4].split(";");
                            for (String espStr : espArray) {
                                try {
                                    Especialidade esp = Especialidade.valueOf(espStr.trim());
                                    especialidadesLidas.add(esp);
                                    if (primeiraEspecialidade == null) {
                                        primeiraEspecialidade = esp;
                                    }
                                } catch (IllegalArgumentException e) {
                                    //ignora especialidades inválidas
                                }
                            }
                        }
                        
                        if (primeiraEspecialidade != null) {
                            Medico medico = new Medico(nome, cpf, crm, primeiraEspecialidade, custoConsulta);
                            //adiciona as especialidades secundárias (caso tenha)
                            for (int i = 1; i < especialidadesLidas.size(); i++) {
                                medico.adicionarEspecialidade(especialidadesLidas.get(i));
                            }
                            medicos.add(medico);
                        }
                        
                    } catch (NumberFormatException e) {
                        System.err.println("Aviso: Linha inválida no arquivo de médicos (CRM ou Custo não é número): " + linha);
                    }
                }
            }
        } catch (IOException e) {
            //se o arquivo não existe, retorna lista vazia
        } 
        
        return medicos;
    }
}