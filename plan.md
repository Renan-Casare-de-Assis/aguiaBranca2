# Plano de Implementação - Sprint 1 (Challenge Águia Branca)

## 1) Contexto do projeto (resumo executivo)
- Projeto de faculdade com foco em entrega funcional rápida.
- Escopo fechado em Sprint 1.
- Conectividade externa escolhida: somente API REST mockada.
- Sem Firebase.
- Stack alvo do SPEC: Android nativo em Kotlin, Jetpack Compose, MVVM + Clean Architecture, Retrofit.
- Objetivo prático: implementar o obrigatório com baixo risco, usando prompts simples para IA menor.
- ✅ **Mockups HTML completos**: 11 telas finalizadas em `documentos/ui-mockups/` como referência visual.

## 1.1) Mockups HTML finalizados (base visual para Compose)

✅ **11 telas HTML prontas** com design system completo (`styles.css`):

**Autenticação (1 tela):**
- 01 - Login

**Operador (4 telas):**
- 02 - Home
- 03 - Nova Ideia
- 04 - Minhas Ideias
- 09 - Orientações (leitura)

**Gestor (3 telas):**
- 05 - Curadoria
- 06 - Projetos
- 10 - Orientações (leitura)

**Liderança (3 telas):**
- 07 - Orientações CRUD
- 08 - Dashboard
- 11 - Portfólio

Todos com melhorias de designer sênior: touch targets 48px, white space otimizado, typography scale profissional, WCAG 2.1 compliance.

## 2) Escopo obrigatório da Sprint 1
- Autenticação com 3 perfis: Operador, Gestor, Liderança.
- Orientações estratégicas:
  - Leitura para todos
  - CRUD apenas para Liderança
- Ideias:
  - Operador cria e vê próprias ideias
  - Gestor vê todas, prioriza e aprova
- Projetos:
  - Gestor cria/atualiza
  - Liderança consulta
- Dashboard:
  - Liderança visualiza KPIs principais
- Evidência de integração HTTP real:
  - GET, POST e PUT/PATCH funcionando em API mockada externa

## 3) Fases numeradas (ordem real de execução)

## Fase 0 - Preparação rápida e definição de corte
### Objetivo da fase
Garantir base de execução em 1 dia, com escopo mínimo viável já fechado.

### Tarefas práticas (checklist)
- [ ] Confirmar equipe, dispositivo Android e ambiente de build.
- [ ] Fechar lista de telas obrigatórias da Sprint 1.
- [ ] Definir corte de escopo antes de codar.
- [ ] Definir contas mock dos 3 perfis.
- [ ] Criar checklist de evidências para banca.

### Prompt simples para IA menor
Quero um checklist mínimo para Sprint 1 de app Android Kotlin com 3 perfis e API mockada. Liste somente o obrigatório para entregar em 1 dia, sem Firebase e sem funcionalidades extras.

### Mini tutorial (manual)
1. Abra o SPEC e marque só os itens obrigatórios.
2. Crie uma lista de telas mínimas por perfil.
3. Defina o que será cortado se faltar tempo.
4. Valide se todos concordam com o corte.
5. Não comece codando sem essa decisão.

### Critério de pronto (DoD)
- Escopo obrigatório fechado em uma página.
- Corte de escopo documentado.
- Tarefas do dia priorizadas.

---

## Fase 1 - API mockada externa e contrato estável
### Objetivo da fase
Subir a API mockada com endpoints e dados iniciais compatíveis com o SPEC.

### Tarefas práticas (checklist)
- [ ] Criar recursos: users, guidelines, ideas, projects, dashboard_summary.
- [ ] Popular usuários de teste (operator, manager, leadership).
- [ ] Validar endpoints mínimos:
  - [ ] POST auth/login
  - [ ] GET/POST/PUT/DELETE guidelines
  - [ ] GET/POST/PATCH ideas
  - [ ] GET/POST/PUT projects
  - [ ] GET dashboard/summary
- [ ] Testar tudo com cliente HTTP e salvar prints.
- [ ] Congelar nomes de campos para evitar quebra de DTO.

### Prompt simples para IA menor
Gere um passo a passo para configurar uma API mockada com recursos users, guidelines, ideas, projects e dashboard_summary, com exemplos de payload para GET, POST e PUT/PATCH, seguindo app de inovação corporativa com perfis operator, manager e leadership.

