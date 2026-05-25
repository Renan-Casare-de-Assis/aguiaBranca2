-- Inserção de Usuários de Teste (Senha '123456' para todos)
INSERT INTO USUARIOS (ID, NOME, EMAIL, SENHA, PERFIL, UNIDADE)
VALUES ('user_op_1', 'Renan Operador', 'operador@aguiabranca.com.br', '123456', 'OPERADOR', 'Vitoria - ES');

INSERT INTO USUARIOS (ID, NOME, EMAIL, SENHA, PERFIL, UNIDADE)
VALUES ('user_ge_1', 'Renan Gestor', 'gestor@aguiabranca.com.br', '123456', 'GESTOR', 'Vitoria - ES');

INSERT INTO USUARIOS (ID, NOME, EMAIL, SENHA, PERFIL, UNIDADE)
VALUES ('user_li_1', 'Renan Lider', 'lider@aguiabranca.com.br', '123456', 'LIDERANCA', 'Corporativo');

-- Inserção de Diretrizes
INSERT INTO DIRETRIZES (ID, TITULO, DESCRICAO, PILAR, STATUS, CRIADO_POR)
VALUES ('guid_1', 'Foco em Eficiência Operacional', 'Reduzir desperdício em todas as áreas de manutenção.', 'EFICIENCIA', 'ATIVO', 'user_li_1');

INSERT INTO DIRETRIZES (ID, TITULO, DESCRICAO, PILAR, STATUS, CRIADO_POR)
VALUES ('guid_2', 'Inovação em Logística 4.0', 'Implementar tecnologias de rastreio em tempo real.', 'TECNOLOGIA', 'ATIVO', 'user_li_1');

-- Inserção de Ideias
INSERT INTO IDEIAS (ID, TITULO, DESCRICAO, CATEGORIA, OPERADOR_ID, OPERADOR_NOME, UNIDADE, STATUS, PRIORIDADE)
VALUES ('idea_1', 'Reciclagem de Pneus', 'Sistema para reaproveitamento de pneus velhos na pavimentação.', 'SUSTENTABILIDADE', 'user_op_1', 'Renan Operador', 'Vitoria - ES', 'APROVADA', 'ALTA');

INSERT INTO IDEIAS (ID, TITULO, DESCRICAO, CATEGORIA, OPERADOR_ID, OPERADOR_NOME, UNIDADE, STATUS, PRIORIDADE)
VALUES ('idea_2', 'App de Escala Inteligente', 'Algoritmo para otimizar a escala de motoristas.', 'PRODUTIVIDADE', 'user_op_1', 'Renan Operador', 'Vitoria - ES', 'NOVA', 'MEDIA');

-- Inserção de Projetos
INSERT INTO PROJETOS (ID, IDEIA_ID, TITULO, OBJETIVO, ETAPA, STATUS, GERENTE_ID, DATA_INICIO, DATA_FIM_META, INVESTIMENTO, RETORNO_FINANCEIRO, REDUCAO_CUSTO, GANHO_PRODUTIVIDADE_PCT, LUCRO, ROI, PROGRESSO_PCT)
VALUES ('proj_1', 'idea_1', 'Projeto Recicla Pneus', 'Implementar usina de moagem de pneus em Cariacica.', 'EXECUCAO', 'ON_TRACK', 'user_ge_1', SYSTIMESTAMP, SYSTIMESTAMP, 500000.00, 1200000.00, 200000.00, 15.0, 700000.00, 140.0, 45);

-- Métrica Global (Opcional, mas ajuda o Dashboard a carregar rápido)
INSERT INTO METRICAS_PAINEL (ID, TOTAL_IDEIAS, IDEIAS_APROVADAS, PROJETOS_ATIVOS, INVESTIMENTO_TOTAL, RETORNO_TOTAL, LUCRO_TOTAL, REDUCAO_CUSTO_TOTAL, MEDIA_GANHO_PRODUTIVIDADE, ROI_GLOBAL)
VALUES ('metrics_2026_05', 2, 1, 1, 500000.00, 1200000.00, 700000.00, 200000.00, 15.0, 140.0);
