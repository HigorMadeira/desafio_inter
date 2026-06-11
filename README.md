# Desafio Técnico Java + React

Aplicação de gerenciamento de tarefas desenvolvida para o desafio técnico Java SR.

A solução possui backend em Java/Spring Boot, frontend em React + TypeScript, banco PostgreSQL com migrations via Flyway, arquitetura em camadas e uma arquitetura orientada a eventos com Kafka para comunicação assíncrona entre componentes.

## Stack

### Backend

* Java 17
* Spring Boot
* Maven
* PostgreSQL
* Flyway
* JUnit / MockMvc

### Frontend

* React
* TypeScript
* Vite
* CSS puro

### Infraestrutura local

* Docker
* PostgreSQL
* Kafka

## Funcionalidades

A aplicação permite:

* Criar tarefa
* Listar tarefas
* Buscar tarefa por ID
* Atualizar tarefa
* Remover tarefa
* Alterar status da tarefa pelo frontend

Cada tarefa possui:

* ID
* Título
* Descrição
* Status
* Data de criação

Status disponíveis:

* `PENDING`
* `IN_PROGRESS`
* `DONE`

## Arquitetura

O backend foi organizado em camadas para separar responsabilidades e reduzir acoplamento.

### `api`

Camada de entrada HTTP.

Contém os controllers e DTOs de request/response.

Exemplos:

* `TaskController`
* `CreateTaskRequest`
* `UpdateTaskRequest`
* `TaskResponse`

Essa camada não contém regra de negócio. Ela apenas recebe a requisição, valida os dados básicos, chama a aplicação e devolve a resposta.

### `application`

Camada responsável pelos casos de uso.

Contém:

* `TaskService`
* `CreateTaskCommand`
* `UpdateTaskCommand`
* `TaskEventPublisher`

Essa camada orquestra os fluxos de criação, listagem, atualização e remoção de tarefas.

### `domain`

Camada central do negócio.

Contém:

* `Task`
* `TaskStatus`
* `TaskRepository`
* `TaskNotFoundException`
* `TaskCreatedEvent`
* `TaskUpdatedEvent`

A entidade `Task` não possui anotações de Spring ou JPA. A intenção foi manter o domínio independente de framework e banco de dados.

### `infrastructure`

Camada com detalhes técnicos.

Contém a implementação de persistência com JPA/PostgreSQL e o processamento simples de eventos.

Exemplos:

* `TaskJpaEntity`
* `SpringDataTaskJpaRepository`
* `PostgresTaskRepository`
* `TaskPersistenceMapper`
* `SpringTaskEventPublisher`
* `LogTaskEventHandler`

## Decisões técnicas

### Spring Boot

O desafio mencionava Micronaut como desejável, mas também aceitava Spring Boot. Optei por Spring Boot por ser uma stack madura, produtiva e adequada para demonstrar arquitetura, persistência, migrations, testes, healthcheck e integração com frontend dentro do prazo.

### Domínio separado da persistência

A entidade de domínio `Task` foi separada da entidade JPA `TaskJpaEntity`.

Essa decisão evita que o domínio fique acoplado ao Hibernate/JPA. A conversão entre domínio e persistência é feita pelo `TaskPersistenceMapper`.

### Flyway

O banco é versionado com Flyway.

A migration inicial fica em:

```text
backend/src/main/resources/db/migration/V1__create_tasks_table.sql
```

O Hibernate está configurado com:

```properties
spring.jpa.hibernate.ddl-auto=validate
```

Assim, o Hibernate apenas valida o schema. Quem cria e evolui as tabelas é o Flyway.

### Event-driven

Ao criar ou atualizar uma tarefa, a aplicação publica eventos:

* `TaskCreatedEvent`
* `TaskUpdatedEvent`

A publicação é feita por uma porta da aplicação:

```text
TaskEventPublisher
```

A implementação atual publica mensagens em um tópico Kafka chamado `task-events`.

A aplicação continua desacoplada da tecnologia de mensageria porque a camada `application` conhece apenas a interface `TaskEventPublisher`. A implementação concreta com Kafka fica na camada `infrastructure`.

Também existe um consumer simples, `LogTaskEventHandler`, que consome as mensagens do Kafka e registra o payload em log. Em um cenário real, esse consumer poderia ser evoluído para auditoria, métricas, notificações ou integração com outros serviços.

### Frontend

O frontend foi mantido simples, mas funcional.

Ele permite criar, listar, remover e alterar status de tarefas. A alteração de status é feita localmente primeiro e só é enviada ao backend quando o usuário clica em “Salvar status”.

Isso evita chamadas desnecessárias para a API e deixa a interação mais controlada.

## Como rodar com Docker

Na raiz do projeto:

```bash
docker compose up --build
```

Frontend disponível em:

```text
http://localhost:5173
```

## Endpoints principais

Base URL:

```text
http://localhost:8080/api/tasks
```

### Criar tarefa

```http
POST /api/tasks
```

Body:

```json
{
  "title": "Minha tarefa",
  "description": "Descrição da tarefa"
}
```

### Listar tarefas

```http
GET /api/tasks
```

### Buscar por ID

```http
GET /api/tasks/{id}
```

### Atualizar tarefa

```http
PUT /api/tasks/{id}
```

Body:

```json
{
  "title": "Minha tarefa atualizada",
  "description": "Descrição atualizada",
  "status": "IN_PROGRESS"
}
```

### Remover tarefa

```http
DELETE /api/tasks/{id}
```
## Documentação da API

A API possui documentação OpenAPI/Swagger gerada com Springdoc.

Com o backend rodando:

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Com a aplicação rodando via Docker Compose, também é possível acessar:

- Swagger UI via frontend/Nginx: http://localhost:5173/swagger-ui.html

## Testes

O projeto possui teste de integração para o fluxo principal da API.

Ele cobre:

* Criação de tarefa
* Listagem
* Atualização
* Remoção
* Erro 404
* Validação 400

Para rodar:

```bash
cd backend
./mvnw test
```

Ou apenas o teste principal:

```bash
./mvnw test -Dtest=TaskControllerIntegrationTest
```

## Logs de eventos

Ao criar ou atualizar uma tarefa, o backend registra logs parecidos com:

```text
task_event type=TASK_CREATED taskId=... title="..." occurredAt=...
task_event type=TASK_UPDATED taskId=... title="..." status=IN_PROGRESS occurredAt=...
```

Esse fluxo demonstra a emissão e o processamento desacoplado de eventos.

## Build

Backend:

```bash
cd backend
./mvnw clean package
```

Frontend:

```bash
cd frontend
npm run build
```

## Uso de IA

Ferramentas de IA foram utilizadas como apoio durante o desenvolvimento, sob orquestração, e revisão da documentação.

Foi usado github copilot como autocompleter, e claude cli para criação de estruturas repetitivas.

O uso ocorreu principalmente para apoio da criação de estruturas repetitivas como DTOs, entidades e mapeamentos. Para o frontend usei claude como agente para deixar o react mais refinado/bonito mais rapido.


