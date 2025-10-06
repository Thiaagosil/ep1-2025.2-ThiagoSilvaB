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
            //salvando: Nome, CPF, CRM, CustoConsulta, Especialidades
            writer.println(medico.getNome() + "," + medico.getCpf() + "," + medico.getCRM() + "," + medico.getCustoConsulta() + "," + especialidadesStr);
            System.out.println("Médico " + medico.getNome() + " cadastrado com sucesso!");
            return true;
        } catch (IOException e) {
            System.out.println("Erro ao salvar médico no arquivo: " + e.getMessage());
            return false;
        }
    }
    
    // NOVO: Método para ler todos os médicos do arquivo CSV
    public List<Medico> listarTodosMedicos() {
        List<Medico> medicos = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_MEDICOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
                
                // Estrutura do CSV: Nome[0], CPF[1], CRM[2], Custo[3], Especialidades[4]
                if (dados.length >= 5) {
                    try {
                        String nome = dados[0].trim();
                        String cpf = dados[1].trim();
                        int crm = Integer.parseInt(dados[2].trim());
                        double custoConsulta = Double.parseDouble(dados[3].trim());
                        
                        List<Especialidade> especialidadesLidas = new ArrayList<>();
                        Especialidade primeiraEspecialidade = null;
                        
                        // Processa especialidades
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
                                    // Ignora especialidades inválidas
                                }
                            }
                        }
                        
                        if (primeiraEspecialidade != null) {
                            Medico medico = new Medico(nome, cpf, crm, primeiraEspecialidade, custoConsulta);
                            // Adiciona as especialidades secundárias
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
            // Se o arquivo não existe, retorna lista vazia (comportamento esperado)
        } 
        
        return medicos;
    }
    
    //método para buscar por CRM
    public Optional<Medico> buscarMedicoPorCrm(String crmBuscado) {

        try {
            int crm = Integer.parseInt(crmBuscado.trim());
            return listarTodosMedicos().stream()
                       .filter(m -> m.getCRM() == crm)
                       .findFirst();
                       
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    //método para buscar por CPF
    public Optional<Medico> buscarMedicoPorCpf(String cpfBuscado) {
        String cpf = cpfBuscado.trim();
        return listarTodosMedicos().stream()
                   .filter(m -> m.getCpf().equals(cpf))
                   .findFirst();
    }


    //método para filtrar por especialidade.
    public List<Medico> listarMedicosPorEspecialidade(String especialidade) {
     
        List<Medico> medicosFiltrados = new ArrayList<>();
     
        String especialidadeBusca = especialidade.trim().toUpperCase(); 
        
        for (Medico medico : listarTodosMedicos()) { 
            boolean temEspecialidade = medico.getEspecialidades().stream()
                                            .anyMatch(e -> e.name().equals(especialidadeBusca));
            
            if (temEspecialidade) {
                medicosFiltrados.add(medico);
            }
        }
        
        return medicosFiltrados;
    }
}