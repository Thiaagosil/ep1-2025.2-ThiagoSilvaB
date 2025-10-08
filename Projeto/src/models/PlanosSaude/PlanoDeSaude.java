package models.PlanosSaude;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import models.pessoa.medico.Especialidade;

public class PlanoDeSaude {
    
    private final String nome;
    private final String codigo;
    
    //vai mapear as especialidade para definir o percentual de desconto
    private final Map<Especialidade, Double> descontoPorEspecialidade;

    public PlanoDeSaude(String nome, String codigo){ 
    this.nome = nome;
    this.codigo = codigo;
    this.descontoPorEspecialidade = new HashMap<>();
}

public void adicionarDesconto(Especialidade especialidade, double percentualDesconto){

    //desconto entre 0% e 100%
    if(percentualDesconto < 0.0 || percentualDesconto > 1.0){
        throw new IllegalArgumentException("Erro! Percentual de desconto deve estar entre 0.0 (0%) e 1.0 (100%).");
    }
    this.descontoPorEspecialidade.put(especialidade, percentualDesconto);
}

    public double getDescontoPorEspecialidade(Especialidade especialidade){
            return this.descontoPorEspecialidade.getOrDefault(especialidade,0.0);
    }
    
     public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public double calcularCustoInternacao(double custoBase, LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        return custoBase; 
}


}
