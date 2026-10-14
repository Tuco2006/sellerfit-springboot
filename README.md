# SellerFit - API Restful (Java / Spring Boot)

API do SellerFit feita para a Sprint 4. Recebe a transcrição de uma reunião comercial, identifica as dores do cliente (com a OpenAI ou com o motor local de regras), calcula o sinal de churn/upsell e sugere os atendentes da TOTVS mais compatíveis. Tem também o CRUD completo de clientes, atendentes, reuniões, dores e matches no banco Oracle da FIAP.

Equipe SellerFit:

- Mateus Saavedra De Mendonça Cruz - RM563266
- Davi Melo - RM561818
- Danilo Fernandes - RM561657
- Arthur Berlofa - RM564438
- Ulisses Ribeiro - RM562230

A documentação completa (endpoints, protótipo, modelo do banco e diagrama de classes) está em
`documentacao/SellerFit_Documentacao_Sprint4.pdf`.

## O que precisa ter instalado

- Java 21
- Não precisa instalar o Maven, o projeto já vem com o `mvnw`

## Como rodar

- 1 Banco de dados

As tabelas já estão criadas no Oracle da FIAP. O script de criação está em `script.sql`.

- 2 Usuário, senha e chave da OpenAI

Essas informações não ficam no código. Para rodar na sua máquina, crie o arquivo
`src/main/resources/local.properties` com:

```properties
db.usuario=seu usuario do Oracle
db.senha=sua senha do Oracle
openai.api.key=sua chave da OpenAI
```

Esse arquivo não vai para o GitHub. A chave da OpenAI é opcional: se ficar vazia, a análise da
transcrição é feita pelo motor local de regras em vez da IA.

- 3 Subir a API

```bash
.\mvnw spring-boot:run
```

Ou abrir o projeto no IntelliJ e rodar a classe `SellerfitApplication`.

A API sobe em `http://localhost:8080`. Quando aparecer `Started SellerfitApplication` no console ela
já está no ar (o terminal fica "parado" mesmo, é o servidor rodando). Para conferir, abra
`http://localhost:8080/api/health` no navegador. O campo `ia` mostra se está usando `openai` ou
`motor-local`.

## Deploy no Render

New > Web Service, conectar este repositório e escolher `Docker` como linguagem (usa o `Dockerfile`
do projeto). Em Health Check Path, colocar `/api/health`.

Variáveis de ambiente (Environment):

| Variável | Valor |
|---|---|
| `DB_USUARIO` | usuário do Oracle da FIAP |
| `DB_SENHA` | senha do Oracle da FIAP |
| `OPENAI_API_KEY` | chave da OpenAI |

A lista completa de endpoints e códigos de status está na documentação em PDF.
