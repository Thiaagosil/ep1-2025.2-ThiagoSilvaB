package models.paciente;

import java.util.ArrayList;
import java.util.List;
import models.consulta.Consulta;
import models.internacao.Internacao;

public class Paciente {
    private String nome;
    private String cpf;
    private int idade;
    private List<Consulta> historicoConsultas;
    private List<Internacao> historicoInternacoes;

    public Paciente(String nome, String cpf, int idade) {
        this.nome = nome;
        this.cpf = cpf;
        this.idade = idade;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
    }
    
    // Getters e Setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }
    
    // Arrays para o histórico, com encapsulamento de listas

    // retorna uma cópia da lista 
    public List<Consulta> getHistoricoConsultas() {
        return new ArrayList<>(this.historicoConsultas);
    }

    public void adicionarConsulta(Consulta consulta) {
        this.historicoConsultas.add(consulta);
    }

    public List<Internacao> getHistoricoInternacoes() {
        return new ArrayList<>(this.historicoInternacoes);
    }

    public void adicionarInternacao(Internacao internacao) {
        this.historicoInternacoes.add(internacao);
    }
}