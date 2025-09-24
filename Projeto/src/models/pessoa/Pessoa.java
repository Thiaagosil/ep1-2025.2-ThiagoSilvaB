package models.pessoa;

public class Pessoa {
    private String nome;
    private String CPF;


    public Pessoa(String nome, String CPF){
        this.nome = nome;
        this.CPF = CPF;
    }

    //setters e getters

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setCpf(String CPF){
        this.CPF = CPF;
    }

    public String getCpf(){
        return CPF;
    }




}



