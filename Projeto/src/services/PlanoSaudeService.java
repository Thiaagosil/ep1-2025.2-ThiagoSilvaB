package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.PlanosSaude.PlanoDeSaude;
import models.pessoa.medico.Especialidade;
import repository.PlanoSaudeRepositoryCSV;

public class PlanoSaudeService {
    
    private final List<PlanoDeSaude> planos; 
    private final PlanoSaudeRepositoryCSV repository; 

    public PlanoSaudeService(PlanoSaudeRepositoryCSV repository) {
        new java.io.File("data").mkdirs();
        this.repository = repository;
        //carrega a lista do repositório
        this.planos = repository.carregarPlanos();
    }
    

    // método para cadastrar Planos (Comum ou Especial)
    public void cadastrarPlano(PlanoDeSaude novoPlano) {
        if (this.planos.stream().anyMatch(p -> p.getCodigo().equals(novoPlano.getCodigo()))) {
            throw new IllegalArgumentException("Erro! Já existe um plano com o código: " + novoPlano.getCodigo());
        }
        this.planos.add(novoPlano);
        // PERSISTÊNCIA: Salva a lista completa através do Repositório
        repository.salvarPlanos(this.planos);
    }
    
    // método para buscar plano por código (LÊ DA MEMÓRIA)
    public Optional<PlanoDeSaude> buscarPlanoPorCodigo(String codigo) {
        return this.planos.stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst();
    }
    
    // método para listar todos os planos (LÊ DA MEMÓRIA)
    public List<PlanoDeSaude> listarPlanos() {
        return new ArrayList<>(this.planos);
    }
    
    // metodo para configurar Desconto por Especialidade 
    public void configurarDesconto(String codigoPlano, Especialidade especialidade, double desconto) {
        Optional<PlanoDeSaude> planoOpt = buscarPlanoPorCodigo(codigoPlano);

        if (planoOpt.isEmpty()) {
            throw new IllegalArgumentException("Plano de Saúde não encontrado para o código: " + codigoPlano);
        }

        PlanoDeSaude plano = planoOpt.get();
        plano.adicionarDesconto(especialidade, desconto); 

        //salva a lista completa através do Repositório
        repository.salvarPlanos(this.planos); 
    }
}