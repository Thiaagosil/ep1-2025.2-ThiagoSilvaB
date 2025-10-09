# 🏥 Trabalho Prático – Sistema de Gerenciamento Hospitalar  

### 🎯 Objetivo  
Implementar um *Sistema de Gerenciamento Hospitalar* em *Java, aplicando conceitos avançados de **Programação Orientada a Objetos (POO), com foco em **herança, polimorfismo, encapsulamento, persistência de dados* e *regras de negócio mais complexas*.  

---
## Descrição do Projeto

Desenvolvimento de um sistema de gerenciamento hospitalar utilizando os conceitos de orientação a objetos (herança, polimorfismo e encapsulamento) e persistência de dados em arquivos.

## Dados do Aluno

- **Nome completo:** Thiago Da Silva Borges
- **Matrícula:** 251016045
- **Curso:** Engenharias
- **Turma:** 02

---

## Instruções para Compilação e Execução

1. **Compilação:**  
 No terminal, acesse o diretório src dentro da pasta Projeto e digite: `javac Main.java`
  

3. **Execução:**  
  Depois de compilar, ainda no mesmo diretório (src), execute: `java Main`

4. **Estrutura de Pastas:**  

**projeto está dividido em pacotes principais, cada um com uma função específica:*

MODELS : 
 Contém as classes de domínio que representam as entidades do sistema, como Paciente, Medico, Consulta, Internacao e PlanoDeSaude.

 REPOSITORY :
Responsável pela persistência dos dados, realizando leitura e escrita dos arquivos CSV.


 SERVICES :
Agrupa a lógica de negócio do sistema.
As classes aqui processam, validam e interligam os dados entre os repositórios e a interface.


 UI :
Contém a interface de interação com o usuário.
A classe Menu.java é o ponto de entrada do sistema, exibindo as opções e controlando a navegação entre as funcionalidades.

 DATA :
Armazena os arquivos .csv usados pelo sistema (medicos.csv, pacientes.csv, etc).
 




3. **Versão do JAVA utilizada:**  
   [A versão ultilizada no projeto foi: `java 21`]

---

## Vídeo de Demonstração