### Mini tutorial (manual)
1. Crie projeto no provedor de mock API.
2. Crie cada recurso com os campos do SPEC.
3. Insira pelo menos 5 registros por recurso.
4. Teste todos os métodos HTTP obrigatórios.
5. Salve URLs finais e exemplos de request/response.

### Critério de pronto (DoD)
- Todos os endpoints respondem fora do app.
- Campos batem com o SPEC.
- Evidências salvas (prints de requisições e respostas).

---

## Fase 2 - Esqueleto Android + Retrofit funcional
### Objetivo da fase
Construir base técnica mínima para chamadas HTTP reais em app Android.

### Tarefas práticas (checklist)
- [ ] Criar projeto Android Kotlin com Compose.
- [ ] Configurar arquitetura em camadas (presentation, domain, data).
- [ ] Adicionar Retrofit, OkHttp e serialização.
- [ ] Criar cliente HTTP único com timeout e log.
- [ ] Criar DTOs iniciais para auth e ideas.
- [ ] Implementar primeiro fluxo fim a fim: login real.

### Prompt simples para IA menor
Crie checklist de configuração mínima de Retrofit em Android Kotlin com Compose, incluindo dependências, cliente HTTP, interface de API e serialização JSON. Quero algo simples, sem Firebase, focado em API mockada.

### Mini tutorial (manual)
1. Configure dependências de rede no build.
2. Crie serviço Retrofit com base URL do mock.
3. Crie modelos de request/response de login.
4. Faça chamada POST de login em botão de teste.
5. Valide resposta no Logcat e na UI.

### Critério de pronto (DoD)
- App abre e realiza login via HTTP real.
- Sem crash de serialização.
- Log de request/response visível no ambiente de desenvolvimento.

---

## Fase 3 - Autenticação e roteamento por perfil
### Objetivo da fase
Entrar no app e redirecionar para home correta de cada perfil.

### Tarefas práticas (checklist)
- [ ] Implementar tela de login com validação simples.
- [ ] Mapear role retornada da API para navegação.
- [ ] Criar home de Operador, Gestor e Liderança.
- [ ] Implementar logout limpando sessão local.
- [ ] Bloquear acesso sem sessão válida.

### Prompt simples para IA menor
Preciso de fluxo de autenticação Android Kotlin com 3 perfis e navegação por role. Após login: operator vai para Home Operador, manager para Home Gestor, leadership para Home Liderança. Inclua logout e proteção de rota.

### Mini tutorial (manual)
1. Receba o role da resposta do login.
2. Salve sessão local mínima.
3. Na inicialização, verifique sessão.
4. Redirecione para home do perfil.
5. No logout, limpe sessão e volte para login.

### Critério de pronto (DoD)
- 3 usuários de teste entram em homes diferentes.
- Logout funciona.
- Usuário sem login não acessa telas protegidas.

---

## Fase 4 - Módulo de Ideias (núcleo do fluxo)
### Objetivo da fase
Entregar captura e curadoria de ideias com regras de perfil.

### Tarefas práticas (checklist)
- [ ] Operador cria ideia (POST).
- [ ] Operador lista apenas próprias ideias (GET com filtro por operatorId).
- [ ] Gestor lista todas as ideias.
- [ ] Gestor prioriza e aprova (PATCH).
- [ ] Validar campos obrigatórios com feedback inline.

### Prompt simples para IA menor
Monte um plano de implementação do módulo de ideias em Android Kotlin com API mockada: operador cria e vê próprias ideias; gestor vê todas, prioriza e aprova com PATCH. Inclua validações simples de formulário e mensagens de erro.

### Mini tutorial (manual)
1. Crie formulário de ideia com título, descrição e categoria.
2. Envie POST com status inicial NEW.
3. Na listagem do operador, filtre por autor.
4. Na listagem do gestor, carregue tudo.
5. Aplique PATCH para prioridade e aprovação.

### Critério de pronto (DoD)
- Operador cria ideia e vê status.
- Gestor muda prioridade e aprova.
- Fluxo completo ideia criada até aprovada funcionando.

---

## Fase 5 - Módulo de Orientações Estratégicas
### Objetivo da fase
Implementar leitura para todos e CRUD exclusivo da Liderança.

### Tarefas práticas (checklist)
- [ ] Tela de listagem visível para todos os perfis.
- [ ] Criar/editar/excluir apenas para Liderança.
- [ ] Exigir confirmação em exclusão.
- [ ] Exibir erro de permissão para perfil sem acesso de escrita.

