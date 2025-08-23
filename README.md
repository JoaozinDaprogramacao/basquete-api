# Documentação da API de Basquete

Bem-vindo à documentação oficial da API de Basquete. Esta API permite o gerenciamento de informações relacionadas a basquete e utiliza um sistema de autenticação moderno baseado em OAuth2 e JWT.

**URL Base da API:**
-   **Desenvolvimento:** `http://localhost:8080`
-   **Produção:** `https://api.seudominio.com.br`

## Autenticação

A API utiliza um fluxo de autenticação stateless com **Bearer Tokens (JWT)**. Para obter um token, o consumidor da API deve guiar o usuário através de um fluxo de login com o Google.

### Fluxo de Obtenção de Token

1.  **Início do Login:** O cliente (seu app frontend/mobile) deve redirecionar o usuário para o endpoint de login da nossa API:
    `GET /login`
2.  **Autorização do Google:** O usuário será apresentado à tela de login do Google. Após autorizar a aplicação, ele será redirecionado de volta para o backend.
3.  **Redirecionamento com Token:** O backend irá processar a autenticação, gerar um JWT e redirecionar o usuário para a URL do frontend configurada (ex: `https://seufrontend.com/login/callback`) com o token anexado como um parâmetro de consulta (`query param`):
    `https://seufrontend.com/login/callback?token=eyJhbGciOiJIUzI1NiJ9...`
4.  **Uso do Token:** Seu cliente deve extrair este token da URL, armazená-lo de forma segura e enviá-lo em todas as futuras requisições para endpoints protegidos no cabeçalho `Authorization`.

### Formato do Cabeçalho de Autorização

Para todas as chamadas a endpoints que requerem autenticação, inclua o seguinte cabeçalho:

```http
Authorization: Bearer <seu_jwt_token>
```

---

## Endpoints da API

### Autenticação

---

#### `GET /login`

Inicia o fluxo de autenticação com o Google. Este endpoint deve ser aberto no navegador do usuário.

-   **Autenticação:** Não requerida.
-   **Ação:** Redireciona o usuário para a página de login do Google.

---

#### `POST /logout`

Invalida o token de acesso atual do usuário.

-   **Autenticação:** Requerida (Bearer Token).
-   **Descrição:** O token enviado no cabeçalho `Authorization` será adicionado a uma "blocklist" e não poderá mais ser usado.
-   **Resposta de Sucesso:**
    -   **Código:** `200 OK`
    -   **Corpo:** `Logout bem-sucedido.`

---

### Usuários

---

#### `GET /api/v1/user/me`

Obtém as informações do perfil do usuário atualmente autenticado.

-   **Autenticação:** Requerida (Bearer Token).
-   **Resposta de Sucesso:**
    -   **Código:** `200 OK`
    -   **Corpo:**
        ```json
        {
          "id": "10987654321...",
          "nome": "LeBron James",
          "email": "lebron.james@example.com",
          "fotoUrl": "[https://lh3.googleusercontent.com/a/exemplo](https://lh3.googleusercontent.com/a/exemplo)..."
        }
        ```

---
### Jogadores

Endpoints para gerenciar os jogadores cadastrados. **Todos requerem autenticação.**

---

#### `POST /api/v1/jogadores`

Cria um novo jogador.

-   **Autenticação:** Requerida (Bearer Token).
-   **Corpo da Requisição:** `application/json`
    ```json
    {
      "nome": "Stephen Curry",
      "anoNascimento": 2011,
      "nomeResponsavel": "Dell Curry",
      "contatoResponsavel": "(38)998765432",
      "rgEntregue": true
    }
    ```
-   **Regras de Validação:**
    -   `nome`: Obrigatório, entre 3 e 100 caracteres.
    -   `anoNascimento`: Obrigatório, entre 1990 e o ano atual.
    -   `nomeResponsavel`: Obrigatório, entre 3 e 100 caracteres.
    -   `contatoResponsavel`: Obrigatório, no formato `(XX)XXXXXXXXX`.
    -   `rgEntregue`: Obrigatório (`true` ou `false`).
-   **Resposta de Sucesso:**
    -   **Código:** `201 Created`
    -   **Cabeçalho:** `Location: http://localhost:8080/api/v1/jogadores/{uuid-gerado}`
    -   **Corpo:**
        ```json
        {
          "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
          "nome": "Stephen Curry",
          "idade": 14,
          "nomeResponsavel": "Dell Curry",
          "contatoResponsavel": "(38)998765432",
          "rgEntregue": true
        }
        ```
