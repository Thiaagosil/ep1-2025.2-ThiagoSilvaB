package models.internacao;


import java.time.LocalDateTime;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;

public class Internacao {

    private final Paciente paciente;
    private final Medico medicoResponsavel;
    private final LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private final Quarto quarto;
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

         // quarto é marcado como ocupado ao iniciar a internação
        quarto.setEstaOcupado(true);
  }


  //getters

        public Paciente getPaciente(){
            return paciente;
        }

        
        public Medico getMedicoResponsavel(){
            return medicoResponsavel;
        }


        public LocalDateTime getDataEntrada(){
            return dataEntrada;
        }


        public LocalDateTime getDataSaida(){
            return dataSaida;
        }


        public Quarto getQuarto(){
            return quarto;
        }

        
        public double getCustoInternacao(){
            return custoInternacao;
        }


        public boolean isEstaCancelada(){
            return estaCancelada;
        }



        //setters - calculo

        public void setDataSaida(LocalDateTime dataSaida){
            this.dataSaida = dataSaida;
        }


        public void setCustoInternacao(double custoInternacao){
                this.custoInternacao = custoInternacao;
        }


        public void setEstaCancelada(boolean estaCancelada){
                this.estaCancelada = estaCancelada;
        }




}
