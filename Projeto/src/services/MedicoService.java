package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.pessoa.medico.Medico;
import repository.MedicoRepositoryCSV;

public class MedicoService {
    
    
    private final MedicoRepositoryCSV repository; 
    private final List<Medico> todosMedicos; 

    
    public MedicoService(MedicoRepositoryCSV repository) {
        new java.io.File("data").mkdirs();
        this.repository = repository;

        //carrega a lista do repositório
        this.todosMedicos = repository.carregarMedicos(); 
    }

    public boolean cadastrarMedico(Medico medico){

        if(medico == null || medico.getNome() == null || medico.getNome().trim().isEmpty() ||
           medico.getCpf() == null || medico.getCpf().trim().isEmpty() || medico.getCRM() <= 0) 
        {
            System.out.println("Erro! Dados inseridos inválidos!");
            return false;
        }

        //verifica se o médico já existe na lista em memória
        if (this.todosMedicos.stream().anyMatch(m -> m.getCRM() == medico.getCRM() || m.getCpf().equals(medico.getCpf()))) {
            System.out.println("Erro! Médico com este CRM ou CPF já cadastrado.");
            return false;
        }

        this.todosMedicos.add(medico);

        //salva a lista completa através do Repositório
        repository.salvarMedicos(this.todosMedicos);
        
        System.out.println("Médico " + medico.getNome() + " cadastrado com sucesso!");
        return true;
    }
    
    // MÉTODO LISTAR
    public List<Medico> listarTodosMedicos() {
        return new ArrayList<>(this.todosMedicos);
    }
    
    // método para buscar por CRM 
    public Optional<Medico> buscarMedicoPorCrm(String crmBuscado) {

        try {
            int crm = Integer.parseInt(crmBuscado.trim());
            return this.todosMedicos.stream()
                           .filter(m -> m.getCRM() == crm)
                           .findFirst();
                           
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
    
    // método para buscar por CPF 
    public Optional<Medico> buscarMedicoPorCpf(String cpfBuscado) {
        String cpf = cpfBuscado.trim();
        return this.todosMedicos.stream()
                       .filter(m -> m.getCpf().equals(cpf))
                       .findFirst();
    }


    // método para filtrar por especialidade 
    public List<Medico> listarMedicosPorEspecialidade(String especialidade) {
     
        List<Medico> medicosFiltrados = new ArrayList<>();
     
        String especialidadeBusca = especialidade.trim().toUpperCase(); 
        
        for (Medico medico : this.todosMedicos) { 
            boolean temEspecialidade = medico.getEspecialidades().stream()
                                             .anyMatch(e -> e.name().equals(especialidadeBusca));
            
            if (temEspecialidade) {
                medicosFiltrados.add(medico);
            }
        }
        
        return medicosFiltrados;
    }
}