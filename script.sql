DROP TABLE T_FIAP_MATCH_SF CASCADE CONSTRAINTS;
DROP TABLE T_FIAP_DOR_SF CASCADE CONSTRAINTS;
DROP TABLE T_FIAP_REUNIAO_SF CASCADE CONSTRAINTS;
DROP TABLE T_FIAP_ATENDENTE_SF CASCADE CONSTRAINTS;
DROP TABLE T_FIAP_CLIENTE_SF CASCADE CONSTRAINTS;

CREATE TABLE T_FIAP_CLIENTE_SF (
    ID_CLIENTE   NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOME         VARCHAR2(100) NOT NULL,
    EMPRESA      VARCHAR2(100) NOT NULL,
    SEGMENTO     VARCHAR2(50)  NOT NULL,
    ORCAMENTO    NUMBER(12,2)  DEFAULT 0
);

CREATE TABLE T_FIAP_ATENDENTE_SF (
    ID_ATENDENTE     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    NOME             VARCHAR2(100) NOT NULL,
    EMAIL            VARCHAR2(100),
    CARGO            VARCHAR2(80)  NOT NULL,
    COR_AVATAR       VARCHAR2(7),
    INICIAIS         VARCHAR2(3),
    TRACOS           VARCHAR2(200) NOT NULL,
    SEGMENTOS        VARCHAR2(200) NOT NULL,
    FOCO             VARCHAR2(50),
    BIO              VARCHAR2(400),
    ANOS_EXPERIENCIA NUMBER(2)     DEFAULT 0,
    NOTA_MEDIA       NUMBER(3,1)   DEFAULT 0,
    CASES_RESOLVIDOS NUMBER(5)     DEFAULT 0,
    DISPONIBILIDADE  VARCHAR2(20)  DEFAULT 'Disponível'
);

CREATE TABLE T_FIAP_REUNIAO_SF (
    ID_REUNIAO    NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ID_CLIENTE    NUMBER NOT NULL,
    DATA_REUNIAO  DATE DEFAULT SYSDATE,
    TRANSCRICAO   CLOB NOT NULL,
    NOTA_NPS      NUMBER(2),
    URGENCIA      VARCHAR2(10),
    SINAL_NEGOCIO VARCHAR2(30),
    STATUS        VARCHAR2(20) DEFAULT 'ABERTA',
    CONSTRAINT FK_REUNIAO_CLIENTE_SF FOREIGN KEY (ID_CLIENTE)
        REFERENCES T_FIAP_CLIENTE_SF (ID_CLIENTE) ON DELETE CASCADE
);

CREATE TABLE T_FIAP_DOR_SF (
    ID_DOR         NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ID_REUNIAO     NUMBER NOT NULL,
    DESCRICAO      VARCHAR2(200) NOT NULL,
    NIVEL_URGENCIA VARCHAR2(10)  NOT NULL,
    CONSTRAINT FK_DOR_REUNIAO_SF FOREIGN KEY (ID_REUNIAO)
        REFERENCES T_FIAP_REUNIAO_SF (ID_REUNIAO) ON DELETE CASCADE
);

