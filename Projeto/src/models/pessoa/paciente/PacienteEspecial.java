package models.pessoa.paciente;

import models.PlanosSaude.PlanosDeSaude;
import models.pessoa.medico.Especialidade;

public class PacienteEspecial extends Paciente {
    
    private String condicaoEspecial;
    private PlanosDeSaude planosDeSaude;

    public PacienteEspecial(String nome, String cpf, int idade, String CondicaoEspecial, PlanosDeSaude planosDeSaude){
        super(nome, cpf, idade);
        this.condicaoEspecial = CondicaoEspecial;
        this.planosDeSaude = planosDeSaude;

    }


    public String getCondicaoEspecial() {
         return condicaoEspecial;
    }

    public void setCondicaoEspecial(String CondicaoEspecial){
        this.condicaoEspecial = CondicaoEspecial;
    }


    //plano de saude

    public double descontoPorEspecialidade(Especialidade especialidade){
        if (this.planosDeSaude == null) return 0.0;
        
        double descontoBase = this.planosDeSaude.getDescontoPorEspecialidade(especialidade);
        
        //quando o paciente tiver mais ou 60 anos
        if(super.getIdade() >= 60){ 
            double descontoSenior = 0.10;

            return descontoBase + descontoSenior; 
        }
    
        return descontoBase;
    }



    //Getters e setters
         public PlanosDeSaude getPlanoSaude() {
        return this.planosDeSaude;
    }

    public void setPlanoSaude(PlanosDeSaude planosDeSaude) { 
        this.planosDeSaude = planosDeSaude;
    }
}

