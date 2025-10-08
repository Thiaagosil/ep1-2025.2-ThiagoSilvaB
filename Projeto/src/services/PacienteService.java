package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.PlanosSaude.PlanosDeSaude;
import models.pessoa.paciente.Paciente;
import models.pessoa.paciente.PacienteEspecial;
import repository.PacienteRepositoryCSV; 

public class PacienteService {
    
    
    private final PlanoSaudeService planoSaudeService; 
    private final PacienteRepositoryCSV repository; 
    private final List<Paciente> todasPacientes;

    public PacienteService(PlanoSaudeService planoSaudeService, PacienteRepositoryCSV repository) {
        new java.io.File("data").mkdirs();
        this.planoSaudeService = planoSaudeService; 
        this.repository = repository;
        
        // CARREGA A LISTA DO REPOSITORY NA INICIALIZAÇÃO
        this.todasPacientes = repository.carregarPacientes(planoSaudeService);
    }

    
    // método para cadastrar Paciente Comum ou Especial (AGORA USA O REPOSITORY).
    public boolean cadastrarPaciente(Paciente paciente){
        if (paciente == null || paciente.getNome() == null || paciente.getNome().trim().isEmpty() || paciente.getCpf() == null || paciente.getCpf().trim().isEmpty()){
             System.out.println("Erro! Nome e CPF não podem ser nulos!");
             return false;
        }

        
        if (this.todasPacientes.stream().anyMatch(p -> p.getCpf().equals(paciente.getCpf()))) {
            System.out.println("Erro! Já existe um paciente cadastrado com este CPF.");
            return false;
        }

        this.todasPacientes.add(paciente);
        
        //CHAMA O REPOSITORY PARA SALVAR
        repository.salvarPacientes(this.todasPacientes); 
        
        System.out.println("Paciente " + paciente.getNome() + " cadastrado com sucesso!");
        return true;
    }
    
    public boolean cadastrarPacienteEspecial(String nome, String cpf, int idade, String codigoPlano) {
        Optional<PlanosDeSaude> planoOpt = planoSaudeService.buscarPlanoPorCodigo(codigoPlano);
        
        if (planoOpt.isEmpty()) {
            throw new IllegalArgumentException("Erro! Plano de Saúde com código " + codigoPlano + " não encontrado.");
        }
        
        //reusa o método de cadastro para salvar na lista
        PacienteEspecial novoPaciente = new PacienteEspecial(nome, cpf, idade, "", planoOpt.get());
        
        return cadastrarPaciente(novoPaciente); 
    }
    
    public Optional<Paciente> buscarPacientePorCpf(String cpfPaciente) {
        //busca na lista em memória 
        return this.todasPacientes.stream()
                .filter(p -> p.getCpf().equals(cpfPaciente))
                .findFirst();
    }


    // metodo para listar todos os pacientes 
    public List<Paciente> listarTodosPacientes() {
        //retorna uma cópia da lista em memória
        return new ArrayList<>(this.todasPacientes);
    }
}