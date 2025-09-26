package models.pessoa.medico;

public enum Especialidade {
   CARDIOLOGIA("Cardiologia"),
    PEDIATRIA("Pediatria"),
    ORTOPEDIA("Ortopedia"),
    CLINICO_GERAL("Clínico Geral"),
    DERMATOLOGIA("Dermatologia");


    private String descricao;

    Especialidade(String descricao){
        this.descricao = descricao;
    }

    public String getdescricao(){
        return descricao;
    }


}
