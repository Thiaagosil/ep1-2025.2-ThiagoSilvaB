package ui;
import java.util.Locale;
import java.util.Optional; 
import java.util.Scanner; 
import models.internacao.Internacao;
import models.pessoa.medico.Especialidade;
import models.pessoa.medico.Medico; 
import models.pessoa.paciente.Paciente;
import services.ConsultaService;
import services.InternacaoService;
import services.MedicoService;
import services.PacienteService;
import services.QuartoService;





public class Menu {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        
        PacienteService pacienteService = new PacienteService();
        MedicoService medicoService = new MedicoService();
        ConsultaService consultaService = new ConsultaService(pacienteService, medicoService);
        
        //serviços
        QuartoService quartoService = new QuartoService();
        InternacaoService internacaoService = new InternacaoService(quartoService);

        
        int opcao = -1;

        while(opcao != 0){
            System.out.println("\n=========== MENU PRINCIPAL ===========");
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

                case 4:
                    gerenciarInternacoes(sc, internacaoService, pacienteService, medicoService, quartoService);
                    break;
                     
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



   //gerenciador de pacientes
    public static void gerenciarPacientes(Scanner sc, PacienteService pacienteService){

        int opcaoPaciente = -1;

        while(opcaoPaciente != 0){
            System.out.println("\n=============== TELA PACIENTE ===============");
            System.out.println("\nEscolha uma Opção :");

            System.out.println("1- Cadastrar Paciente (Comum)");
            System.out.println("2- Cadastrar Paciente (Especial)");
            System.out.println("3- Listar Pacientes Cadastrados");
            System.out.println("0- Voltar ao Menu Principal");
         
            try {
                opcaoPaciente = sc.nextInt();
                sc.nextLine(); 
        } catch (java.util.InputMismatchException e) {
                System.out.println("Erro! Entrada inválida. Por favor, digite um número.");
                sc.nextLine(); 
                opcaoPaciente = -1;
            continue;
        }

            switch (opcaoPaciente){
                //paciente comum
                case 1: 
                    try {
                        System.out.println("\n================= CADASTRO DE PACIENTE (Comum) =================");
                        System.out.println("\nNome do Paciente:");
                        String nome = sc.nextLine();
                        System.out.println("\nCPF: ");
                        String cpf = sc.nextLine();
                        System.out.println("\nIdade: ");
                        int idade = sc.nextInt();
                        sc.nextLine();

                        Paciente novoPaciente = new Paciente(nome, cpf , idade);
                        pacienteService.cadastrarPaciente(novoPaciente);
                        System.out.println("\n Paciente cadastrado com sucesso!");
                    } catch (Exception e) {
                        System.out.println("Erro ao cadastrar paciente: " + e.getMessage());
                        //linha extra seja consumida após erro de input
                        if (sc.hasNextLine()) {
                            sc.nextLine(); 
                        }
                    }
                
                break;  
                
                //paciente especial
                case 2:
                 System.out.println("...");
                break;

                //listagem de pacientes
                case 3:
                        System.out.println("\n==== LISTA DE PACIENTES CADASTRADOS ====");

                          if (pacienteService.listarTodosPacientes().isEmpty()) {
                            System.out.println("Nenhum paciente cadastrado!");
                    } 
                        else{
                            pacienteService.listarTodosPacientes().forEach(p -> 
                        System.out.println("Nome: " + p.getNome() + " | CPF: " + p.getCpf() + " | Idade: " + p.getIdade()));
                    }

                break;

                case 0:
                        System.out.println("Voltando...");
                break;


                default:
                if(opcaoPaciente != -1) {
                        System.out.println("Opção Inválida. Tente novamente! ");
                }
            }
            
        }

    }


    //gerenciador de medicos.
    public static void gerenciarMedicos(Scanner sc, MedicoService medicoService){
        int opcaoMedico = -1;

        while(opcaoMedico != 0){
            System.out.println("\n=============== TELA MÉDICO ===============");
            System.out.println("\nEscolha uma Opção :");

            System.out.println("1- Cadastrar Médico");
            System.out.println("2- Listar Médicos Cadastrados");
            
            System.out.println("0- Voltar ao Menu Principal");
          
            
            try {
            opcaoMedico = sc.nextInt();
            sc.nextLine();
            
        }
         catch (java.util.InputMismatchException e) {
            System.out.println("Erro! Entrada inválida. Por favor, digite um número.");
            sc.nextLine(); // Limpa o buffer de entrada
            opcaoMedico = -1;
            continue;
        }

        switch(opcaoMedico){
            case 1:
                    try{
                        System.out.println("\n================= CADASTRO DE MÉDICO =================");

                        System.out.println("Nome do Médico: ");
                        String nome = sc.nextLine();

                        System.out.println("CPF: ");
                        String cpf = sc.nextLine();

                        System.out.print("CRM (apenas números): ");
                        int crm = sc.nextInt();
                        sc.nextLine();
                        

                     Especialidade especialidadeInicial = null;
                     boolean especialidadeValida = false;
        
                    while (!especialidadeValida) {
                    System.out.println("\n--- ESPECIALIDADES DISPONÍVEIS ---");
             
                    int i = 1;
                    for (Especialidade e : Especialidade.values()) {
                      
                        System.out.println(i++ + "- " + e.getDescricao());
                    }
                    
                    System.out.print("Escolha o número da especialidade inicial: ");
                    int escolha = sc.nextInt();
                    sc.nextLine(); 


                    if (escolha > 0 && escolha <= Especialidade.values().length) {
                        especialidadeInicial = Especialidade.values()[escolha - 1];
                        especialidadeValida = true;
                    } else {
                        System.out.println("opção de especialidade inválida. Tente novamente.");
                    }
                }
                        //custo sem desconto
                        System.out.print("Custo da Consulta: R$");

                        double custoConsulta = sc.nextDouble();
                        sc.nextLine(); 


                        //cadastro e inserção no medicoservice
                        Medico novoMedico = new Medico(nome, cpf, crm, especialidadeInicial, custoConsulta);
                        medicoService.cadastrarMedico(novoMedico);

                        System.out.println("\n Médico " + nome + " cadastrado com sucesso!");


                    }

                    

                    catch(java.util.InputMismatchException e){
                    System.out.println("Erro! Entrada inválida. por favor, digite um número inteiro.");
                    sc.nextLine(); 
                    }
                    
                    catch(Exception e){
                    System.out.println("Erro ao cadastrar médico: " + e.getMessage());
                    }
            break;


            case 2:
                    System.out.println("\n==== LISTA DE MÉDICOS CADASTRADOS ==== ");
                     if (medicoService.listarTodosMedicos().isEmpty()) {
                        System.out.println("Nenhum médico cadastrado!");
                    } else {
                    medicoService.listarTodosMedicos().forEach(m -> {
            
            String especialidades = "";
            for (Especialidade e : m.getEspecialidades()) {
                especialidades += e.getDescricao() + ", "; 
            }
            
            if (!especialidades.isEmpty()) {
                especialidades = especialidades.substring(0, especialidades.length() - 2);
            }
            
            String custoFormatado = String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", m.getCustoConsulta());
            
            System.out.println("\nNome: " + m.getNome() + 
                               "  \nCPF: " + m.getCpf() + 
                               "  \nCRM: " + m.getCRM() +
                               "  \nEspecialidades: " + (especialidades.isEmpty() ? "Nenhuma" : especialidades) +
                               "  \nCusto/Consulta: " + custoFormatado);
        });
    }
            break;

            case 0:
            System.out.println("Voltando ao menu principal...");
            break;

        default:
            if(opcaoMedico != -1) {
                System.out.println("Opção Inválida. Tente novamente! ");
            }

        }
            

        }

    }


    //gerenciador de consultas
    public static void gerenciarConsultas(Scanner sc, ConsultaService consultaService){
        int opcaoConsulta = -1;

        while(opcaoConsulta != 0){
            System.out.println("\n=============== TELA CONSULTA ===============");
            System.out.println("\nEscolha uma Opção :");

            System.out.println("1- Marcar Consulta");
            
            System.out.println("4- Listar Consultas Cadastradas");
            System.out.println("0- Voltar ao Menu Principal");
            
        }

    }

    //1 - registrar nova Internação 
    
    public static void registrarNovaInternacao(Scanner sc, InternacaoService internacaoService,  PacienteService pacienteService, MedicoService medicoService, 
                                               QuartoService quartoService)
         {
        
        System.out.println("\n--- REGISTRAR NOVA INTERNAÇÃO ---");
        
        try {
            //pega o cpf do paciente
            System.out.print("Digite o CPF do paciente: ");
            String cpfPaciente = sc.nextLine();
            Optional<Paciente> pacienteOpt = pacienteService.buscarPacientePorCpf(cpfPaciente);
            
            if (pacienteOpt.isEmpty()) {
                System.out.println("Erro: Paciente com CPF " + cpfPaciente + " não encontrado.");
                return;
            }
            Paciente paciente = pacienteOpt.get();

            //pega o crm do medico
            System.out.print("Digite o CRM do médico responsável: ");
            String crmMedico = sc.nextLine();
            Optional<Medico> medicoOpt = medicoService.buscarMedicoPorCrm(crmMedico);
            
            if (medicoOpt.isEmpty()) {
                System.out.println("Erro! Médico com CRM " + crmMedico + " não encontrado.");
                return;
            }
            Medico medicoResponsavel = medicoOpt.get();

       
            System.out.println("\n--- Quartos Livres Disponíveis ---");
            quartoService.listarQuartosLivres().forEach(q -> System.out.println("Quarto " + q.getNumero()));
            System.out.print("Digite o número do quarto desejado: ");
            int numeroQuarto = sc.nextInt();
            sc.nextLine(); 
            
            //incia internacao 
            Internacao novaInternacao = internacaoService.registrarInternacao(
                paciente, 
                numeroQuarto, 
                medicoResponsavel
            );
            
            System.out.println("\n INTERNAÇÃO REGISTRADA COM SUCESSO!");
            System.out.println("Paciente: " + novaInternacao.getPaciente().getNome());
            System.out.println("Médico: " + novaInternacao.getMedicoResponsavel().getNome());
            System.out.println("Quarto: " + novaInternacao.getQuarto().getNumero());
            System.out.println("Início: " + novaInternacao.getDataEntrada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
        } catch (java.util.InputMismatchException e) {
            System.out.println("Erro! Entrada inválida. por favor, digite números onde solicitado.");
            sc.nextLine();
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Erro ao registrar internação: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    //2- dar alta em internação
    public static void darAltaEmInternacao(Scanner sc, InternacaoService internacaoService, PacienteService pacienteService) {
        System.out.println("\n--- DAR ALTA EM INTERNAÇÃO ---");
        System.out.print("Digite o CPF do paciente para dar alta: ");
        String cpfPaciente = sc.nextLine();
        
        //buscar paciente
        var pacienteOpt = pacienteService.buscarPacientePorCpf(cpfPaciente);
        
        if (pacienteOpt.isEmpty()) {
            System.out.println("Erro: Paciente com CPF " + cpfPaciente + " não encontrado.");
            return;
        }
        
        //buscar a internacao ativa
        var internacaoOpt = internacaoService.buscarInternacaoAtivaPorPacienteCpf(cpfPaciente);
        
        if (internacaoOpt.isEmpty()) {
            System.out.println("Erro: O paciente " + pacienteOpt.get().getNome() + " não possui uma internação ativa.");
            return;
        }
        
        Internacao internacao = internacaoOpt.get();
        
        try {
         
            Internacao internacaoFinalizada = internacaoService.darAlta(internacao);
            
            System.out.println("\n ALTA CONCEDIDA COM SUCESSO!");
            System.out.println("=============================================");
            System.out.println("Paciente: " + internacaoFinalizada.getPaciente().getNome());
            System.out.println("Quarto Liberado: " + internacaoFinalizada.getQuarto().getNumero());
            System.out.println("Entrada: " + internacaoFinalizada.getDataEntrada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            System.out.println("Saída: " + internacaoFinalizada.getDataSaida().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            
           
            String custoFormatado = String.format(Locale.forLanguageTag("pt-BR"), "R$ %.2f", internacaoFinalizada.getCustoInternacao());
            
            System.out.println("CUSTO TOTAL FINAL: " + custoFormatado);
            System.out.println("=============================================");
            
        } catch (IllegalStateException e) {
            System.out.println("Erro ao dar alta: " + e.getMessage());
        }
    }


    //3 - cancelar internacao

    public static void cancelarInternacaoAtiva(Scanner sc, InternacaoService internacaoService, PacienteService pacienteService) {
    System.out.println("\n--- CANCELAR INTERNAÇÃO ATIVA ---");
    System.out.print("Digite o CPF do paciente cuja internação deve ser cancelada: ");
    String cpfPaciente = sc.nextLine();
   
    var pacienteOpt = pacienteService.buscarPacientePorCpf(cpfPaciente);
    
    if (pacienteOpt.isEmpty()) {
        System.out.println("Erro: Paciente com CPF " + cpfPaciente + " não encontrado.");
        return;
    }
    

    var internacaoOpt = internacaoService.buscarInternacaoAtivaPorPacienteCpf(cpfPaciente);
    
    if (internacaoOpt.isEmpty()) {
        System.out.println("Erro: O paciente " + pacienteOpt.get().getNome() + " não possui uma internação ativa para ser cancelada.");
        return;
    }
    
    Internacao internacao = internacaoOpt.get();
    
    try {
        Internacao internacaoCancelada = internacaoService.cancelarInternacao(internacao);
        
        System.out.println("\n❌ INTERNAÇÃO CANCELADA COM SUCESSO!");
        System.out.println("=============================================");
        System.out.println("Paciente: " + internacaoCancelada.getPaciente().getNome());
        System.out.println("Quarto " + internacaoCancelada.getQuarto().getNumero() + " liberado.");
        System.out.println("Internação iniciada em: " + internacaoCancelada.getDataEntrada().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        System.out.println("Status Final: Cancelada");
        System.out.println("=============================================");
        
    } catch (IllegalStateException e) {
        System.out.println("Erro ao cancelar internação: " + e.getMessage());
    }
}

// gerenciamento de internações
    public static void gerenciarInternacoes(Scanner sc, InternacaoService internacaoService, PacienteService pacienteService,
                MedicoService medicoService, QuartoService quartoService){
                    
        int opcaoInternacao = -1;


        while(opcaoInternacao != 0){
            System.out.println("\n=============== GERENCIAR INTERNAÇÕES ===============");
            System.out.println("\nEscolha uma Opção:");
            System.out.println("1- Registrar Nova Internação");
            System.out.println("2- Dar Alta em Internação");
            System.out.println("3- Cancelar Internação");
            System.out.println("4- Visualizar Quartos Livres");
            System.out.println("0- Voltar ao Menu Principal");
                            

            try {
                opcaoInternacao = sc.nextInt();
                sc.nextLine();

                switch(opcaoInternacao){
                
                    case 1: 
                    
                        registrarNovaInternacao(sc, internacaoService, pacienteService, medicoService, quartoService);
                        break;

                    case 2:
                  
                        darAltaEmInternacao(sc, internacaoService, pacienteService);
                        break;

                    case 3:
                        cancelarInternacaoAtiva(sc, internacaoService, pacienteService);
                        break;
                    
                    case 4:
                        System.out.println("\n==== QUARTOS LIVRES ====");
                        quartoService.listarQuartosLivres().forEach(System.out::println);
                        break;
                    
                    case 0:
                        System.out.println("voltando...");
                        break;
                    
                    default:
                        System.out.println("Opção Inválida. Tente novamente!");


                }

                
            } catch(java.util.InputMismatchException e){
                System.out.println("Erro! Entrada inválida. por favor, digite um número válido.");
                sc.nextLine();
                opcaoInternacao = -1; 
            }

        }   



    }



}