- (https://drive.google.com/drive/folders/1CPM9qS_gV_pBo4squMGCNdbbg2UWPkT8?usp=drive_link)

---

## Prints da Execução

1. Menu Principal:  
   <img width="302" height="252" alt="image" src="https://github.com/user-attachments/assets/1ef260b0-5ff8-404e-a508-c97d7a9b9c97" />


2. Cadastro de Médico:  
   <img width="407" height="426" alt="image" src="https://github.com/user-attachments/assets/f561c123-cd3a-4347-bcfa-4c23c6cbae13" />


3. Relatório de ?:  
  <img width="418" height="171" alt="image" src="https://github.com/user-attachments/assets/ccfb0fca-9c36-4278-9a63-192191533c5a" />


 <img width="517" height="451" alt="image" src="https://github.com/user-attachments/assets/859458d8-eb6f-4179-809a-c2d74ff86580" />


---

---

## Observações (Extras ou Dificuldades)

- A maior dificuldade foi garantir a consistência dos dados e a lógica de verificação de horários para o agendamento de consultas e quartos para internações, aplicando a persistência correta.

---

## Contato

- 2005thiagosilva@gmail.com

---

### 🖥️ Descrição do Sistema  

O sistema deve simular o funcionamento de um hospital com cadastro de *pacientes, médicos, especialidades, consultas e internações*.  

1. *Cadastro de Pacientes*  
   - Pacientes comuns e pacientes especiais (ex: com plano de saúde).  
   - Cada paciente deve ter: nome, CPF, idade, histórico de consultas e internações.  

2. *Cadastro de Médicos*  
   - Médicos podem ter especialidades (ex: cardiologia, pediatria, ortopedia).  
   - Cada médico deve ter: nome, CRM, especialidade, custo da consulta e agenda de horários.  

3. *Agendamento de Consultas*  
   - Um paciente pode agendar uma consulta com um médico disponível.  
   - Consultas devem registrar: paciente, médico, data/hora, local, status (agendada, concluída, cancelada).  
   - Pacientes especiais (plano de saúde) podem ter *vantagens*, como desconto.  
   - Duas consultas não podem estar agendadas com o mesmo médico na mesma hora, ou no mesmo local e hora

4. *Consultas e Diagnósticos*  
   - Ao concluir uma consulta, o médico pode registrar *diagnóstico* e/ou *prescrição de medicamentos*.  
   - Cada consulta deve ser registrada no *histórico do paciente*.  

5. *Internações*  
   - Pacientes podem ser internados.  
   - Registrar: paciente, médico responsável, data de entrada, data de saída (se já liberado), quarto e custo da internação.  
   - Deve existir controle de *ocupação dos quartos* (não permitir duas internações no mesmo quarto simultaneamente).  
   - Internações devem poder ser canceladas, quando isso ocorrer, o sistema deve ser atualizado automaticamente.

6. *Planos de saúde*    
   -  Planos de saude podem ser cadastrados.
   -  Cada plano pode oferecer *descontos* para *especializações* diferentes, com possibilidade de descontos variados.
   -  Um paciente que tenha o plano de saúde deve ter o desconto aplicado.
   -  Deve existir a possibilidade de um plano *especial* que torna internação de menos de uma semana de duração gratuita.
   -  Pacientes com 60+ anos de idade devem ter descontos diferentes.

7. *Relatórios*  
   - Pacientes cadastrados (com histórico de consultas e internações).  
   - Médicos cadastrados (com agenda e número de consultas realizadas).  
   - Consultas futuras e passadas (com filtros por paciente, médico ou especialidade).  
   - Pacientes internados no momento (com tempo de internação).  
   - Estatísticas gerais (ex: médico que mais atendeu, especialidade mais procurada).  
   - Quantidade de pessoas em um determinado plano de saúde e quanto aquele plano *economizou* das pessoas que o usam.  


---

### ⚙️ Requisitos Técnicos  
- O sistema deve ser implementado em *Java*.  
- Interface via *terminal (linha de comando)*.  
- Os dados devem ser persistidos em *arquivos* (.txt ou .csv).  
- Deve existir *menu interativo*, permitindo navegar entre as opções principais.  

---

### 📊 Critérios de Avaliação  

1. *Modos da Aplicação (1,5)* → Cadastro de pacientes, médicos, planos de saúde, consultas e internações.  
2. *Armazenamento em arquivo (1,0)* → Dados persistidos corretamente, leitura e escrita funcional.  
3. *Herança (1,0)* → Ex.: Paciente e PacienteEspecial, Consulta e ConsultaEspecial, Médico e subclasses por especialidade.  
4. *Polimorfismo (1,0)* → Ex.: regras diferentes para agendamento, preços de consultas.
5. *Encapsulamento (1,0)* → Atributos privados, getters e setters adequados.  
6. *Modelagem (1,0)* → Estrutura de classes clara, bem planejada e com relacionamentos consistentes.  
7. *Execução (0,5)* → Sistema compila, roda sem erros e possui menus funcionais.  
8. *Qualidade do Código (1,0)* → Código limpo, organizado, nomes adequados e boas práticas.  
9. *Repositório (1,0)* → Uso adequado de versionamento, commits frequentes com mensagens claras.  
10. *README (1,0)* → Vídeo curto (máx. 5 min) demonstrando as funcionalidades + prints de execução + explicação da modelagem.  

🔹 *Total = 10 pontos*  
🔹 *Pontuação extra (até 1,5)* → Melhorias relevantes, como:  
- Sistema de triagem automática com fila de prioridade.  
- Estatísticas avançadas (tempo médio de internação, taxa de ocupação por especialidade).  
- Exportação de relatórios em formato .csv ou .pdf.  
- Implementação de testes unitários para classes principais.  
- Menu visual.
