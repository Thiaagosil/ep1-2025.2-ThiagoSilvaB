package models.PlanosSaude;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PlanoEspecial extends PlanoDeSaude{
    
    public PlanoEspecial(String nome, String codigo){
        super(nome, codigo);
    }


    @Override 
    public double calcularCustoInternacao(double custoBase, LocalDateTime dataEntrada, LocalDateTime dataSaida){
        
        //O chnoroUnit.days conta os dias de internações do paciente.
        long diasInternados = ChronoUnit.DAYS.between(dataEntrada.toLocalDate(), dataSaida.toLocalDate());

        //internações de menos de uma semana é gratuita, o plano cobre
        if(diasInternados < 7){
            
            System.out.println("Plano Especial aplicado: Internação de " + diasInternados + " dias é gratuita."); 
            return 0.0; //gratuito
        }
        

        return custoBase;

    } 

}
