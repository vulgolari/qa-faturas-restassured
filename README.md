# QA Faturas RestAssured

Este projeto contém testes automatizados para a API de Faturas utilizando RestAssured.

## Requisitos

- Java 17 ou superior
- Maven 3.8 ou superior
- Git

## Configuração do Ambiente

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/qa-faturas-restassured.git
cd qa-faturas-restassured
```

2. Instale as dependências:
```bash
mvn clean install
```

## Estrutura do Projeto

```
src/
├── main/
│   └── java/
│       └── com/
│           └── ntconsult/
│               └── qa/
│                   └── faturas/
│                       ├── config/
│                       ├── models/
│                       ├── services/
│                       └── utils/
└── test/
    ├── java/
    │   └── com/
    │       └── ntconsult/
    │           └── qa/
    │               └── faturas/
    │                   └── tests/
    └── resources/
        ├── data/
        └── jacoco/
```

## Executando os Testes

Para executar todos os testes:
```bash
mvn test
```

Para executar testes específicos:
```bash
mvn test -Dtest=NomeDaClasseDeTeste
```

## Relatórios de Cobertura

Após a execução dos testes, os relatórios de cobertura do JaCoCo estarão disponíveis em:
```
target/site/jacoco/index.html
```

## Contribuindo

1. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
2. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
3. Faça push para a branch (`git push origin feature/nova-feature`)
4. Abra um Pull Request

## Licença

Este projeto está licenciado sob a licença MIT - veja o arquivo [LICENSE](LICENSE) para mais detalhes.
