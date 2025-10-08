package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import models.PlanosSaude.PlanosDeSaude;
import models.PlanosSaude.PlanoEspecial; 
import models.pessoa.medico.Especialidade;

public class PlanoSaudeService {
    private static final String ARQUIVO_PLANOS = "data/planos.csv";
    private final List<PlanosDeSaude> planos; 

    public PlanoSaudeService() {
        new java.io.File("data").mkdirs();
        this.planos = carregarPlanos();
    }
    
    private void salvarTodosPlanos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PLANOS, false))) {
            for (PlanosDeSaude plano : planos) {
                String tipo = (plano instanceof PlanoEspecial) ? "ESPECIAL" : "COMUM";
                writer.print(plano.getNome() + "," + plano.getCodigo() + "," + tipo);

                //persistindo desconto
                for (Especialidade esp : Especialidade.values()) {
                    double desconto = plano.getDescontoPorEspecialidade(esp);
                    if (desconto > 0.0) {
                        writer.print("," + esp.name() + ":" + desconto);
                    }
                }
                writer.println(); 
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar planos no arquivo: " + e.getMessage());
        }
    }
    
    private List<PlanosDeSaude> carregarPlanos() {
        List<PlanosDeSaude> planosCarregados = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PLANOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
                if (dados.length >= 3) {
                    String nome = dados[0].trim();
                    String codigo = dados[1].trim();
                    String tipo = dados[2].trim();
                    
                    PlanosDeSaude plano;
                    if (tipo.equals("ESPECIAL")) {
                        plano = new PlanoEspecial(nome, codigo);
                    } else {
                        plano = new PlanosDeSaude(nome, codigo);
                    }
                    
                    //carrega os descontos
                    for (int i = 3; i < dados.length; i++) {
                        String[] descontoData = dados[i].split(":");
                        if (descontoData.length == 2) {
                            try {
                                Especialidade especialidade = Especialidade.valueOf(descontoData[0].trim());
                                double desconto = Double.parseDouble(descontoData[1].trim());
                                plano.AdicionarDesconto(especialidade, desconto);
                            } catch (IllegalArgumentException ignored) {
                            //ignora dados de desconto mal formatados
                            }

                        }
                    }
                    planosCarregados.add(plano);
                }
            }
        } catch (IOException e) {
            //retorna lista vazia se o arquivo não existir
        }
        return planosCarregados;
    }

    //método para cadastrar Planos (Comum ou Especial)
    public void cadastrarPlano(PlanosDeSaude novoPlano) {
        if (this.planos.stream().anyMatch(p -> p.getCodigo().equals(novoPlano.getCodigo()))) {
            throw new IllegalArgumentException("Erro! Já existe um plano com o código: " + novoPlano.getCodigo());
        }
        this.planos.add(novoPlano);
        salvarTodosPlanos();
    }
    
    //método para buscar plano por código
    public Optional<PlanosDeSaude> buscarPlanoPorCodigo(String codigo) {
        return this.planos.stream()
            .filter(p -> p.getCodigo().equals(codigo))
            .findFirst();
    }
    
    //método para listar todos os planos
    public List<PlanosDeSaude> listarPlanos() {
        return new ArrayList<>(this.planos);
    }
    
    //metodo para configurar Desconto por Especialidade
    public void configurarDesconto(String codigoPlano, Especialidade especialidade, double desconto) {
        Optional<PlanosDeSaude> planoOpt = buscarPlanoPorCodigo(codigoPlano);

        if (planoOpt.isEmpty()) {
            throw new IllegalArgumentException("Plano de Saúde não encontrado para o código: " + codigoPlano);
        }

        PlanosDeSaude plano = planoOpt.get();
        plano.AdicionarDesconto(especialidade, desconto); 

        salvarTodosPlanos(); 
    }
}