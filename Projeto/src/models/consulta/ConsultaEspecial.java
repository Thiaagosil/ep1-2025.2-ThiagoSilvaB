package models.consulta;

import java.time.LocalDateTime;
import models.PlanosSaude.PlanosDeSaude;
import models.pessoa.medico.Especialidade;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.PacienteEspecial;


public class ConsultaEspecial extends Consulta{
    
    private PlanosDeSaude planoAplicado;
    private double valorDesconto;
    private double custoFinal;



    public ConsultaEspecial(PacienteEspecial paciente, Medico medico, LocalDateTime dataHora, PlanosDeSaude plano){
        super(paciente, medico, dataHora);


        if (paciente.getPlanoSaude() == null) { 
            throw new IllegalArgumentException("Paciente Especial deve ter um Plano de Saúde.");
        }
        this.planoAplicado = paciente.getPlanoSaude();


        this.custoFinal = calcularCustoFinal();

    }

    private double calcularCustoFinal() {

        double custoBase = this.getMedico().getCustoConsulta();

        double percentualDescontoTotal = 0.0;
       
        if (!this.getMedico().getEspecialidades().isEmpty()) {
            Especialidade especialidadePrincipal = this.getMedico().getEspecialidades().get(0);
            
      
            PacienteEspecial pacienteEspecial = (PacienteEspecial)this.getPaciente();
            percentualDescontoTotal = pacienteEspecial.descontoPorEspecialidade(especialidadePrincipal);
        }

        //calcula o valor do desconto
        this.valorDesconto = custoBase * percentualDescontoTotal;
        
        //calcula o custo final
        return custoBase - this.valorDesconto;
    }


    

    public PlanosDeSaude getPlanoAplicado() {
        return planoAplicado;
    }

    public double getValorDesconto() {
        return valorDesconto;
    }
    
    public double getCustoFinal() {
        return custoFinal;
    }


}
