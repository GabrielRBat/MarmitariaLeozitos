# Marmitaria Leozitos API

API REST para gerenciamento de marmitas e pedidos, desenvolvida com Spring Boot 3, JDBC, MySQL, integracao com a API externa ViaCEP e mensageria com RabbitMQ.

## Integrantes

Equipe 7

- Vitor Agner
- Gabriel B.
- Henry
- Leonardo

## Descricao do Sistema

O Marmitaria Leozitos e um sistema de gerenciamento de vendas para marmitarias. O administrador cadastra marmitas no catalogo e registra pedidos dos clientes. O sistema calcula automaticamente o valor total com base nos precos atuais das marmitas no banco, valida os itens do pedido, consulta endereco de entrega pelo CEP usando a ViaCEP e calcula uma taxa de entrega por regiao.

Quando um pedido e criado, a API publica uma mensagem no RabbitMQ. Um consumidor interno escuta a fila e registra o evento recebido, demonstrando comunicacao assincrona entre sistemas.

## Regras de Negocio

- Marmita deve ter nome e preco maior que zero.
- Pedido deve ter pelo menos uma marmita.
- Cada item do pedido deve ter quantidade maior que zero.
- O valor total e calculado pela soma dos subtotais dos itens.
- Pedidos com entrega consultam o CEP na ViaCEP e somam taxa de entrega:
  - SP: R$ 5,00
  - RJ, MG ou ES: R$ 10,00
  - demais estados: R$ 15,00
- Ao criar pedido, a API publica o evento `pedido.criado` no RabbitMQ.

## Endpoints da API

### Marmitas

| Metodo | Endpoint | Descricao |
| :--- | :--- | :--- |
| `GET` | `/marmitas` | Lista todas as marmitas cadastradas. |
| `GET` | `/marmitas/{id}` | Busca os detalhes de uma marmita especifica. |
| `POST` | `/marmitas` | Cadastra uma nova marmita. |
| `PUT` | `/marmitas/{id}` | Atualiza os dados de uma marmita existente. |
| `DELETE` | `/marmitas/{id}` | Remove uma marmita do catalogo. |

### Pedidos

| Metodo | Endpoint | Descricao |
| :--- | :--- | :--- |
| `GET` | `/pedidos` | Lista todos os pedidos realizados. |
| `GET` | `/pedidos/{id}` | Busca os detalhes de um pedido e seus itens. |
| `POST` | `/pedidos` | Cria um pedido sem entrega e calcula o total dos itens. |
| `POST` | `/pedidos/com-entrega` | Cria um pedido com CEP, consulta ViaCEP, calcula taxa de entrega e publica evento no RabbitMQ. |
| `PUT` | `/pedidos/{id}` | Atualiza itens de um pedido. |
| `PUT` | `/pedidos/{id}/com-entrega` | Atualiza itens e dados de entrega de um pedido. |
| `DELETE` | `/pedidos/{id}` | Cancela e remove um pedido do sistema. |

### Enderecos

| Metodo | Endpoint | Descricao |
| :--- | :--- | :--- |
| `GET` | `/enderecos/{cep}` | Consulta endereco na ViaCEP. |
| `GET` | `/enderecos/{cep}/entrega` | Consulta endereco e simula taxa de entrega. |

## Exemplos de JSON

Criar marmita:

```json
{
  "nome": "Feijoada",
  "preco": 25.9,
  "categoria": "Brasileira"
}
```

Criar pedido sem entrega:

```json
[
  {
    "marmita": {
      "id": 1
    },
    "quantidade": 2
  }
]
```

Criar pedido com entrega:

```json
{
  "cepEntrega": "01001000",
  "itens": [
    {
      "marmita": {
        "id": 1
      },
      "quantidade": 2
    }
  ]
}
```

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.5
- Spring Web
- Spring JDBC
- Spring AMQP
- MySQL 8.0
- RabbitMQ 3 com Management UI
- ViaCEP
- Swagger / SpringDoc OpenAPI
- Docker e Docker Compose
- Maven

## Arquitetura do Projeto

O projeto segue arquitetura em camadas:

- `model`: classes de dominio e DTOs.
- `controller`: endpoints REST.
- `service`: regras de negocio, validacoes, calculos e orquestracao.
- `repository`: acesso ao banco com JDBC.
- `client`: integracao com API externa ViaCEP.
- `messaging`: configuracao, produtor e consumidor RabbitMQ.
- `exception`: tratamento global de erros.

## Como Executar

Pre-requisitos:

- Docker Desktop instalado e rodando.

Na pasta raiz do projeto, execute:

```bash
docker-compose up --build
```

Aguarde a aplicacao iniciar na porta `8080`.

Servicos disponiveis:

- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- RabbitMQ Management: `http://localhost:15672`
  - usuario: `guest`
  - senha: `guest`
- MySQL:
  - host: `localhost`
  - porta: `3307`
  - usuario: `root`
  - senha: `root`
  - database: `marmitaria_db`

## Banco de Dados

O script de criacao das tabelas esta em:

```text
demo/src/main/resources/schema.sql
```

Se o banco ja tiver sido criado com uma versao antiga do schema, recrie o volume do Docker ou atualize manualmente a tabela `pedido` para incluir `cep_entrega`, `endereco_entrega` e `taxa_entrega`.
