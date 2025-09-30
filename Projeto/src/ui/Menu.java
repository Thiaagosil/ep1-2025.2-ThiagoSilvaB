package ui;
import java.util.Scanner;
import models.pessoa.paciente.Paciente;
import services.ConsultaService;
import services.MedicoService;
import services.PacienteService;

public class Menu {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        PacienteService pacienteService = new PacienteService();
        MedicoService medicoService = new MedicoService();
        ConsultaService consultaService = new ConsultaService(pacienteService, medicoService);
                

        // consulta..

        
        int opcao = -1;

        while(opcao != 0){
            System.out.println("\n===========MENU PRINCIPAL===========");
            System.out.println("\nBem-vindo!");
            System.out.println("\nEscolha o serviço de sua preferencia: ");

            System.out.println("1- Cadastro de Paciente");
            System.out.println("2- Cadastro de Médico");
            System.out.println("3- Agendar Consulta");
            System.out.println("4- Gerenciar Internações");
            System.out.println("5- Cadastrar Plano de Saúde"); 
            System.out.println("6- Relatórios e Estatísticas");
             
            System.out.println("0- SAIR");
            
            
            //===========================

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
               
                case 1:
                    gerenciarPacientes(sc, pacienteService);
                    break;

                 case 2:
                    gerenciarMedicos(sc, medicoService);
                    break;

                case 3:
                    gerenciarConsultas(sc, consultaService);
                    break;

                // case 4:
                //     gerenciarPacientes(sc, pacientes);
                //     break;

                // case 5:
                //     gerenciarPacientes(sc, pacientes);
                //     break;

                // case 6:
                //     gerenciarPacientes(sc, pacientes);
                //     break;


               case 0:
                    System.out.println("Fechando o Sistema...");
                    break;

                    
                default:
                    System.out.println("Opção Inválida. Tente novamente! ");
                
                }
        }

        sc.close();
 }


    public static void gerenciarPacientes(Scanner sc, PacienteService pacienteService){

        int opcaoPaciente = -1;

        while(opcaoPaciente != 0){
            System.out.println("\n===============Tela Paciente===============");
            System.out.println("\nEscolha uma Opção :");

            System.out.println("1- Cadastrar Paciente (Comum)");
            System.out.println("2- Cadastrar Paciente (Especial)");
            System.out.println("3- Listar Pacientes Cadastrados");
            System.out.println("0- Voltar ao Menu Principal");
            
            opcaoPaciente = sc.nextInt();
            sc.nextLine();
            

            switch (opcaoPaciente){
                case 1: 
                    System.out.println("\n================= Cadastro de Paciente (Comum) =================");
                    System.out.println("\nNome do Paciente:");
                    String nome = sc.nextLine();
                    System.out.println("\nCPF: ");
                    String cpf = sc.nextLine();
                    System.out.println("\nIdade: ");
                    int idade = sc.nextInt();
                    sc.nextLine();

                Paciente novoPaciente = new Paciente(nome, cpf , idade);
                pacienteService.cadastrarPaciente(novoPaciente);
                
                break;    
            }
            
            
        }


    }

    public static void gerenciarMedicos(Scanner sc, MedicoService medicoService){
        int opcaoMedico = -1;

        while(opcaoMedico != 0){
            System.out.println("\n===============Tela Médico===============");
            System.out.println("\nEscolha uma Opção :");

            System.out.println("1- Cadastrar Médico");
            System.out.println("2- Listar Médicos Cadastrados");
           
            System.out.println("0- Voltar ao Menu Principal");
            
            opcaoMedico = sc.nextInt();
            sc.nextLine();
            
        }

    }


    public static void gerenciarConsultas(Scanner sc, ConsultaService consultaService){
        int opcaoConsulta = -1;

        while(opcaoConsulta != 0){
            System.out.println("\n===============Tela Consulta===============");
            System.out.println("\nEscolha uma Opção :");

            System.out.println("1- Marcar Consulta");
            
            System.out.println("4- Listar Consultas Cadastradas");
            System.out.println("0- Voltar ao Menu Principal");
          
        }


    }


} 