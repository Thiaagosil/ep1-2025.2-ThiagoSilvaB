package repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import models.PlanosSaude.PlanoEspecial;
import models.PlanosSaude.PlanosDeSaude;
import models.pessoa.medico.Especialidade;

public class PlanoSaudeRepositoryCSV {
    
    private static final String ARQUIVO_PLANOS = "data/planos.csv";

    // salva todos os planos
    public void salvarPlanos(List<PlanosDeSaude> planos) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARQUIVO_PLANOS, false))) {
            for (PlanosDeSaude plano : planos) {
                
                if (plano == null || plano.getNome() == null || plano.getCodigo() == null) {
                    continue; 
                }
                
                String tipo = (plano instanceof PlanoEspecial) ? "ESPECIAL" : "COMUM";
                writer.print(plano.getNome() + "," + plano.getCodigo() + "," + tipo);

                
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
    
    // carrega todos os planos
    public List<PlanosDeSaude> carregarPlanos() {
        List<PlanosDeSaude> planosCarregados = new ArrayList<>();
        new java.io.File("data").mkdirs();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_PLANOS))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.trim().split(",");
                if (dados.length >= 3) {
                    String nome = dados[0].trim();
                    String codigo = dados[1].trim();
                    String tipo = dados[2].trim();
                    
                    if (nome.isEmpty() || codigo.isEmpty()) {
                        continue;
                    }
                    
                    PlanosDeSaude plano;
                    if (tipo.equals("ESPECIAL")) {
                        plano = new PlanoEspecial(nome, codigo);
                    } else {
                        plano = new PlanosDeSaude(nome, codigo);
                    }
                    
                    // carrega os descontos
                    for (int i = 3; i < dados.length; i++) {
                        String[] descontoData = dados[i].split(":");
                        if (descontoData.length == 2) {
                            try {
                                Especialidade especialidade = Especialidade.valueOf(descontoData[0].trim());
                                double desconto = Double.parseDouble(descontoData[1].trim());
                                plano.AdicionarDesconto(especialidade, desconto);
                            } catch (IllegalArgumentException ignored) {
                            // ignora dados mal formatados
                            }
                        }
                    }
                    planosCarregados.add(plano);
                }
            }
        } catch (IOException e) {
            // retorna lista vazia se o arquivo não existir
        }
        return planosCarregados;
    }
}