    package services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.internacao.Internacao;
import models.internacao.Quarto;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;

public class InternacaoService {


    private final QuartoService quartoService;

    private final List<Internacao> internacoesAtivas;
    private final List<Internacao> historicoInternacoes;
    
    
    public InternacaoService(QuartoService quartoService) {
        this.quartoService = quartoService;
        this.internacoesAtivas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
        
    }
    
 
    public Internacao registrarInternacao(Paciente paciente, int numeroQuarto, Medico medicoResponsavel){
        
        //buscar quarto pelo numero
        Optional<Quarto> quartoOpt = quartoService.buscarQuartoPorNumero(numeroQuarto);

        //verificar se quarto existe
        if(quartoOpt.isEmpty()){
            throw new IllegalArgumentException("Erro! Quarto número " + numeroQuarto + " não existe.");
        }
        
        
        //verificar caso o paciente ja está internado!
        if (paciente.getInternacaoAtual().isPresent()) {
             throw new IllegalStateException("Erro! O paciente " + paciente.getNome() + " já está internado.");
        }



        Quarto quarto = quartoOpt.get();

        //verificar se quarto está ocupado
        if(quarto.isEstaOcupado()){ 
            throw new IllegalStateException("Erro! Quarto " + numeroQuarto + " está ocupado no momento.");
        }
        
        //marcar quarto como ocupado
        quartoService.marcarComoOcupado(quarto);

         Internacao novaInternacao = new Internacao(
            paciente, 
            medicoResponsavel, 
            LocalDateTime.now(), 
            null, 
            quarto, 
            0.0 //custo inicial é zero!
        );

        //atualiza estado do paciente
        paciente.setInternacaoAtual(novaInternacao);

        //adiciona internacao no historico do paciente
        paciente.adicionarInternacao(novaInternacao);

        
        
        internacoesAtivas.add(novaInternacao);

        


    return novaInternacao;
        
    }
    

    public Internacao darAlta(Internacao internacao){
        
        if (internacao.getDataSaida() != null || internacao.isEstaCancelada()) {
            throw new IllegalStateException("Erro! A internação já foi finalizada ou cancelada.");
        }

        //marcar a data de saida.
        internacao.setDataSaida(LocalDateTime.now());

         
        double custoCalculado = calcularCustoTotal(internacao); 
        internacao.setCustoInternacao(custoCalculado);

        //libera o quarto
        quartoService.marcarComoLivre(internacao.getQuarto());

        //marcar o paciente como nao internado
        internacao.getPaciente().setInternacaoAtual(null);

        //mover internacao para historico
        internacoesAtivas.remove(internacao);
        historicoInternacoes.add(internacao);


        return internacao;
    }


    public double calcularCustoTotal(Internacao internacao){
        
        LocalDateTime entrada = internacao.getDataEntrada();
        LocalDateTime saida = internacao.getDataSaida();

        if(saida == null){
            return 0.0;
        }


        long duracaoHoras = ChronoUnit.HOURS.between(entrada,saida);


        final double CUSTO_POR_HORA = 10.0;
        final double CUSTO_FIXO_DIARIO = 200.0;

        long duracaoDias = ChronoUnit.DAYS.between(entrada.toLocalDate(), saida.toLocalDate());

        if (duracaoDias == 0) {
             return duracaoHoras * CUSTO_POR_HORA;
        } else {
             return (duracaoDias * CUSTO_FIXO_DIARIO) + (duracaoHoras * CUSTO_POR_HORA);
        }
    
    }

    //metodo para cancelar a internacao
    public Internacao cancelarInternacao(Internacao internacao){
            
        if(internacao.getDataSaida() != null || internacao.isEstaCancelada()){
             throw new IllegalStateException("Erro! A internação já foi finalizada ou cancelada.");
        }

        internacao.setEstaCancelada(true);
        quartoService.marcarComoLivre(internacao.getQuarto());
        internacao.getPaciente().setInternacaoAtual(null); 

          
        internacoesAtivas.remove(internacao);
        historicoInternacoes.add(internacao);

        
     
        return internacao;
    }


}