package ui;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional; 
import java.util.Scanner;

import models.PlanosSaude.PlanoEspecial;
import models.PlanosSaude.PlanosDeSaude;
import models.consulta.Consulta;
import models.consulta.ConsultaEspecial;
import models.internacao.Internacao;
import models.pessoa.medico.Especialidade;
import models.pessoa.medico.Medico; 
import models.pessoa.paciente.Paciente;
import services.ConsultaService;
import services.InternacaoService;
import services.MedicoService;
import services.PacienteService;
import services.PlanoSaudeService;
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

        PlanoSaudeService planoSaudeService = new PlanoSaudeService();

        
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
            System.out.println("7- Históricos e Buscas");
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
                    gerenciarConsultas(sc, consultaService, pacienteService, medicoService);
                    break;

                case 4:
                    gerenciarInternacoes(sc, internacaoService, pacienteService, medicoService, quartoService);
                    break;
                     
                case 5:
                    gerenciarPlanoSaude(sc, planoSaudeService);
                break;

                // case 6:
                //     gerenciarPacientes(sc, pacientes);
                //     break;

                case 7:
                    gerenciarHistorico(sc, pacienteService);
                break;

                case 0:
                    System.out.println("Fechando o Sistema...");
                    break;

                    
                default:
                    System.out.println("Opção Inválida. Tente novamente! ");
                
                }
        }

        sc.close();
   }


    //historico paciente
    public static void gerenciarHistorico(Scanner sc, PacienteService pacienteService){
        System.out.println("\n--- BUSCA DE HISTÓRICO DE PACIENTE ---");

        try {
            System.out.print("Digite o CPF do paciente para buscar o histórico: ");
            String cpfPaciente = sc.nextLine();
            
            java.util.Optional<Paciente> pacienteOpt = pacienteService.buscarPacientePorCpf(cpfPaciente);

            if (pacienteOpt.isEmpty()) {
                System.out.println("Erro! Paciente com CPF " + cpfPaciente + " não encontrado.");
                return;
            }
            
            Paciente paciente = pacienteOpt.get();
            System.out.println("\n--- Histórico de: " + paciente.getNome() + " ---");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            
            List<Consulta> consultas = paciente.getHistoricoConsultas(); 
            
            System.out.println("----------------------------------------");
            if (consultas.isEmpty()) {
                System.out.println("Consultas: Nenhuma consulta registrada.");
            } else {
                System.out.println("Consultas Registradas (" + consultas.size() + "):");
                
                for (Consulta c : consultas) {
                    System.out.println(" - Data: " + c.getDataHora().format(formatter) + 
                                    "  \nMédico: " + c.getMedico().getNome() + 
                                    "  \nStatus: " + c.getStatus());
                }
            }
            
            
            List<Internacao> internacoes = paciente.getHistoricoInternacoes(); 

            System.out.println("----------------------------------------");
            if (internacoes.isEmpty()) {
                System.out.println("Internações: Nenhuma internação registrada.");
            } else {
                System.out.println("Internações Registradas (" + internacoes.size() + "):");
                for (Internacao i : internacoes) {
                    
                    String saida = (i.getDataSaida() != null) 
                        ? i.getDataSaida().format(formatter) 
                        : "Em curso";
                    
                    System.out.println(" - Entrada: " + i.getDataEntrada().format(formatter) + 
                                    "  \nSaída: " + saida + 
                                    "  \nQuarto: " + i.getQuarto().getNumero()); 
                }
            }
            
            System.out.println("----------------------------------------");
            
            if (paciente.getInternacaoAtual().isPresent()) {
                System.out.println("\nATENÇÃO: Paciente está atualmente INTERNADO.");
            }

            System.out.println("Busca de histórico concluída.");

        } catch (Exception e) {
            System.out.println("Ocorreu um erro ao buscar o histórico: " + e.getMessage());
        }
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
public static void gerenciarConsultas(Scanner sc, ConsultaService consultaService, PacienteService pacienteService, MedicoService medicoService){
    int opcaoConsulta = -1;

    while(opcaoConsulta != 0){
        System.out.println("\n=============== TELA CONSULTA ===============");
        System.out.println("\nEscolha uma Opção :");

        System.out.println("1- Marcar Consulta");
        System.out.println("2- Listar Consultas Cadastradas"); 
        System.out.println("3- Finalizar Consulta"); 
        System.out.println("4- Cancelar Consulta");

        System.out.println("0- Voltar ao Menu Principal");
        
        try {
            opcaoConsulta = sc.nextInt();
            sc.nextLine(); 
        } catch (java.util.InputMismatchException e) {
            System.out.println("Erro! Entrada inválida. Por favor, digite um número.");
            sc.nextLine(); 
            opcaoConsulta = -1;
            continue;
        }

        switch(opcaoConsulta) {
            case 1:
                marcarNovaConsulta(sc, consultaService, pacienteService, medicoService);                
                break;
            case 2:
                
                 System.out.println("\n==== LISTA DE CONSULTAS CADASTRADAS ====");

                List<Consulta> lista = consultaService.listarTodasConsultas();
                
                if (lista.isEmpty()) {
                    System.out.println("Nenhuma consulta agendada!");
                } else {
                    java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

                    for (Consulta c : lista) {
                        System.out.println("-=-==-=-=-=-==--==---==-=--=-=--==-=-=--==-=-");
                        System.out.println("Paciente: " + c.getPaciente().getNome() + " (CPF: " + c.getPaciente().getCpf() + ")");
                        System.out.println("Médico: " + c.getMedico().getNome() + " (CRM: " + c.getMedico().getCRM() + ")");
                        System.out.println("Data/Hora: " + c.getDataHora().format(formatter));
                        System.out.println("Status: " + c.getStatus());
                        
                        // caso seja ConsultaEspecial
                        if (c instanceof ConsultaEspecial) {
                            ConsultaEspecial ce = (ConsultaEspecial) c;
                            String custoFormatado = String.format(java.util.Locale.forLanguageTag("pt-BR"), "R$ %.2f", ce.getCustoFinal());
                            System.out.println("Tipo: Especial (Plano: " + ce.getPlanoAplicado().getNome() + ")");
                            System.out.println("Valor Original: R$" + String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.2f", c.getMedico().getCustoConsulta()));
                            System.out.println("Desconto Aplicado: R$" + String.format(java.util.Locale.forLanguageTag("pt-BR"), "%.2f", ce.getValorDesconto()));
                            System.out.println("Custo Final: " + custoFormatado);

                        } else {
                            String custoFormatado = String.format(java.util.Locale.forLanguageTag("pt-BR"), "R$ %.2f", c.getMedico().getCustoConsulta());
                            System.out.println("Tipo: Comum");
                            System.out.println("Custo: " + custoFormatado);
                        }
                    }
                    System.out.println("-=-==-=-=-=-==--==---==-=--=-=--==-=-=--==-=-");
                }
                break;

            case 3:
                finalizarConsultaRealizada(sc, consultaService);
            break;

            case 4:
                 cancelarConsultaAgendada(sc, consultaService);
            break;

            case 0:
                System.out.println("Voltando ao menu principal...");
                break;
            default:
                System.out.println("Opção Inválida. Tente novamente! ");
        }
    }
}

    //case 1 (consulta) - marcar consulta

    public static void marcarNovaConsulta(Scanner sc, ConsultaService consultaService, PacienteService pacienteService, MedicoService medicoService){
            System.out.println("========== AGENDAMENTO DE CONSULTA ==========");

            try {
                // Buscar paciente
                System.out.print("Digite o CPF do paciente para a consulta: ");
                String cpfPaciente = sc.nextLine();
                Optional<Paciente> optPaciente = pacienteService.buscarPacientePorCpf(cpfPaciente);

                
                if (optPaciente.isEmpty()) {
                System.out.println("Erro! Paciente com CPF " + cpfPaciente + " não encontrado.");
                
                return;
                }
                
                Paciente paciente = optPaciente.get();

                //buscar medico 
                System.out.print("Digite o CRM do médico desejado: ");
                String crmMedicoStr = sc.nextLine();
                
                int crmMedico = Integer.parseInt(crmMedicoStr); 
                Optional<Medico> medicoOpt = medicoService.buscarMedicoPorCrm(crmMedicoStr); 

                if (medicoOpt.isEmpty()) {
                    System.out.println("Erro! Médico com CRM " + crmMedicoStr + " não encontrado.");
                    return;
                }

                Medico medico = medicoOpt.get();

                //data e hora
        
                System.out.print("Digite a data da consulta: ");
                String dataStr = sc.nextLine();
                
                System.out.print("Digite a hora da consulta: ");
                String horaStr = sc.nextLine();
                
                java.time.format.DateTimeFormatter format = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
                java.time.LocalDateTime dataHoraConsulta = java.time.LocalDateTime.parse(dataStr + " " + horaStr, format);
        
               
                //confirmação do agendamento
                System.out.println("\n SUCESSO! CONSULTA AGENDADA.");
                System.out.println("Paciente: " + paciente.getNome());
                System.out.println("Médico: " + medico.getNome() + " (" + medico.getEspecialidades().get(0).getDescricao() + ")");
                System.out.println("Data/Hora: " + dataHoraConsulta.format(format));
                


            } 
                
            
            
                //usuario colocou o CRM errado
            catch(NumberFormatException e){
                System.out.println("Erro de formato! O CRM deve ser um número inteiro. " + e.getMessage());
            }
                //usuario colocou a data errada
            catch(java.time.format.DateTimeParseException e){
                System.out.println("Erro de formato! A data ou hora deve estar no formato dd/MM/yyyy ou HH:mm, respectivamente.");
            }
            catch(Exception e){
                System.out.println("Ocorreu um erro ao agendar a consulta: " + e.getMessage());
            }
    }


    //case 3 (Consultas) - Finalizar consultas.
        public static void finalizarConsultaRealizada(Scanner sc, ConsultaService consultaService) {
            System.out.println("\n--- FINALIZAR CONSULTA REALIZADA ---");

            try {
                System.out.print("Digite o CPF do paciente: ");
                String cpfPaciente = sc.nextLine();
                
                System.out.print("Digite a data da consulta: ");
                String dataStr = sc.nextLine();
                
                System.out.print("Digite a hora da consulta: ");
                String horaStr = sc.nextLine();
                
                java.time.format.DateTimeFormatter format = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                java.time.LocalDateTime dataHoraConsulta = java.time.LocalDateTime.parse(dataStr + " " + horaStr, format);
                
                System.out.print("Digite o Diagnóstico: ");
                String diagnostico = sc.nextLine();
                
                System.out.print("Digite a Prescrição: ");
                String prescricao = sc.nextLine();
                
                
                //confirma finalização da consulta
                Consulta consultaFinalizada = consultaService.finalizarConsulta(cpfPaciente, dataHoraConsulta, diagnostico, prescricao);
                
                
                System.out.println("\n SUCESSO! CONSULTA FINALIZADA.");
                System.out.println("=-=-=-=-=-=-=-=--=-==-=-=-==-=-=-==-");
                System.out.println("Paciente: " + consultaFinalizada.getPaciente().getNome());
                System.out.println("Médico: " + consultaFinalizada.getMedico().getNome());
                System.out.println("Data/Hora: " + consultaFinalizada.getDataHora().format(format));
                System.out.println("Status Atual: " + consultaFinalizada.getStatus());
                System.out.println("Diagnóstico Registrado.");
                System.out.println("Prescrição Registrada.");
                System.out.println("=-=-=-=-=-=-=-=--=-==-=-=-==-=-=-==-");

                
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Erro de formato! Tente novamente.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro ao finalizar consulta: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        }
        
        //case 4 (Consulta) - Cancelar consulta
        public static void cancelarConsultaAgendada(Scanner sc, ConsultaService consultaService) {
            System.out.println("\n--- CANCELAR CONSULTA AGENDADA ---");

            try {
                //dados da consulta
                System.out.print("Digite o CPF do paciente: ");
                String cpfPaciente = sc.nextLine();
                
                System.out.print("Digite a data da consulta (dd/MM/yyyy): ");
                String dataStr = sc.nextLine();
                
                System.out.print("Digite a hora da consulta (HH:mm): ");
                String horaStr = sc.nextLine();
                
                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                java.time.LocalDateTime dataHoraConsulta = java.time.LocalDateTime.parse(dataStr + " " + horaStr, formatter);
                
                
                
                Consulta consultaCancelada = consultaService.cancelarConsulta(cpfPaciente, dataHoraConsulta);
                
                
                
                System.out.println("\n SUCESSO! CONSULTA CANCELADA.");
                System.out.println("=-=-=-=-=-=-=-=--=-==-=-=-==-=-=-==-");
                System.out.println("Paciente: " + consultaCancelada.getPaciente().getNome());
                System.out.println("Médico: " + consultaCancelada.getMedico().getNome());
                System.out.println("Data/Hora: " + consultaCancelada.getDataHora().format(formatter));
                System.out.println("Status Atual: " + consultaCancelada.getStatus());
                System.out.println("=-=-=-=-=-=-=-=--=-==-=-=-==-=-=-==-");

                
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Erro de formato! Tente novamente.");
            } catch (IllegalArgumentException e) {
                System.out.println("Erro ao cancelar consulta: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Ocorreu um erro inesperado: " + e.getMessage());
            }
        }











    //case 1 (Internações) - registrar nova Internação 
    
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

    //case 2 (Internações) - dar alta em internação
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


    //case 3 (Internações) - cancelar internacao

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


 
 
 
    //gerenciamento do plano de saúde
    public static void gerenciarPlanoSaude(Scanner sc, PlanoSaudeService planoSaudeService){
        int opcaoPlano = -1;

    while (opcaoPlano != 0) {
        System.out.println("\n=============== GERENCIAR PLANOS DE SAÚDE ===============");
        System.out.println("Escolha uma Opção:");
        System.out.println("1 - Cadastrar Novo Plano");
        System.out.println("2 - Configurar Descontos por Especialidade");
        System.out.println("3 - Listar Planos Cadastrados");
        System.out.println("0 - Voltar ao Menu Principal");
        
        try {
            opcaoPlano = sc.nextInt();
            sc.nextLine(); // Consumir a linha pendente

            switch (opcaoPlano) {
                case 1:
                    cadastrarPlanoSaude(sc, planoSaudeService); 
                    break;
                case 2:
                    //configurarDescontosPlano(sc, planoSaudeService);
                    break;
                case 3:
                    listarPlanosDeSaude(planoSaudeService); 
                    break;
                case 0:
                    System.out.println("Voltando ao Menu Principal...");
                    break;
                default:
                    System.out.println("Opção Inválida. Tente novamente!");
            }
        } catch (java.util.InputMismatchException e) {
            System.out.println("Erro! Entrada inválida. Por favor, digite um número válido.");
            sc.nextLine(); 
            opcaoPlano = -1;
        }
    }

}
 
 
 
 
    //cadastro de PLano de Saúde
    public static void cadastrarPlanoSaude(Scanner sc, PlanoSaudeService planoSaudeService){
        System.out.println("\n--- CADASTRO DE PLANO DE SAÚDE ---");
        sc.nextLine();

    try {
        System.out.print("Nome do Plano: ");
        String nome = sc.nextLine();

        System.out.print("Código Único do Plano: ");
        String codigo = sc.nextLine();
        
        System.out.println("\n--- Tipo de Plano ---");
        System.out.println("1 - Plano Básico");
        System.out.println("2 - Plano Especial (Cobre internação < 7 dias)");
        System.out.print("Escolha o tipo (1 ou 2): ");
        int tipo = sc.nextInt();
        sc.nextLine();

        PlanosDeSaude novoPlano;

            switch (tipo) {
                case 2 -> {
                    novoPlano = new PlanoEspecial(nome, codigo);
                    System.out.println("Plano Especial selecionado.");
                }
                case 1 -> {
                    novoPlano = new PlanosDeSaude(nome, codigo);
                    System.out.println("Plano Básico selecionado.");
                }
                default -> throw new IllegalArgumentException("Erro! Tipo de plano inválido.");
            }
        
        planoSaudeService.cadastrarPlano(novoPlano);

        System.out.println("\n--- SUCESSO! ---");
        System.out.println("Plano '" + novoPlano.getNome() + "' (" + novoPlano.getCodigo() + ") cadastrado com sucesso!");

    } 
    catch (IllegalArgumentException e) {
        System.out.println("ERRO ao cadastrar plano: " + e.getMessage());
    } 
    catch (java.util.InputMismatchException e) {
        System.out.println("ERRO de entrada. Por favor, digite um número para o tipo de plano.");
        sc.nextLine(); 
    } 
    catch (Exception e) {
        System.out.println("Erro inesperado: " + e.getMessage());
    }
}


    //Listar planos de saude
    public static void listarPlanosDeSaude(PlanoSaudeService planoSaudeService) {
        System.out.println("\n--- PLANOS DE SAÚDE CADASTRADOS ---");
        java.util.List<models.PlanosSaude.PlanosDeSaude> planos = planoSaudeService.listarPlanos();
        
        if (planos.isEmpty()) {
            System.out.println("Nenhum plano de saúde cadastrado.");
            return;
        }

        for (models.PlanosSaude.PlanosDeSaude plano : planos) {
            System.out.println("=====================================");
            System.out.println("NOME: " + plano.getNome());
            System.out.println("CÓDIGO: " + plano.getCodigo());

            //verificar caso seja plano especial
            if (plano instanceof models.PlanosSaude.PlanoEspecial) {
                System.out.println("TIPO: PLANO Especial");
            } else {
                System.out.println("TIPO: Plano Básico");
            }
            
            
            boolean temDesconto = false;
            // Percorrer todas as especialidades para ver quais tem desconto
            for (models.pessoa.medico.Especialidade especialidade : models.pessoa.medico.Especialidade.values()) {
                double desconto = plano.getDescontoPorEspecialidade(especialidade);
                if (desconto > 0.0) {
                    System.out.printf(" - %s: %.0f%%\n", especialidade.name(), desconto * 100);
                    temDesconto = true;
                }
            }
            
            if (!temDesconto) {
                System.out.println(" - Sem descontos de consulta configurados.");
            }
        }
        System.out.println("=====================================");
    }



}