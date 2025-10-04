package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.pessoa.medico.Especialidade;
import models.pessoa.medico.Medico;

public class MedicoService {
    private static final String ARQUIVO_MEDICOS = "data/medicos.csv";

    public MedicoService() {
        new java.io.File("data").mkdirs();
    }

    public boolean cadastrarMedico(Medico medico){

        if(medico == null || medico.getNome() == null || medico.getNome().trim().isEmpty() ||
           medico.getCpf() == null || medico.getCpf().trim().isEmpty() || medico.getCRM() <= 0) 
        {
            System.out.println("Erro! Dados inseridos inválidos!");
            return false;
        }

        String especialidadesStr = "";

        for(Especialidade esp : medico.getEspecialidades()){
            especialidadesStr += esp.name() + (medico.getEspecialidades().indexOf(esp) < medico.getEspecialidades().size() - 1 ? ";" : "");
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

    public Optional<Medico> buscarMedicoPorCrm(String crmBuscado) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MEDICOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");

                if (dados.length > 2 && dados[2].trim().equals(crmBuscado)) {
                    
                    String nome = dados[0].trim();
                    String cpf = dados[1].trim();
                    int crm = Integer.parseInt(dados[2].trim());
                    double custoConsulta = Double.parseDouble(dados[3].trim());
                    
                    List<Especialidade> especialidadesLidas = new ArrayList<>();
                    Especialidade primeiraEspecialidade = null;

                    if (dados.length > 4 && !dados[4].trim().isEmpty()) {
                        String[] espArray = dados[4].split(";");
                        for (String espStr : espArray) {
                            try {
                                Especialidade esp = Especialidade.valueOf(espStr.trim());
                                especialidadesLidas.add(esp);
                                if (primeiraEspecialidade == null) {
                                    primeiraEspecialidade = esp;
                                }
                            } catch (IllegalArgumentException e) {
                                
                            }
                        }
                    }
                    
                    if (primeiraEspecialidade == null) {
                         continue; 
                    }
                    
                    Medico medico = new Medico(nome, cpf, crm, primeiraEspecialidade, custoConsulta);
                    
                    for (int i = 1; i < especialidadesLidas.size(); i++) {
                        medico.adicionarEspecialidade(especialidadesLidas.get(i));
                    }
                    
                    return Optional.of(medico);
                }
            }
        } catch (IOException e) {
            System.out.println("Aviso: Arquivo de médicos não encontrado ou erro de leitura. " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Aviso: Erro de formato ao ler CRM ou CustoConsulta no arquivo. Linha ignorada.");
        }
        
        return Optional.empty();
    }
    
    public Optional<Medico> buscarMedicoPorCpf(String cpfBuscado) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MEDICOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
              
                if (dados.length > 1 && dados[1].trim().equals(cpfBuscado)) {
                    
                    String nome = dados[0].trim();
                    String cpf = dados[1].trim();
                    int crm = Integer.parseInt(dados[2].trim());
                    double custoConsulta = Double.parseDouble(dados[3].trim());
                    
                    List<Especialidade> especialidadesLidas = new ArrayList<>();
                    Especialidade primeiraEspecialidade = null;

                    if (dados.length > 4 && !dados[4].trim().isEmpty()) {
                        String[] espArray = dados[4].split(";");
                        for (String espStr : espArray) {
                            try {
                                Especialidade esp = Especialidade.valueOf(espStr.trim());
                                especialidadesLidas.add(esp);
                                if (primeiraEspecialidade == null) {
                                    primeiraEspecialidade = esp;
                                }
                            } catch (IllegalArgumentException e) {
                                
                            }
                        }
                    }
                    
                    if (primeiraEspecialidade == null) {
                         continue; 
                    }
                    
                    Medico medico = new Medico(nome, cpf, crm, primeiraEspecialidade, custoConsulta);
                    
                    for (int i = 1; i < especialidadesLidas.size(); i++) {
                        medico.adicionarEspecialidade(especialidadesLidas.get(i));
                    }
                    
                    return Optional.of(medico);
                }
            }
        } catch (IOException e) {
            
        } catch (NumberFormatException e) {
            
        }
        
        return Optional.empty();
    }
    
    public List<Medico> listarTodos() {
        //dps
        return new ArrayList<>(); 
    }
}