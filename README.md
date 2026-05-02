# Stock Tracker

<div align="center">

![Java](https://img.shields.io/badge/Java-21-f89820?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Spring WebFlux](https://img.shields.io/badge/Spring%20WebFlux-Reactive-0A7A3D?style=for-the-badge&logo=spring&logoColor=white)
![Project Reactor](https://img.shields.io/badge/Project%20Reactor-Mono%20%7C%20Flux-6DB33F?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![H2](https://img.shields.io/badge/H2-Database-09476B?style=for-the-badge&logo=h2database&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Ready-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-Boilerplate%20Free-BC4521?style=for-the-badge)

API backend reativa para consultar cotações, histórico diário, overview de ações e gerenciar favoritos.

</div>

---

## Visão Geral

O **Stock Tracker** é uma API REST construída com **Spring Boot**, **Spring WebFlux** e **Project Reactor**. Ela consome dados da **Alpha Vantage API** para buscar informações de ações em tempo real e mantém uma lista de ações favoritas usando **Spring Data JPA**.

O projeto usa programação reativa com `Mono` e `Flux`, evitando chamadas bloqueantes como `.block()` dentro do fluxo WebFlux.

## Tecnologias

| Tecnologia | Uso no projeto |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot 4 | Base da aplicação |
| Spring WebFlux | Endpoints reativos |
| Project Reactor | Tipos `Mono` e `Flux` |
| WebClient | Cliente HTTP para Alpha Vantage |
| Spring Data JPA | Persistência dos favoritos |
| H2 Database | Banco local em arquivo |
| PostgreSQL | Driver disponível para banco externo |
| Lombok | Redução de boilerplate |
| Maven Wrapper | Build e execução do projeto |

## Funcionalidades

- Buscar cotação atual de uma ação.
- Consultar overview de uma empresa.
- Consultar histórico diário de preços.
- Salvar ações favoritas.
- Listar favoritos com preço atualizado.
- Persistir favoritos em banco H2 local.

## Arquitetura

```text
src/main/java/com/appsdeveloperblog/ws/stocktracker
├── client
│   └── StockClient.java          # Integração com Alpha Vantage via WebClient
├── config
│   └── WebClientConfig.java      # Configuração do WebClient
├── controller
│   └── StockController.java      # Endpoints REST
├── dto
│   ├── AlphaVantageResponse.java
│   ├── DailyStockResponse.java
│   ├── FavoriteStockRequest.java
│   ├── StockHistoryResponse.java
│   ├── StockOverviewResponse.java
│   └── StockResponse.java
├── entity
│   └── FavoriteStock.java        # Entidade JPA
├── repository
│   └── FavoriteStockRepository.java
└── service
    └── StockService.java         # Regras de negócio e mappers
```

## Pré-requisitos

- Java 21+
- Chave da Alpha Vantage
- Maven Wrapper incluído no projeto

Crie uma chave gratuita em:

```text
https://www.alphavantage.co/support/#api-key
```

## Configuração

O projeto espera a variável de ambiente `API_KEY`:

```bash
export API_KEY=sua_chave_aqui
```

Configuração principal:

```properties
server.port=8082
alpha.vantage.base.url=https://www.alphavantage.co/query
alpha.vantage.api.key=${API_KEY}
spring.datasource.url=jdbc:h2:file:./data/stockdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## Como Rodar

```bash
./mvnw spring-boot:run
```

A API sobe em:

```text
http://localhost:8082
```

Console H2:

```text
http://localhost:8082/h2-console
```

Dados de conexão H2:

```text
JDBC URL: jdbc:h2:file:./data/stockdb
User: sa
Password:
```

## Endpoints

### Buscar Cotação

```http
GET /api/v1/stocks/{stockSymbol}
```

Exemplo:

```bash
curl http://localhost:8082/api/v1/stocks/AAPL
```

Resposta:

```json
{
  "symbol": "AAPL",
  "price": 210.14,
  "lastUpdated": "2026-05-01"
}
```

### Buscar Overview

```http
GET /api/v1/stocks/{stockSymbol}/overview
```

Exemplo:

```bash
curl http://localhost:8082/api/v1/stocks/MSFT/overview
```

### Buscar Histórico Diário

```http
GET /api/v1/stocks/{symbol}/history?days=30
```

Exemplo:

```bash
curl "http://localhost:8082/api/v1/stocks/GOOGL/history?days=10"
```

Resposta:

```json
[
  {
    "date": "2026-05-01",
    "open": 170.0,
    "close": 172.4,
    "high": 173.1,
    "low": 169.8,
    "volume": 25300000
  }
]
```

### Adicionar Favorito

```http
POST /api/v1/stocks/favorites
```

Exemplo:

```bash
curl -X POST http://localhost:8082/api/v1/stocks/favorites \
  -H "Content-Type: application/json" \
  -d '{"symbol":"AAPL"}'
```

Resposta:

```json
{
  "id": 1,
  "symbol": "AAPL"
}
```

### Listar Favoritos com Preço Atual

```http
GET /api/v1/stocks/favorites
```

Exemplo:

```bash
curl http://localhost:8082/api/v1/stocks/favorites
```

Resposta:

```json
[
  {
    "symbol": "AAPL",
    "price": 210.14,
    "lastUpdated": "2026-05-01"
  },
  {
    "symbol": "MSFT",
    "price": 430.2,
    "lastUpdated": "2026-05-01"
  }
]
```

## Fluxo Reativo

O projeto usa `Mono` quando espera **zero ou um resultado**:

```java
Mono<StockResponse>
```

E usa `Flux` quando espera **vários resultados**:

```java
Flux<DailyStockResponse>
```

Exemplo do fluxo de favoritos:

```text
Banco H2
  -> FavoriteStockRepository.findAll()
  -> Flux.fromIterable(favorites)
  -> WebClient busca preço de cada símbolo
  -> Flux<StockResponse>
  -> JSON response
```

## Cuidados Importantes

- Não use `.block()` dentro de controllers ou services WebFlux.
- Valide respostas da Alpha Vantage antes de acessar `globalQuote()`.
- Normalize símbolos antes de salvar: `trim().toUpperCase()`.
- A API gratuita da Alpha Vantage possui limite de requisições.
- Se a Alpha Vantage retornar `"Note"` ou `"Information"`, o campo `"Global Quote"` pode vir ausente.

## Testes

```bash
./mvnw test
```

## Roadmap

- Tratamento global de erros com respostas padronizadas.
- Endpoint para remover favoritos.
- Cache para reduzir chamadas repetidas à Alpha Vantage.
- Paginação ou ordenação para favoritos.
- Perfil PostgreSQL para produção.

---

<div align="center">

Feito com Java, Spring Boot e programação reativa.

</div>
