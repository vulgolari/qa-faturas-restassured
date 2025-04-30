# QA Desafio Técnico - Gerenciamento de Faturas

Este projeto contém testes automatizados para o sistema de gerenciamento de faturas.

## Estrutura do Projeto

```
src/test/java/com/example/
├── config/
│   └── TestConfig.java
├── model/
│   ├── Contrato.java
│   ├── Fatura.java
│   └── Pagamento.java
├── service/
│   └── ContratoService.java
└── tests/
    └── ContratoTests.java
```

## Tecnologias Utilizadas

- Java 11
- JUnit 5
- RestAssured
- Maven

## Casos de Teste Implementados

1. Criação de Contratos
   - Criação de contrato válido
   - Verificação de campos obrigatórios
   - Validação de datas

2. Geração de Faturas
   - Geração automática de faturas
   - Verificação de periodicidade
   - Cálculo de valores

3. Processamento de Pagamentos
   - Pagamento via diferentes métodos
   - Atualização de status
   - Validação de valores

4. Controle de Vencimentos
   - Aplicação de multas
   - Cálculo de juros
   - Atualização de status

## Como Executar os Testes

1. Certifique-se de que o sistema está rodando em `http://localhost:8080`
2. Execute os testes usando Maven:
   ```bash
   mvn test
   ```

## Dependências

As dependências estão configuradas no arquivo `pom.xml`:

- JUnit 5
- RestAssured
- TestContainers (para testes com containers Docker)

## Observações

- Os testes foram implementados seguindo boas práticas de automação
- A estrutura do projeto permite fácil manutenção e extensão
- Os casos de teste cobrem os principais fluxos do sistema
- A documentação está em português para facilitar o entendimento da equipe
