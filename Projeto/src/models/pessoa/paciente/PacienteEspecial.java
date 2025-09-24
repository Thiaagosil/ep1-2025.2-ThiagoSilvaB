package models.pessoa.paciente;

public class PacienteEspecial extends Paciente {
    
    private String CondicaoEspecial;

    public PacienteEspecial(String nome, String cpf, int idade, String CondicaoEspecial){
        super(nome, cpf, idade);
        this.CondicaoEspecial = CondicaoEspecial;
    }


    public String getCondicaoEspecia() {
            return CondicaoEspecial;
    }

    public void setCondicaoEspecial(String CondicaoEspecial){
        this.CondicaoEspecial = CondicaoEspecial;
    }



}
