package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.consulta.Consulta;
import models.consulta.ConsultaStatus;
import models.pessoa.medico.Medico;
import models.pessoa.paciente.Paciente;

public class ConsultaService {
    private static final String ARQUIVO_CONSULTAS = "data/consultas.csv";
    private List<Consulta> consultas;

    //Dependência, buscar em MedicoService e PacienteService
    private PacienteService  pacienteService;
    private MedicoService  medicoService;

    //construtor para injetar a dependência.
    public ConsultaService(PacienteService pacienteService, MedicoService medicoService){
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.consultas = this.carregarConsultasDoArquivos(); 

    }

    // tentar agendar uma nova consulta após verificar se a disponibilidade..
    public boolean agendarConsulta(Paciente paciente, Medico medico, LocalDateTime dataHora){
        if(paciente == null || medico == null){
            System.out.println("Erro! Médico e Paciente não podem ser nulos!");
            return false;
        }
        
         if(!this.isMedicoDisponivel(medico, dataHora)){ 
            System.out.println("Erro! o Médico " + medico.getNome()+ " não está disponível neste horário!");
            return false;
        }
        

        Consulta novaConsulta = new Consulta(paciente, medico, dataHora);

        if(salvarConsulta(novaConsulta)) {
            paciente.adicionarConsulta(novaConsulta);
            System.out.println("Consulta agendada com sucesso para ás " + dataHora);
            return true;
        }


        return false;

    }

    // Verificando a Disponibilidade do médico

   private boolean isMedicoDisponivel(Medico medico, LocalDateTime dataHora){ 
        for(Consulta c : consultas){
            // verificação se é o mesmo médico, mesmo horário..
            if(c.getMedico().getCpf().equals(medico.getCpf()) && c.getDataHora().isEqual(dataHora)) {
                return false;
            }    
        }
        return true;
    }





    private boolean salvarConsulta(Consulta consulta) {
        try(PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_CONSULTAS, true))){
            //CPF como indetificação 
            
            writer.println(
                consulta.getPaciente().getCpf() + "," + 
                consulta.getMedico().getCpf() + "," + 
                consulta.getDataHora() + "," +
                consulta.getStatus()
            );
            consultas.add(consulta);
            return true;
        }
        catch(IOException e){
            System.err.println("Erro ao salvar consulta no arquivo: " + e.getMessage());
            return false;
        }
    }


    public List<Consulta> carregarConsultasDoArquivos(){
        

        List<Consulta> listaCarregada = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_CONSULTAS))) {
            
            String linha;

            while((linha = reader.readLine()) != null){
                String[] dados = linha.split(",");


                if (dados.length == 4) {
                    String cpfPaciente = dados[0];
                    String cpfMedico = dados[1];
                    LocalDateTime dataHora = LocalDateTime.parse(dados[2]);
                    ConsultaStatus status = ConsultaStatus.valueOf(dados[3]);

                    // Buscar os Objetos pelo CPF.
                    Paciente paciente = pacienteService.buscarPacientePorCpf(cpfPaciente);
                    Medico medico = medicoService.buscarMedicoPorCpf(cpfMedico);

                    if(paciente == null || medico == null){
                        Consulta c = new Consulta(paciente, medico, dataHora);
                        c.setStatus(status);

                        listaCarregada.add(c);

                        paciente.adicionarConsulta(c);  // adicionar consulta no historico do paciente

                    }
                    
                    else 
                    {
                        System.err.println("Erro, Paciente ou Médico não encontrado ao carregar consulta.");
                   }
                }
            }
        }
     catch (IOException e) {
            System.err.println("Erro ao carregar consultas do arquivo: " + e.getMessage());
        } catch (Exception e) {
             System.err.println("Erro de formato ao carregar consultas: " + e.getMessage());
        }
        return listaCarregada;
    }

     public List<Consulta> listarTodasConsultas() {
        return new ArrayList<>(this.consultas); // Retorna uma cópia de todas as consultas
    }
}
