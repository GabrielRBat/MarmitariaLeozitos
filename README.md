# 🍲 Marmitaria Leozitos API

API REST para gerenciamento de marmitas e pedidos, desenvolvida com Spring Boot 3 e persistência JDBC. O projeto utiliza Docker para facilitar o ambiente de desenvolvimento.

## Descrição do Sistema

O **Marmitaria Leozitos** é um sistema de gerenciamento de vendas para marmitarias. Ele permite que o administrador cadastre marmitas (catálogo) e registre pedidos dos clientes. O sistema realiza automaticamente o cálculo do valor total do pedido com base nos preços atuais das marmitas no banco de dados, garantindo a integridade dos valores e a persistência dos dados através de uma estrutura robusta em MySQL.

## Endpoints da API

### Marmitas
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/marmitas` | Lista todas as marmitas cadastradas. |
| `GET` | `/marmitas/{id}` | Busca os detalhes de uma marmita específica. |
| `POST` | `/marmitas` | Cadastra uma nova marmita. |
| `PUT` | `/marmitas/{id}` | Atualiza os dados de uma marmita existente. |
| `DELETE` | `/marmitas/{id}` | Remove uma marmita do catálogo. |

### Pedidos
| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/pedidos` | Lista todos os pedidos realizados. |
| `GET` | `/pedidos/{id}` | Busca os detalhes de um pedido e seus itens. |
| `POST` | `/pedidos` | Cria um novo pedido (calcula total automaticamente). |
| `PUT` | `/pedidos/{id}` | Atualiza os itens ou status de um pedido. |
| `DELETE` | `/pedidos/{id}` | Cancela e remove um pedido do sistema. |

## Tecnologias Utilizadas


- **Java 17** (OpenJDK)
- **Spring Boot 3.2.5**
- **MySQL 8.0**
- **JDBC** (Java Database Connectivity)
- **Docker & Docker Compose**
- **Swagger (SpringDoc OpenAPI)** para documentação e testes
- **Maven** para gerenciamento de dependências

## Arquitetura do Projeto

O projeto segue o padrão **MVC (Model-View-Controller)** com uma camada de **Service** e **Repository**:

1.  **Model:** Classes de domínio (`Marmita`, `Pedido`, `ItemPedido`).
2.  **Controller:** Endpoints da API que recebem as requisições JSON.
3.  **Service:** Lógica de negócio, validações e regras de cálculo.
4.  **Repository:** Acesso ao banco de dados utilizando JDBC puro (SQL).
5.  **Exception:** Tratamento global de erros e exceções personalizadas.

## Como Executar

### Pré-requisitos
- Docker Desktop instalado e rodando. https://www.docker.com/products/docker-desktop/

### Passo a Passo

1.  Abra o terminal na pasta raiz do projeto (`C:\MarmitariaLeozitos`).
2.  Suba os containers utilizando o Docker Compose:
    ```bash
    docker-compose up --build
    ```
3.  Aguarde até visualizar a mensagem `Tomcat started on port 8080`.

---

## Como Testar

A maneira mais fácil de testar a API é através do **Swagger UI**, que fornece uma interface visual interativa.

1.  Com o projeto rodando, acesse:
     **[http://localhost:8080/swagger-ui/index.html]**

### Fluxo de Teste Sugerido:

1.  **Criar Marmita (POST):**
    - Vá em `POST /marmitas`.
    - Clique em "Try it out".
    - Use o JSON: `{"nome": "Feijoada", "preco": 25.90, "categoria": "Brasileira"}`.
    - Clique em "Execute".
2.  **Listar Marmitas (GET):**
    - Vá em `GET /marmitas` para ver o ID gerado pelo banco.
3.  **Criar Pedido (POST):**
    - Vá em `POST /pedidos`.
    - Use o JSON: `[{"marmita": {"id": 1}, "quantidade": 2}]`.
    - O sistema calculará o valor total automaticamente.
4.  **Testar Validação:**
    - Tente criar uma marmita com `preco: 0` e veja o erro 400 (Bad Request).
5.  **Testar Exceção:**
    - Tente buscar uma marmita com um ID que não existe e veja o erro 404 (Not Found) personalizado.

## Banco de Dados

Se desejar conectar ao banco de dados via DBeaver ou Workbench:
- **Host:** `localhost`
- **Porta:** `3307` (Mapeada para evitar conflitos com MySQL local)
- **Usuário:** `root`
- **Senha:** `root`
- **Database:** `marmitaria_db`

---
Desenvolvido para o Projeto de Integração de Sistemas.
