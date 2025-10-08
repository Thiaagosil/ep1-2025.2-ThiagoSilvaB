package models.pessoa.paciente;

import models.PlanosSaude.PlanosDeSaude;
import models.pessoa.medico.Especialidade;

public class PacienteEspecial extends Paciente {
    
    private String condicaoEspecial;
    private PlanosDeSaude planoSaude; 

    public PacienteEspecial(String nome, String cpf, int idade, String condicaoEspecial, PlanosDeSaude planoSaude){
        super(nome, cpf, idade);
        this.condicaoEspecial = condicaoEspecial; 
        this.planoSaude = planoSaude; 
    }


    public String getCondicaoEspecial() {
        return condicaoEspecial;
    }

    public void setCondicaoEspecial(String condicaoEspecial){ 
        this.condicaoEspecial = condicaoEspecial;
    }


    // Plano de saude
    public double descontoPorEspecialidade(Especialidade especialidade){
        if (this.planoSaude == null) return 0.0; 
        
        double descontoBase = this.planoSaude.getDescontoPorEspecialidade(especialidade); 
        
        //quando o paciente tiver mais ou 60 anos
        if(super.getIdade() >= 60){ 
            double descontoSenior = 0.10; // 10%
            
            
            return descontoBase + descontoSenior; 
        }
    
        return descontoBase;
    }


    // Getters e setters
    public PlanosDeSaude getPlanoSaude() { 
        return this.planoSaude;
    }

    public void setPlanoSaude(PlanosDeSaude planoSaude) { 
        this.planoSaude = planoSaude;
    }
}