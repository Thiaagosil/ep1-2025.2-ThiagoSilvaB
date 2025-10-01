package services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.internacao.Quarto;

public class QuartoService {
    
    private final List<Quarto> quartosEmMemoria = new ArrayList<>();

    public QuartoService(){
        quartosEmMemoria.add(new Quarto(01, 200));
        quartosEmMemoria.add(new Quarto(02, 250));
        quartosEmMemoria.add(new Quarto(03, 300));
    }
    


    //buscará o quarto pelo número 
    public Optional<Quarto> buscarQuartoPorNumero(int numero){
        return quartosEmMemoria.stream()
            .filter(q -> q.getNumero() == numero)
                .findFirst();
    }


    //listar quartos livres
    public List<Quarto> listarQuartosLivres(){
        return quartosEmMemoria.stream()
             .filter(q -> !q.isEstaOcupado())
                .toList();
    } 


    //classificará o quarto como ocupado
    public boolean marcarComoOcupado(Quarto quarto){
        if(!quarto.isEstaOcupado()){
            quarto.setEstaOcupado(true);
            return true;  
        }
        return false; // quarto ja estava ocupado
    }


    //classificará quarto como livre
    public void marcarComoLivre(Quarto quarto){
        quarto.setEstaOcupado(false);
    }





}
