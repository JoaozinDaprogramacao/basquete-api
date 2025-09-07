# Documentação da API de Basquete

Bem-vindo à documentação oficial da API de Basquete. Esta API permite o
gerenciamento completo de jogadores, treinos e o acompanhamento de
desempenho, utilizando um sistema de autenticação moderno baseado em
OAuth2 e JWT.

**URL Base da API:** - **Desenvolvimento:** `http://localhost:8080` -
**Produção:** `https://api.seudominio.com.br`

## Autenticação

A API utiliza um fluxo de autenticação stateless com **Bearer Tokens
(JWT)**. Para obter um token, o consumidor da API deve guiar o usuário
através de um fluxo de login com o Google.

### Fluxo de Obtenção de Token

1.  **Início do Login:** O cliente (seu app frontend/mobile) deve
    redirecionar o usuário para o endpoint de login da nossa API:
    `GET /login`
2.  **Autorização do Google:** O usuário será apresentado à tela de
    login do Google. Após autorizar a aplicação, ele será redirecionado
    de volta para o backend.
3.  **Redirecionamento com Token:** O backend irá processar a
    autenticação, gerar um JWT e redirecionar o usuário para a URL do
    frontend configurada (ex: `https://seufrontend.com/login/callback`)
    com o token anexado como um parâmetro de consulta (`query param`):
    `https://seufrontend.com/login/callback?token=eyJhbGciOiJIUzI1NiJ9...`
4.  **Uso do Token:** Seu cliente deve extrair este token da URL,
    armazená-lo de forma segura e enviá-lo em todas as futuras
    requisições para endpoints protegidos no cabeçalho `Authorization`.

### Formato do Cabeçalho de Autorização

Para todas as chamadas a endpoints que requerem autenticação, inclua o
seguinte cabeçalho:

``` http
Authorization: Bearer <seu_jwt_token>
```

## Endpoints da API

### Autenticação

#### GET /login

Inicia o fluxo de autenticação com o Google. Este endpoint deve ser
aberto no navegador do usuário.

-   Autenticação: Não requerida.
-   Ação: Redireciona o usuário para a página de login do Google.

#### POST /logout

Invalida o token de acesso atual do usuário.

-   Autenticação: Requerida (Bearer Token).
-   Descrição: O token enviado no cabeçalho Authorization será
    adicionado a uma "blocklist" e não poderá mais ser usado.
-   Resposta de Sucesso: 200 OK, Corpo: Logout bem-sucedido.

------------------------------------------------------------------------

### Usuários

#### GET /api/v1/user/me

Obtém as informações do perfil do usuário atualmente autenticado.

-   Autenticação: Requerida (Bearer Token).
-   Resposta de Sucesso (200 OK):

``` json
{
  "id": "10987654321...",
  "nome": "LeBron James",
  "email": "lebron.james@example.com",
  "fotoUrl": "https://lh3.googleusercontent.com/a/exemplo..."
}
```

------------------------------------------------------------------------

### Jogadores

Endpoints para gerenciar os jogadores cadastrados. Todos requerem
autenticação.

#### POST /api/v1/jogadores

Cria um novo jogador com seus atributos iniciais.

-   Autenticação: Requerida (Bearer Token).
-   Corpo da Requisição (application/json):

``` json
{
  "nome": "Victor Wembanyama",
  "dataNascimento": "2004-01-04",
  "nomeResponsavel": "Felix Wembanyama",
  "contatoResponsavel": "(38)998765432",
  "rgEntregue": true,
  "modulo": "PROFISSIONAL",
  "atributosFisicos": {
    "altura": "2.24",
    "fisico": 85
  },
  "habilidadesTecnicas": {
    "drible": 70,
    "arremesso": 80,
    "bandeja": 85,
    "passe": 65,
    "arremesso3Pontos": 78,
    "lancesLivre": 82
  },
  "atributosMentais": {
    "visaoDeJogo": 75,
    "comunicacao": 80,
    "flow": 88
  }
}
```

-   Regras de Validação:
    -   `dataNascimento`: Obrigatório, formato YYYY-MM-DD, deve ser no
        passado.
    -   `modulo`: Obrigatório. Valores possíveis: INICIANTE,
        INTERMEDIARIO, AVANCADO, PROFISSIONAL.
