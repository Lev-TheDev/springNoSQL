# 🍃 springNoSQL: API RESTful com Spring Boot e MongoDB

## 📌 Sumário
* [Sobre o Projeto](#-sobre-o-projeto)
* [Fundamentos Teóricos (NoSQL & Agregados)](#-fundamentos-teóricos-nosql--agregados)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Arquitetura e Modelo de Dados](#-arquitetura-e-modelo-de-dados)
* [Estrutura do Projeto](#-estrutura-do-projeto)
* [Configuração do Ambiente e MongoDB Compass](#-configuração-do-ambiente-e-mongodb-compass)
* [Como Executar a Aplicação](#-como-executar-a-aplicação)
* [Endpoints Principais](#-endpoints-principais)
* [Licença](#-licença)

---

## ✨ Sobre o Projeto

O repositório **springNoSQL** reúne os estudos práticos e a implementação de uma API RESTful construída em **Java** com o ecossistema **Spring Boot**, utilizando o banco de dados orientado a documentos **MongoDB** gerenciado e inspecionado visualmente através do **MongoDB Compass**.

O foco central é vivenciar a transição do paradigma relacional (SQL) para o orientado a documentos (NoSQL), compreendendo o desenho de dados baseado em **agregados**, o ganho de desempenho pela eliminação de junções complexas e as facilidades proporcionadas pelo **Spring Data MongoDB**.

---

## 🧠 Fundamentos Teóricos (NoSQL & Agregados)

Este projeto aplica conceitos fundamentais de arquiteturas modernas e bancos não relacionais:

### 1. Incompatibilidade de Impedância (*Object-Relational Impedance Mismatch*)
* No modelo relacional convencional, um objeto de domínio (ex.: um `Pedido`) precisa ser decomposto em várias tabelas normalizadas (`pedidos`, `itens_pedido`, `pagamentos`, `enderecos`, `clientes`).
* A leitura e montagem desse objeto exigem múltiplos comandos `LEFT OUTER JOIN`, o que eleva a latência, consome processamento excessivo de CPU e degrada o desempenho sob alta concorrência.

### 2. Escalabilidade: Vertical vs. Horizontal
* **Bancos Relacionais (SQL):** Projetados prioritariamente para **escala vertical** (aumento de hardware em uma única máquina física/virtual), o que impõe limites operacionais e custos elevados.
* **Bancos NoSQL:** Projetados nativamente para **escala horizontal** (distribuição de dados e processamento através de *clusters* com nós menores), garantindo alta resiliência e expansão flexível.

### 3. Modelo Orientado a Agregados (*Aggregate-Oriented Database*)
* O MongoDB opera na categoria de **Bancos Orientados a Documentos**.
* **Conceito de Agregado:** Um agregado é um conjunto de objetos estreitamente interligados que são tratados e persistidos como **uma única unidade atômica**.
* Dados lidos e alterados com frequência em conjunto permanecem no mesmo documento BSON. Isso garante que todos os dados do agregado residam fisicamente no **mesmo nó do cluster**, provendo atomicidade por documento e eliminando junções de rede.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 17+
* **Framework:** Spring Boot (Spring Data MongoDB, Spring Web)
* **Banco de Dados:** MongoDB
* **Ferramenta de Administração:** MongoDB Compass
* **Gerenciador de Dependências:** Maven
* **Cliente de Testes:** Postman / Insomnia / cURL

---

## 📦 Arquitetura e Modelo de Dados

Em vez de dispersar os dados do pedido em chaves estrangeiras, os elementos dependentes são embutidos como subdocumentos hierárquicos:

```json
{
  "_id": "60d5ec49f1b2c82d88c8e100",
  "instante": "2026-03-20T10:30:00Z",
  "cliente": {
    "id": "60d5ec49f1b2c82d88c8e101",
    "nome": "Maria Silva",
    "email": "maria@gmail.com"
  },
  "enderecoDeEntrega": {
    "logradouro": "Rua das Flores",
    "numero": "300",
    "bairro": "Jardim Botânico",
    "cidade": "Uberlândia"
  },
  "itens": [
    {
      "quantidade": 1,
      "preco": 2000.0,
      "produto": { "id": "p01", "nome": "Computador" }
    },
    {
      "quantidade": 2,
      "preco": 80.0,
      "produto": { "id": "p02", "nome": "Mouse" }
    }
  ],
  "pagamento": {
    "tipo": "CARTAO",
    "estado": "QUITADO",
    "numeroDeParcelas": 6
  }
}
```

> **Decisão de Design:** Objetos que possuem ciclo de vida atrelado ao registro principal (como itens e endereço de entrega) são tratados como subdocumentos aninhados, garantindo consulta e gravação em uma única operação de I/O.

---

## 📁 Estrutura do Projeto

O código-fonte segue a separação em camadas convencional de projetos Spring:

```text
src/main/java/com/projeto/springnosql/
│
├── config/             # Carga inicial e seed de dados para testes
├── domain/             # Documentos gerenciados pelo MongoDB (@Document, @Id, @DBRef)
├── dto/                # Data Transfer Objects para requisições e respostas enxutas
├── repository/         # Interfaces estendendo MongoRepository e consultas com @Query
├── resources/          # Controladores REST (@RestController, rotas e verbos HTTP)
│   └── exceptions/     # Tratamento centralizado de erros (@ControllerAdvice)
└── services/           # Regras de negócio e mediação com os repositórios
```

---

## 🧭 Configuração do Ambiente e MongoDB Compass

### 1. Inicializando o MongoDB Local
Certifique-se de ter o daemon do MongoDB rodando na porta padrão (`27017`):

```bash
# Executando via Docker:
docker run -d --name mongodb-local -p 27017:27017 mongo:latest
```

### 2. Conectando com o MongoDB Compass
1. Abra o **MongoDB Compass**.
2. Na tela de início, utilize a connection string padrão:
   ```text
   mongodb://localhost:27017
   ```
3. Clique no botão **Connect**.
4. Pelo Compass é possível inspecionar as coleções, visualizar dados em formato JSON ou tabular, além de monitorar índices e pipelines de agregação.

### 3. Configuração do `application.properties`
No arquivo `src/main/resources/application.properties`, configure os parâmetros de conexão:

```properties
spring.data.mongodb.uri=mongodb://localhost:27017/spring_nosql_db
spring.data.mongodb.database=spring_nosql_db
server.port=8080
```

---

## 🚀 Como Executar a Aplicação

### Pré-requisitos
* **JDK 17** ou superior instalado
* **Maven** (ou o executável `./mvnw` incluso no repositório)
* Instância ativa do **MongoDB**

### Passo a Passo
1. Clone o repositório:
   ```bash
   git clone https://github.com/SEU_USUARIO/springNoSQL.git
   cd springNoSQL
   ```
2. Compile e baixe as dependências:
   ```bash
   ./mvnw clean install
   ```
3. Inicie o servidor Spring Boot:
   ```bash
   ./mvnw spring-boot:run
   ```
4. A API estará pronta para responder em: `http://localhost:8080`

---

## 📡 Endpoints Principais (Exemplo)

| Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/users` | Lista todos os usuários cadastrados |
| `GET` | `/users/{id}` | Busca um usuário específico por ID |
| `POST` | `/users` | Cria um novo usuário |
| `PUT` | `/users/{id}` | Atualiza dados de um usuário existente |
| `DELETE` | `/users/{id}` | Remove um usuário pelo ID |
| `GET` | `/posts/{id}` | Retorna um post contendo comentários aninhados |
| `GET` | `/posts/titlesearch?text={termo}` | Realiza busca textual simples ou por Regex no MongoDB |

---

## 🤝 Contribuindo

1. Faça um Fork do projeto (`git clone https://github.com/SEU_USUARIO/springNoSQL.git`).
2. Crie uma branch para sua funcionalidade (`git checkout -b feature/minha-feature`).
3. Faça o commit de suas alterações (`git commit -m 'feat: adiciona consulta com @Query'`).
4. Envie as modificações (`git push origin feature/minha-feature`).
5. Abra um **Pull Request**.

---

## 📄 Licença

Este projeto está distribuído sob a licença [MIT](LICENSE).