### Prompt simples para IA menor
Quero implementar orientações estratégicas com regra de perfil: todos leem, só leadership faz CRUD. Me dê passos simples para tela de lista + formulário + delete com confirmação, usando API mockada.

### Mini tutorial (manual)
1. Carregue orientações em tela comum.
2. Mostre botões de CRUD só para liderança.
3. No submit, chame POST ou PUT conforme contexto.
4. No delete, peça confirmação e execute DELETE.
5. Atualize lista após sucesso.

### Critério de pronto (DoD)
- Operador e Gestor só conseguem ler.
- Liderança realiza CRUD completo sem erro.
- Exclusão sempre pede confirmação.

---

## Fase 6 - Módulo de Projetos
### Objetivo da fase
Permitir gestão de projetos pelo Gestor e leitura pela Liderança.

### Tarefas práticas (checklist)
- [ ] Gestor cria projeto (POST).
- [ ] Gestor atualiza estágio, status, investimento e retorno (PUT).
- [ ] Calcular ROI no app para exibição.
- [ ] Liderança consulta projetos sem editar.
- [ ] Tratar estados de loading, vazio e erro.

### Prompt simples para IA menor
Preciso do módulo de projetos em Android Kotlin com API mockada: manager cria e atualiza projeto; leadership apenas consulta. Inclua cálculo de ROI na tela e organização simples de estado de UI.

### Mini tutorial (manual)
1. Crie formulário mínimo de projeto.
2. Envie POST para criação.
3. Em edição, carregue dados e envie PUT.
4. Calcule ROI na UI com investimento e retorno.
5. Bloqueie botões de edição para liderança.

### Critério de pronto (DoD)
- Gestor cria e atualiza projeto.
- Liderança consulta dados do projeto.
- ROI aparece corretamente na interface.

---

## Fase 7 - Dashboard da Liderança
### Objetivo da fase
Exibir indicadores principais vindos da API mockada.

### Tarefas práticas (checklist)
- [ ] Buscar resumo com GET dashboard/summary.
- [ ] Exibir ROI global, investimento, retorno, lucro e produtividade.
- [ ] Exibir estado vazio quando não houver dados.
- [ ] Bloquear rota para não liderança.

### Prompt simples para IA menor
Crie um plano simples para tela de dashboard de liderança com dados vindos de endpoint de resumo. Mostrar ROI, investimento, retorno, lucro e produtividade, com estados loading, vazio e erro.

### Mini tutorial (manual)
1. Faça GET do resumo ao abrir a tela.
2. Mapeie resposta para cards de KPI.
3. Trate ausência de dados com mensagem orientativa.
4. Trate erro de rede com ação de tentar novamente.
5. Garanta acesso só para liderança.

### Critério de pronto (DoD)
- Liderança visualiza KPIs principais da API.
- Usuário sem permissão não acessa.
- Estados de tela cobertos.

---

## Fase 8 - Qualidade mínima, build e evidências
### Objetivo da fase
Fechar entrega com estabilidade, APK e materiais de banca.

### Tarefas práticas (checklist)
- [ ] Testes manuais críticos por perfil.
- [ ] Validar fluxos HTTP: GET, POST, PUT/PATCH.
- [ ] Corrigir crashes e erros de navegação/import.
- [ ] Gerar APK instalável.
- [ ] Gravar vídeo de até 5 minutos.
- [ ] Organizar evidências e documentação.

### Prompt simples para IA menor
Me ajude a montar um checklist final de validação para Sprint 1 Android: testes manuais por perfil, validação de chamadas GET/POST/PUT/PATCH, geração de APK e roteiro de vídeo de 5 minutos.

### Mini tutorial (manual)
1. Execute roteiro de testes por perfil.
2. Registre prints de chamadas HTTP no app e no painel do mock.
3. Gere APK release e instale em dispositivo real.
4. Grave vídeo com fluxo completo.
5. Monte pasta final de entrega.

### Critério de pronto (DoD)
- APK instala e executa.
- Funcionalidades obrigatórias funcionam.
- Evidências e vídeo prontos para submissão.

---

## 4) Prompts anti-erro (copiar e colar quando quebrar)

