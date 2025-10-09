package models.internacao;

public class Quarto {

    private final int numero;
    private final double custoDiario;
    private boolean estaOcupado;


    public Quarto(int numero, double custoDiario){
        this.numero = numero;
        this.custoDiario = custoDiario;
        this.estaOcupado = false; //padrao.. quando estiver ocupado o set mudará
    }

    public int getNumero(){
        return numero;
    }

    public double getCustoDiario(){
        return custoDiario;
    }

    public boolean isEstaOcupado(){
        return estaOcupado;
    }


    public void setEstaOcupado(boolean estaOcupado){
        this.estaOcupado = estaOcupado;
    }
   
}
