package models.PlanosSaude;

import java.util.HashMap;
import java.util.Map;
import models.pessoa.medico.Especialidade;

public class PlanosDeSaude {
    
    private final String nome;
    private final String codigo;
    
    //vai mapear as especialidade para definir o percentual de desconto
    private final Map<Especialidade, Double> descontoPorEspecialidade;

    public PlanosDeSaude(String nome, String codigo){ 
    this.nome = nome;
    this.codigo = codigo;
    this.descontoPorEspecialidade = new HashMap<>();
}

    public void AdicionarDesconto(Especialidade especialidade, double percentualDesconto){
        
         //desconto entre 0% e 100%
        if(percentualDesconto >= 0 && percentualDesconto <= 1.0){
            this.descontoPorEspecialidade.put(especialidade, percentualDesconto);
        } else {
            System.out.println("Erro, percentual de desconto deve está entre 0.0 e 1.0");
        }
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




}
