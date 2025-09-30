package models.internacao;

import java.time.LocalDateTime;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;

public class Internacao {

    private Paciente paciente;
    private Medico medicoResponsavel;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Quarto quarto;
    private double custoInternacao;
    private boolean estaCancelada;
    
      
  public Internacao(Paciente paciente, Medico medicoResponsavel, LocalDateTime dataEntrada, LocalDateTime dataSaida, Quarto quarto, double custoInternacao){
        this.paciente = paciente;
        this.medicoResponsavel = medicoResponsavel;
        this.dataEntrada = dataEntrada;
        this.dataSaida = dataSaida;
        this.quarto = quarto;
        this.custoInternacao = custoInternacao;
        this.estaCancelada = false;

         //o quarto é marcado como ocupado ao iniciar a internação
        quarto.setEstaOcupado(true);
  }



  //getters


}
