# Delta Force Loadouts - Backend

## 📌 Sobre o projeto

API REST desenvolvida em Spring Boot para gerenciamento de builds de armas do jogo Delta Force Mobile.

O sistema permite cadastro, consulta e interação com builds, além de funcionalidades administrativas para gerenciamento de usuários, armas e operadores.

---

## 🚀 Tecnologias utilizadas

- Java
- Spring Boot
- Spring Security
- JWT (autenticação)
- Spring Data JPA
- MySQL / PostgreSQL
- Lombok

---

## 🔐 Segurança

- Autenticação via JWT
- Controle de acesso por roles (ADMIN / USUARIO)
- Proteção de rotas no backend
- Bloqueio temporário de login por excesso de tentativas
- Tratamento global de exceções

---

## ⚙️ Funcionalidades principais

- CRUD de builds
- Sistema de likes e dislikes
- Sistema de favoritos
- Filtros, busca e paginação
- Controle de permissões (usuário vs admin)
- Gestão de usuários (bloqueio/desbloqueio)
- Gestão de armas e operadores

---

## 📊 Regras de negócio

- Apenas o dono pode editar a própria build
- Admin pode remover qualquer build
- Likes/dislikes são únicos por usuário
- Código da build é único
- Usuários bloqueados não podem autenticar

---

## 🧩 Estrutura do projeto

- controller → endpoints REST
- service → regras de negócio
- repository → acesso ao banco
- entity → entidades JPA
- dto → transferência de dados
- security → autenticação e autorização
- exception → tratamento global de erros

---

## ▶️ Como rodar o projeto

1. Configurar banco de dados
2. Ajustar `application.properties`
3. Configurar variáveis de ambiente (JWT, DB)
4. Rodar aplicação

---

## 💡 Observações

Este projeto foi desenvolvido com foco em prática real de:
- autenticação e autorização
- organização em camadas
- regras de negócio
- tratamento de erros
