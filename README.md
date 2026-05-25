# 🦅 Projeto Inovação Águia Branca - Sprint 1

Sistema de gestão de inovação corporativa desenvolvido para o Grupo Águia Branca, conectando o fluxo operacional de captura de ideias à visão estratégica da liderança.

## 📱 A Solução
O aplicativo permite que colaboradores de todos os níveis participem do ecossistema de inovação:
- **Operadores**: Registram dores e ideias de forma intuitiva.
- **Gestores**: Realizam a curadoria, priorizam e transformam ideias em projetos acompanháveis.
- **Liderança**: Gerenciam diretrizes estratégicas e acompanham indicadores de performance (ROI, Lucro, Produtividade) via Dashboard.

## 🛠️ Stack Técnica
- **Android**: Kotlin, Jetpack Compose, MVVM, Clean Architecture, Hilt (DI), Retrofit (Networking), Room (Offline Cache).
- **Backend**: Spring Boot, Kotlin, Spring Data JDBC.
- **Banco de Dados**: Oracle Database (Infraestrutura FIAP).
- **Conectividade**: Consumo de API REST funcional.

## 🚀 Como Executar

### 1. Banco de Dados
Certifique-se de que as tabelas necessárias foram criadas no esquema Oracle. O script de criação está disponível em `backend/sql/README.sql`.

### 2. Servidor (Backend)
Na raiz do projeto, execute o comando para subir a API:
```powershell
./gradlew :backend:bootRun
```
O servidor estará disponível em `http://localhost:8080`.

### 3. Aplicativo Android
- Abra o projeto no **Android Studio**.
- Certifique-se de que o Backend está rodando.
- Execute o módulo `app` em um emulador ou dispositivo físico (API 26+).

## 🔐 Credenciais de Acesso (Teste)
| Perfil | E-mail | Senha |
| :--- | :--- | :--- |
| **Operador** | operador@aguiabranca.com.br | 123456 |
| **Gestor** | gestor@aguiabranca.com.br | 123456 |
| **Liderança** | lider@aguiabranca.com.br | 123456 |

---
*Projeto desenvolvido para o Challenge FIAP - Grupo Águia Branca.*
