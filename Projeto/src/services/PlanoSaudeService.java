package services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import models.PlanosSaude.PlanosDeSaude; 

public class PlanoSaudeService {

    private final List<PlanosDeSaude> planos;

    public PlanoSaudeService() {
        this.planos = new ArrayList<>();
    }
    
    //novo plano de saúde no sistema
    public PlanosDeSaude cadastrarPlano(PlanosDeSaude plano) {
        if (buscarPlanoPorCodigo(plano.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Erro! Já existe um plano de saúde cadastrado com o código: " + plano.getCodigo());
        }
        planos.add(plano);
        return plano;
    }
    
     //busca de plano de saude
    public Optional<PlanosDeSaude> buscarPlanoPorCodigo(String codigo) {
        return planos.stream()
                .filter(p -> p.getCodigo().equals(codigo))
                .findFirst();
    }
    
    //retorna uma lista de planos
    public List<PlanosDeSaude> listarPlanos() {
        return Collections.unmodifiableList(planos);
    }
}