## Erro de build Gradle
Você é um assistente de correção de build Android Kotlin. Vou colar o erro do Gradle. Responda em 3 partes: causa raiz provável, correção mínima exata, e checklist de validação pós-correção. Não proponha refatoração grande, só o necessário para compilar.

## Erro de imports e referências
Estou com erro de import no Android Kotlin. Analise o erro e proponha a menor correção possível, mantendo arquitetura atual. Liste dependências faltantes, imports corretos e possíveis conflitos de versão.

## Erro de navegação entre telas
Minha navegação Compose está falhando. Com base no erro, proponha ajuste direto de rotas, argumentos e destino inicial por perfil. Quero solução objetiva para restaurar login, redirecionamento e logout.

## Erro de serialização JSON
Estou com erro de serialização Retrofit. Compare meu DTO com o JSON real da API e me devolva o mapeamento correto campo a campo, indicando tipos, nulabilidade e nomes divergentes.

## Erro de DTO e contrato
Meu DTO não bate com o contrato REST mockado. Gere versão corrigida do contrato de dados para request e response, sem inventar campos, respeitando o que vem da API.

## Erro de chamadas HTTP
Meu app não persiste dados no mock. Diagnostique por ordem: URL, método HTTP, headers, body e parse da resposta. No final, dê uma sequência de testes rápidos para confirmar GET, POST e PUT/PATCH.

---

## 5) Prioridade de corte (se faltar tempo)

## Não cortar (obrigatório para entrega)
- Login com 3 perfis e roteamento.
- Ideias: criação por operador + aprovação por gestor.
- Orientações: leitura para todos + CRUD liderança.
- Projetos: criação/edição gestor + leitura liderança.
- Dashboard liderança com KPIs básicos.
- Evidência real de GET, POST e PUT/PATCH.
- APK + vídeo + documentação mínima.

## Cortar primeiro (baixo impacto na nota funcional)
- Polimento visual avançado.
- Animações e microinterações.
- Filtros avançados no dashboard.
- Módulo de auditoria completo.
- Otimizações de performance além do essencial.

## Cortar por último (somente se crítico)
- Cache offline complexo.
- Testes automatizados amplos.
- Funcionalidades extras de engajamento fora do obrigatório.

---

## 6) Cronograma de 1 dia (blocos de 60-90 min)

## Bloco 1 (90 min)
- Fase 0 completa
- Fase 1 até endpoints validados

## Bloco 2 (90 min)
- Fase 2 completa
- Primeiro login HTTP funcionando

## Bloco 3 (90 min)
- Fase 3 completa
- Início da Fase 4 (criação de ideia)

## Bloco 4 (90 min)
- Fase 4 completa
- Fase 5 iniciada

## Bloco 5 (90 min)
- Fase 5 completa
- Fase 6 completa (mínimo viável)

## Bloco 6 (60 min)
- Fase 7 completa (dashboard básico)

## Bloco 7 (60 min)
- Fase 8: testes críticos e correções rápidas

## Bloco 8 (60 min)
- APK release
- Captura de evidências
- Gravação do vídeo

---

## 7) Entregáveis finais para a banca

- APK Android instalável e testado em dispositivo real.
- Código-fonte completo compactado em ZIP.
- Documentação técnica em PDF ou PPT contendo:
  - Tecnologias usadas
  - Arquitetura
  - Fluxo por perfil
  - Decisão de conectividade (somente API REST mockada)
- Vídeo demonstrativo de até 5 minutos com:
  - Login por perfil
  - Fluxo Operador, Gestor e Liderança
  - Dashboard
  - Prova de integração HTTP real
- Evidências obrigatórias de HTTP:
  - Print de GET funcionando no app e no mock
  - Print de POST criando registro no mock
  - Print de PUT ou PATCH atualizando registro no mock
- Evidências de autorização por perfil:
  - Operador sem acesso de escrita em orientações
  - Liderança sem edição em projetos
  - Gestor com curadoria de ideias funcionando

---

## 8) Checklist final de aceite da Sprint 1
- [ ] Sem Firebase em nenhuma parte da solução.
- [ ] Integração externa efetiva somente com API REST mockada.
- [ ] Funcionalidades obrigatórias implementadas por perfil.
- [ ] Navegação e sessão sem quebra.
- [ ] GET, POST e PUT/PATCH comprovados com evidência.
- [ ] APK gerado e instalado.
- [ ] Vídeo e documentação prontos para submissão.
