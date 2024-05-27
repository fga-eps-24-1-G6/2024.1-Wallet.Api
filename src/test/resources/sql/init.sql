CREATE TABLE companies
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(256) NOT NULL,
    cnpj             VARCHAR(20),
    ipo              INTEGER,
    foundation_year  INTEGER,
    firm_value       BIGINT,
    number_of_papers BIGINT,
    market_segment   VARCHAR(256),
    sector           VARCHAR(256),
    segment          VARCHAR(256),
    external_id      VARCHAR(256)
);

CREATE TABLE balance_sheets
(
    id              SERIAL PRIMARY KEY,
    company_id      INTEGER NOT NULL
        CONSTRAINT balance_sheets_company_id_companies_id_fk REFERENCES companies(id),
    year            INTEGER NOT NULL,
    quarter         INTEGER NOT NULL,
    net_revenue     BIGINT,
    costs           BIGINT,
    gross_profit    BIGINT,
    net_profit      BIGINT,
    ebitda          BIGINT,
    ebit            BIGINT,
    taxes           BIGINT,
    gross_debt      BIGINT,
    net_debt        BIGINT,
    equity          BIGINT,
    cash            BIGINT,
    assets          BIGINT,
    liabilities     BIGINT
);

CREATE INDEX balance_sheets_company_idx
    ON balance_sheets (company_id);

CREATE INDEX balance_sheets_year_idx
    ON balance_sheets (year);

CREATE TABLE stocks
(
    id                  SERIAL PRIMARY KEY,
    external_id         VARCHAR(256),
    ticker              VARCHAR(7) NOT NULL,
    company_id          INTEGER NOT NULL
        CONSTRAINT stocks_company_id_companies_id_fk REFERENCES companies(id),
    free_float          NUMERIC,
    tag_along           NUMERIC,
    avg_daily_liquidity BIGINT
);

CREATE TABLE dividends
(
    id             SERIAL PRIMARY KEY,
    stock_id       INTEGER NOT NULL
        CONSTRAINT dividends_stock_id_stocks_id_fk REFERENCES stocks(id),
    type           VARCHAR(100) DEFAULT 'Dividendo' NOT NULL,
    value          NUMERIC NOT NULL,
    ownership_date DATE NOT NULL,
    payment_date   DATE NOT NULL
);

CREATE INDEX dividends_stock_idx
    ON dividends (stock_id);

CREATE TABLE prices
(
    id         SERIAL PRIMARY KEY,
    stock_id   INTEGER NOT NULL
        CONSTRAINT prices_stock_id_stocks_id_fk REFERENCES stocks(id),
    value      NUMERIC NOT NULL,
    price_date DATE NOT NULL
);

CREATE INDEX prices_stock_idx
    ON prices (stock_id);

CREATE TABLE wallets
(
    id          SERIAL PRIMARY KEY,
    external_id VARCHAR(256) NOT NULL,
    name        VARCHAR(256) NOT NULL
);

ALTER TABLE stocks ADD CONSTRAINT unique_ticker UNIQUE (ticker);

CREATE TABLE transactions
(
    id         SERIAL PRIMARY KEY,
    wallet_id  INTEGER NOT NULL
        CONSTRAINT transactions_wallet_id_wallets_id_fk REFERENCES wallets(id),
    ticker     VARCHAR(7) NOT NULL
        CONSTRAINT transactions_ticker_stocks_ticker_fk REFERENCES stocks(ticker),
    operation  VARCHAR(10) NOT NULL,
    amount     INTEGER NOT NULL,
    price      NUMERIC NOT NULL,
    date       DATE NOT NULL,
    CONSTRAINT transactions_unique_id_wallet_id UNIQUE (id, wallet_id)
);

CREATE INDEX transactions_wallet_idx
    ON transactions (wallet_id);

INSERT INTO companies
    (name, cnpj, ipo, foundation_year, firm_value, number_of_papers, market_segment, sector, segment, external_id)
VALUES
    ('PETROLEO BRASILEIRO S.A. PETROBRAS', '33000167000101', 1977, 1953, 754871726000, 13044496000, 'Nível 2',
    'Empresas do Setor Petróleo, Gás e Biocombustíveis', 'Empresas do Segmento Exploração  Refino e Distribuição', null);

INSERT INTO stocks
    (external_id, ticker, company_id, free_float, tag_along, avg_daily_liquidity)
VALUES
    (null, 'petr4', 1, 63.33, 100, 1617781000);

INSERT INTO dividends
    (stock_id, type, value, ownership_date, payment_date)
VALUES
    (1, 'Dividendos', 0.54947422, '2024-04-25', CURRENT_DATE);

INSERT INTO prices
    (stock_id, value, price_date)
VALUES
    (1, 34.87, '2024-04-23'),
    (1, 32.45, '2024-03-24'),
    (1, 30.96, '2024-03-25'),
    (1, 27.5, '2023-04-25'),
    (1, 35.96, '2024-04-24'),
    (1, 36.02, '2024-04-24'),
    (1, 27.96, '2023-04-24');

INSERT INTO balance_sheets
    (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda, ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities)
VALUES
    (1, 2023, 1, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000),
    (1, 2023, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000),
    (1, 2018, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000),
    (1, 2017, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000),
    (1, 2019, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000),
    (1, 2020, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000),
     (1, 2021, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000, 303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000);

 INSERT INTO wallets
     (external_id, name)
 VALUES
     ('1', 'Carteira Principal - Lucas');
