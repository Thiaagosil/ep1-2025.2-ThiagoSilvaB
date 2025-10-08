package models.pessoa;

public class Pessoa {
    private String nome;
    final private String cpf;


    public Pessoa(String nome, String CPF){
        this.nome = nome;
        this.cpf = CPF;
    }

    //setters e getters

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }


    public String getCpf(){
        return cpf;
    }




}



