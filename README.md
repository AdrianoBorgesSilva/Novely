# Novely
Uma API RESTful robusta para gerenciamento de uma plataforma online de novels, construída com Spring Boot, persistência em MongoDB e segurança baseada em JWT, que oferece cadastro/login de usuários, operações CRUD para novels, capítulos, comentários e avaliações, além de recursos de “favoritar” e “super‑favoritar”, seguindo princípios de arquitetura em camadas — com DTOs para requisições/respostas, entidades anotadas para MongoDB, camada de serviços e repositórios, e tratamento centralizado de exceções — e preparada para fácil extensão ou implantação via Docker.

## Funcionalidades

👤 **Gerenciamento de usuários:** Cadastro, login, atualização de perfil e controle de acesso baseado em função (JWT).

📚 **CRUD de novels:** Criar, ler, atualizar e excluir novels; rastrear visualizações, favoritos e super-favoritos.

📑 **Gerenciamento de capítulos:** Adicionar, editar e listar capítulos com marcação de tempo.

💬 **Comentários e avaliações:** Usuários podem comentar e avaliar novels.

🔐 **Segurança:** Spring Security com filtro de JWT, UserDetailsService customizado e hashing de senhas.

✔️ **Validação:** Bean Validation com Hibernate Validator e Jakarta Validation API.

🐳 **Suporte a Docker:** Contêinerização através do Dockerfile incluso.

## Link Para Testes Online
🔗 **https://novely.onrender.com**

Use Postman ou qualquer outro ambiente de testes de sua preferência.

## Endpoints Principais
### Cadastrar Usuário
**Método:** `POST`

**Endpoint:** `api/users/auth/signup`

**Corpo:** ``{"name": "Teste", "email": "teste@gmail.com", "password": "123456"}``

### Login
**Método:** `POST`

**Endpoint:** `api/users/auth/login`

**Corpo:** `{"email": "teste@gmail.com", "password": "123456"}`

### Criar Novel (Autenticado)
**Método:** `POST`

**Endpoint:** `api/novels`

**Corpo:** `{"title": "Teste", "description": "Apenas um teste", "genre": "FANTASY"}`

### Criar Capítulo (Autenticado)
**Método:** `POST`

**Endpoint:** `api/novels/{novelId}/chapters`

**Corpo:** `{"title": "Teste", "content": "Testando e testando...", "chapterNumber": 1}`

### Criar Comentário (Autenticado)
**Método:** `POST`

**Endpoint:** `api/novels/{novelId}/comments`

**Corpo:** `{ "content": "Isso é apenas um teste"}`

### Criar Avaliação (Autenticado)
**Método:** `POST`

**Endpoint:** `api/novels/{novelId}/ratings`

**Corpo:** `{ "rate": 5 }`

## Tratamento de Erros
O tratamento de exceções é centralizado na camada exception usando um ResourceExceptionHandler para tratamento global, garantindo respostas claras e status HTTP apropriados.

## Licença
Este projeto é licenciado sob a MIT License. *Sugestões são bem vindas*

***

*Às vezes, as pessoas que ninguém imagina que possam fazer algo, são as que fazem coisas que ninguém imagina -* **Alan Turing**