-   **Resposta de Erro (Validação):**
    -   **Código:** `400 Bad Request`
    -   **Corpo:**
        ```json
        {
          "contatoResponsavel": "O formato do contato deve ser (XX)XXXXXXXXX.",
          "nome": "O nome do jogador não pode ser vazio."
        }
        ```

---

#### `GET /api/v1/jogadores`

Lista todos os jogadores cadastrados.

-   **Autenticação:** Requerida (Bearer Token).
-   **Resposta de Sucesso:**
    -   **Código:** `200 OK`
    -   **Corpo:**
        ```json
        [
          {
            "id": "a1b2c3d4-e5f6-7890-1234-567890abcdef",
            "nome": "Stephen Curry",
            "idade": 14,
            "nomeResponsavel": "Dell Curry",
            "contatoResponsavel": "(38)998765432",
            "rgEntregue": true
          },
          {
            "id": "b2c3d4e5-f6a7-8901-2345-67890abcdef1",
            "nome": "Kobe Bryant",
            "idade": 15,
            "nomeResponsavel": "Joe Bryant",
            "contatoResponsavel": "(38)911112222",
            "rgEntregue": false
          }
        ]
        ```

---

#### `GET /api/v1/jogadores/{id}`

Busca um jogador específico pelo seu ID (UUID).

-   **Autenticação:** Requerida (Bearer Token).
-   **Parâmetro de URL:**
    -   `id` (UUID): O identificador único do jogador.
-   **Resposta de Sucesso:**
    -   **Código:** `200 OK`
    -   **Corpo:** (Mesmo formato do `POST`)
-   **Resposta de Erro:**
    -   **Código:** `404 Not Found` se o jogador com o ID informado não existir.

---

#### `PUT /api/v1/jogadores/{id}`

Atualiza os dados de um jogador existente. O `anoNascimento` não pode ser alterado.

-   **Autenticação:** Requerida (Bearer Token).
-   **Parâmetro de URL:**
    -   `id` (UUID): O identificador do jogador a ser atualizado.
-   **Corpo da Requisição:** `application/json` (mesmas validações do `POST`, exceto `anoNascimento`).
    ```json
    {
      "nome": "Stephen 'Wardell' Curry",
      "nomeResponsavel": "Dell Curry",
      "contatoResponsavel": "(38)998765432",
      "rgEntregue": true
    }
    ```
-   **Resposta de Sucesso:**
    -   **Código:** `200 OK`
    -   **Corpo:** O objeto do jogador com os dados atualizados.
-   **Respostas de Erro:**
    -   `400 Bad Request` para erros de validação.
    -   `404 Not Found` se o jogador não for encontrado.

---

#### `DELETE /api/v1/jogadores/{id}`

Exclui um jogador do sistema.

-   **Autenticação:** Requerida (Bearer Token).
-   **Parâmetro de URL:**
    -   `id` (UUID): O identificador do jogador a ser excluído.
-   **Resposta de Sucesso:**
    -   **Código:** `204 No Content`
    -   **Corpo:** Vazio.
-   **Resposta de Erro:**
    -   **Código:** `404 Not Found` se o jogador não for encontrado.

---

## Regras de Negócio

-   **Cálculo da Idade:** A idade do jogador é calculada e salva no banco de dados com base no `anoNascimento` fornecido no momento do cadastro. Ela não é atualizada automaticamente, apenas no momento da criação.
-   **Expiração de Tokens:** Os tokens JWT têm um tempo de vida limitado (atualmente configurado para **1 hora**).
-   **Logout e Invalidação:** Uma vez que o endpoint `POST /logout` é chamado com um token, ele se torna permanentemente inválido.

## Códigos de Status HTTP Comuns

| Código | Significado           | Descrição                                                                                               |
| :----- | :-------------------- | :------------------------------------------------------------------------------------------------------ |
| `200`  | **OK** | A requisição foi bem-sucedida.                                                                          |
| `201`  | **Created** | O recurso foi criado com sucesso.                                           |
| `204`  | **No Content** | A requisição foi bem-sucedida, mas não há conteúdo para retornar (ex: DELETE).        |
| `400`  | **Bad Request** | A requisição é inválida (ex: campos faltando, formato incorreto).        |
| `401`  | **Unauthorized** | O cliente não está autenticado (token JWT inválido, expirado ou faltando).       |
| `403`  | **Forbidden** | O cliente está autenticado, mas não tem permissão para o recurso.                     |
| `404`  | **Not Found** | O recurso solicitado (ex: jogador com um ID específico) não foi encontrado.                                                    |
| `500`  | **Internal Server Error** | Ocorreu um erro inesperado no servidor.                                                               |