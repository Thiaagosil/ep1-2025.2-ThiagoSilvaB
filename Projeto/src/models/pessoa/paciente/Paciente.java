package models.pessoa.paciente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import models.consulta.Consulta;
import models.internacao.Internacao;
import models.pessoa.Pessoa;

public class Paciente extends Pessoa {
  
    private int idade;
    private List<Consulta> historicoConsultas;
    private List<Internacao> historicoInternacoes;
    private Internacao internacaoAtual;

    public Paciente(String nome, String cpf, int idade) {
        super(nome, cpf);
        this.idade = idade;
        this.historicoConsultas = new ArrayList<>();
        this.historicoInternacoes = new ArrayList<>();
         this.internacaoAtual = null;
    }
    
    // Getters e Setters

   

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

    public Optional<Internacao> getInternacaoAtual() {
        return Optional.ofNullable(internacaoAtual);
    }

     public void setInternacaoAtual(Internacao internacaoAtual) {
        this.internacaoAtual = internacaoAtual;
    }

}