CREATE TABLE T_FIAP_MATCH_SF (
    ID_MATCH              NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    ID_REUNIAO            NUMBER NOT NULL,
    ID_ATENDENTE          NUMBER NOT NULL,
    SCORE_COMPATIBILIDADE NUMBER(5,2) NOT NULL,
    MOTIVO                VARCHAR2(400),
    STATUS                VARCHAR2(20) DEFAULT 'SUGERIDO',
    CONSTRAINT FK_MATCH_REUNIAO_SF FOREIGN KEY (ID_REUNIAO)
        REFERENCES T_FIAP_REUNIAO_SF (ID_REUNIAO) ON DELETE CASCADE,
    CONSTRAINT FK_MATCH_ATENDENTE_SF FOREIGN KEY (ID_ATENDENTE)
        REFERENCES T_FIAP_ATENDENTE_SF (ID_ATENDENTE) ON DELETE CASCADE
);

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('João Pereira', 'joao.pereira@sellerfit.com.br', 'Especialista ERP Industrial', '#2563eb', 'JP', 'técnico,direto,objetivo,analítico', 'indústria,logística', 'retenção',
        'Focado em processos de produção e integração de sistemas legados. Vai direto ao ponto e gosta de falar em números.', 6, 8.7, 214, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Mariana Alves', 'mariana.alves@sellerfit.com.br', 'Consultora de Varejo', '#db2777', 'MA', 'comunicativa,empática,ágil,didática', 'varejo,serviços', 'cross-sell',
        'Especialista em jornada do cliente e times de venda. Traduz problema técnico em linguagem simples pro lojista.', 5, 9.4, 301, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Carlos Nogueira', 'carlos.nogueira@sellerfit.com.br', 'Especialista Financeiro', '#059669', 'CN', 'analítico,detalhista,cauteloso,estratégico', 'financeiro,contabilidade', 'retenção',
        'Domina fechamento contábil, fluxo de caixa e integrações bancárias. Gosta de detalhar cada etapa do processo.', 9, 8.2, 189, 'Em atendimento');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Beatriz Ramos', 'beatriz.ramos@sellerfit.com.br', 'Consultora de Saúde', '#7c3aed', 'BR', 'paciente,empática,detalhista,acolhedora', 'saúde,serviços', 'retenção',
        'Trabalhou em clínicas antes de entrar na TOTVS. Entende a rotina corrida de quem atende paciente e busca reduzir fricção.', 4, 9.1, 156, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Eduardo Farias', 'eduardo.farias@sellerfit.com.br', 'Especialista em Logística', '#ea580c', 'EF', 'ágil,prático,direto,resolutivo', 'logística,indústria', 'retenção',
        'Rastreamento, roteirização e integração com transportadoras são o forte dele. Prefere resolver rápido a explicar demais.', 7, 8.5, 245, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Fernanda Costa', 'fernanda.costa@sellerfit.com.br', 'Consultora Estratégica de Contas', '#0891b2', 'FC', 'estratégico,comunicativa,negociadora,confiante', 'indústria,varejo,financeiro', 'cross-sell',
        'Atua em contas grandes e negociações mais longas. Boa em alinhar expectativa de orçamento com o que o sistema entrega.', 11, 9.0, 178, 'Ausente');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Rafael Tanaka', 'rafael.tanaka@sellerfit.com.br', 'Especialista em Suporte Técnico', '#4f46e5', 'RT', 'técnico,paciente,didático,resolutivo', 'indústria,serviços,logística', 'retenção',
        'Perfil de quem gosta de resolver bug junto com o cliente na tela, explicando passo a passo o que está fazendo.', 5, 8.9, 267, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Larissa Menezes', 'larissa.menezes@sellerfit.com.br', 'Consultora de Agronegócio', '#65a30d', 'LM', 'prática,direta,confiante,objetiva', 'agronegócio,indústria', 'cross-sell',
        'Conhece a sazonalidade do campo e como isso afeta fluxo de caixa e estoque. Fala a língua do produtor rural.', 6, 8.6, 132, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Vinícius Souza', 'vinicius.souza@sellerfit.com.br', 'Especialista em Educação', '#d97706', 'VS', 'didático,paciente,comunicativo,organizado', 'educação,serviços', 'retenção',
        'Já implantou sistema de gestão em mais de 40 instituições de ensino. Explica cada módulo com calma.', 4, 9.2, 98, 'Disponível');

INSERT INTO T_FIAP_ATENDENTE_SF (NOME, EMAIL, CARGO, COR_AVATAR, INICIAIS, TRACOS, SEGMENTOS, FOCO, BIO, ANOS_EXPERIENCIA, NOTA_MEDIA, CASES_RESOLVIDOS, DISPONIBILIDADE)
VALUES ('Patrícia Lins', 'patricia.lins@sellerfit.com.br', 'Consultora Sênior Multissetorial', '#be123c', 'PL', 'analítica,estratégica,empática,confiante', 'varejo,saúde,financeiro,serviços', 'retenção,cross-sell',
        'Mais de uma década de TOTVS, já rodou praticamente todos os segmentos. Boa opção quando o perfil do cliente é misto.', 13, 9.3, 412, 'Em atendimento');

INSERT INTO T_FIAP_CLIENTE_SF (NOME, EMPRESA, SEGMENTO, ORCAMENTO) VALUES ('Marcos Andrade', 'Rede Boa Compra', 'varejo', 45000);
INSERT INTO T_FIAP_CLIENTE_SF (NOME, EMPRESA, SEGMENTO, ORCAMENTO) VALUES ('Fernanda Lima', 'Indústria Metalfer', 'indústria', 120000);
INSERT INTO T_FIAP_CLIENTE_SF (NOME, EMPRESA, SEGMENTO, ORCAMENTO) VALUES ('Ricardo Souza', 'Clínica Vida Plena', 'saúde', 30000);

COMMIT;