-   Resposta de Sucesso (201 Created):
    -   Cabeçalho:
        `Location: http://localhost:8080/api/v1/jogadores/{uuid-gerado}`
    -   Corpo: O objeto completo do jogador criado.

#### GET /api/v1/jogadores

Lista todos os jogadores cadastrados.

-   Autenticação: Requerida (Bearer Token).
-   Resposta de Sucesso (200 OK): Array de jogadores.

#### GET /api/v1/jogadores/{id}

Busca um jogador específico pelo seu ID (UUID).

-   Autenticação: Requerida (Bearer Token).
-   Resposta de Sucesso (200 OK): Objeto do jogador.
-   Resposta de Erro: 404 Not Found.

#### PUT /api/v1/jogadores/{id}

Atualiza dados cadastrais, módulo e atributos físicos de um jogador.

-   Autenticação: Requerida (Bearer Token).
-   Corpo da Requisição:

``` json
{
  "nome": "Victor Wembanyama",
  "nomeResponsavel": "Felix Wembanyama",
  "contatoResponsavel": "(38)912345678",
  "rgEntregue": true,
  "modulo": "PROFISSIONAL",
  "atributosFisicos": {
    "altura": "2.25",
    "fisico": 88
  }
}
```

-   Resposta de Sucesso (200 OK): Objeto atualizado.
-   Respostas de Erro: 400 Bad Request, 404 Not Found.

#### DELETE /api/v1/jogadores/{id}

Exclui um jogador e todo o seu histórico de desempenho.

-   Autenticação: Requerida (Bearer Token).
-   Resposta de Sucesso (204 No Content).
-   Resposta de Erro: 404 Not Found.

------------------------------------------------------------------------

### Treinos

#### POST /api/v1/treinos

Cria uma nova sessão de treino.

``` json
{
  "data": "2025-09-07",
  "focoDoTreino": "Arremessos de 3 pontos e transição defensiva",
  "observacoes": "Equipe demonstrou bom aproveitamento, mas precisa melhorar a comunicação na defesa."
}
```

-   Resposta de Sucesso (201 Created): Objeto do treino criado.

#### GET /api/v1/treinos

Lista todas as sessões de treino cadastradas.

-   Resposta de Sucesso (200 OK): Array de treinos.

------------------------------------------------------------------------

### Desempenho (Performance)

#### POST /api/v1/treinos/{treinoId}/jogadores/{jogadorId}/desempenho

Lança o desempenho de um jogador em um treino.

-   Parâmetros:
    -   `treinoId` (UUID)
    -   `jogadorId` (UUID)
-   Corpo da Requisição:

``` json
{
  "pontos": 22,
  "assistencias": 5,
  "rebotes": 9,
  "roubosDeBola": 2,
  "tocos": 3,
  "errosDePasse": 4,
  "arremessosTentados": 18,
  "arremessosConvertidos": 9,
  "feedbackDoTreinador": "Excelente presença no garrafão. Precisa trabalhar na tomada de decisão sob pressão."
}
```

-   Resposta de Sucesso (201 Created): Objeto de desempenho criado.
-   Ação: Recalcula atributos técnicos e mentais do jogador.

------------------------------------------------------------------------

## Regras de Negócio

-   **Cálculo da Idade:** Feito dinamicamente a partir da
    dataNascimento.
-   **Agregação de Atributos:** Sempre recalculados após lançamentos de
    desempenho.
-   **Expiração de Tokens:** 1 hora.
-   **Logout:** Invalida permanentemente o token usado.

------------------------------------------------------------------------

## Códigos de Status HTTP Comuns

Código   Significado             Descrição
  -------- ----------------------- --------------------------------
200      OK                      Requisição bem-sucedida.
201      Created                 Recurso criado com sucesso.
204      No Content              Sucesso sem corpo de resposta.
400      Bad Request             Requisição inválida.
401      Unauthorized            Token inválido ou ausente.
403      Forbidden               Sem permissão para o recurso.
404      Not Found               Recurso não encontrado.
500      Internal Server Error   Erro inesperado no servidor.
