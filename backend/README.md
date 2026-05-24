# Backend API (Oracle)

API Spring Boot para o app Android `aguiaBranca2`.

## Requisitos
- Java 17+
- Acesso Oracle

## Variáveis de ambiente
- `ORACLE_URL` (ex: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`)
- `ORACLE_USER`
- `ORACLE_PASS`
- `BACKEND_PORT` (opcional, default `8080`)

## Como rodar

```powershell
cd C:\Users\renan\IdeaProjects\aguiaBranca2
.\gradlew.bat :backend:bootRun
```

## Teste smoke

```powershell
cd C:\Users\renan\IdeaProjects\aguiaBranca2
.\gradlew.bat :backend:test
```

## Endpoint de saúde
- `GET /api/health`

## Endpoints principais

### Users
- `POST /api/users/login`
- `GET /api/users/{id}`
- `GET /api/users`

### Projects
- `POST /api/projects`
- `GET /api/projects`
- `GET /api/projects/{id}`
- `PUT /api/projects/{id}`
- `DELETE /api/projects/{id}`

### Ideas
- `POST /api/ideas`
- `GET /api/ideas?operatorId={id}`
- `GET /api/ideas/{id}`
- `PATCH /api/ideas/{id}/status`
- `PATCH /api/ideas/{id}/priority`
- `DELETE /api/ideas/{id}`

### Guidelines
- `POST /api/guidelines`
- `GET /api/guidelines`
- `GET /api/guidelines/{id}`
- `PUT /api/guidelines/{id}`
- `DELETE /api/guidelines/{id}`

### Dashboard
- `GET /api/dashboard/metrics`

## Observações
- CORS liberado para `/api/**` em desenvolvimento.
- Erros são retornados no formato:
  - `code`
  - `message`
  - `details`

