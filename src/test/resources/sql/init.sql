create table companies
(
    id               serial
        primary key,
    name             varchar(256) not null,
    cnpj             varchar(20),
    ipo              integer,
    foundation_year  integer,
    firm_value       bigint,
    number_of_papers bigint,
    market_segment   varchar(256),
    sector           varchar(256),
    segment          varchar(256),
    external_id      varchar(256)
);

create table balance_sheets
(
    company_id   serial
        constraint balance_sheets_company_id_companies_id_fk
            references companies,
    year         integer not null,
    quarter      integer not null,
    net_revenue  bigint,
    costs        bigint,
    gross_profit bigint,
    net_profit   bigint,
    ebitda       bigint,
    ebit         bigint,
    taxes        bigint,
    gross_debt   bigint,
    net_debt     bigint,
    equity       bigint,
    cash         bigint,
    assets       bigint,
    liabilities  bigint,
    id           serial
);

create index balance_sheets_company_idx
    on balance_sheets (company_id);

create index balance_sheets_year_idx
    on balance_sheets (year);

create table stocks
(
    id                  serial
        primary key,
    external_id         varchar(256),
    ticker              varchar(7) not null,
    company_id          serial
        constraint stocks_company_id_companies_id_fk
            references companies,
    free_float          numeric,
    tag_along           numeric,
    avg_daily_liquidity bigint
);

create table dividends
(
    stock_id       serial
        constraint dividends_stock_id_stocks_id_fk
            references stocks,
    type           varchar(100) default 'Dividendo'::character varying not null,
    value          numeric                                             not null,
    ownership_date date                                                not null,
    payment_date   date                                                not null,
    id             serial
);

create index dividends_stock_idx
    on dividends (stock_id);

create table prices
(
    stock_id   serial
        constraint prices_stock_id_stocks_id_fk
            references stocks,
    value      numeric not null,
    price_date date    not null,
    id         serial
        primary key
);

create index prices_stock_idx
    on prices (stock_id);

create table wallets
(
    id          serial
        primary key,
    external_id varchar(256) not null,
    name        varchar(256) not null
);

ALTER TABLE stocks ADD CONSTRAINT unique_ticker UNIQUE (ticker);

create table transactions
(
    id        serial
        primary key,
    wallet_id serial
        constraint transactions_wallet_id_wallets_id_fk
            references wallets,
    ticker    varchar     not null
        constraint transactions_ticker_stocks_ticker_fk
            references stocks (ticker),
    operation varchar(10) not null,
    amount    integer     not null,
    price     numeric     not null,
    date      date        not null
);

create index transactions_wallet_idx
    on transactions (wallet_id);

INSERT INTO public.companies (id, name, cnpj, ipo, foundation_year, firm_value, number_of_papers, market_segment,
                              sector, segment, external_id)
VALUES (1, 'PETROLEO BRASILEIRO S.A. PETROBRAS', '33000167000101', 1977, 1953, 754871726000, 13044496000, 'Nível 2',
        'Empresas do Setor Petróleo, Gás e Biocombustíveis', 'Empresas do Segmento Exploração  Refino e Distribuição',
        null);

INSERT INTO public.stocks (id, external_id, ticker, company_id, free_float, tag_along, avg_daily_liquidity)
VALUES (4, null, 'petr4', 1, 63.33, 100, 1617781000);

INSERT INTO public.dividends (stock_id, type, value, ownership_date, payment_date, id)
VALUES (4, 'Dividendos', 0.54947422, '2024-04-25', CURRENT_DATE, 1);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 34.87, '2024-04-23', 2);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 32.45, '2024-03-24', 5);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 30.96, '2024-03-25', 6);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 27.5, '2023-04-25', 7);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 35.96, '2024-04-24', 3);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 36.02, '2024-04-24', 4);

INSERT INTO public.prices (stock_id, value, price_date, id)
VALUES (4, 27.96, '2023-04-24', 8);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2023, 1, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 1);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2023, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 2);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2018, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 2);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2017, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 2);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2019, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 2);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2020, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 2);

INSERT INTO public.balance_sheets (company_id, year, quarter, net_revenue, costs, gross_profit, net_profit, ebitda,
                                   ebit, taxes, gross_debt, net_debt, equity, cash, assets, liabilities, id)
VALUES (1, 2021, 4, 511994000000, -242061000000, 269933000000, 125166000000, 255546000000, 189342000000, -52315000000,
        303062000000, 227799000000, 382340000000, 75263000000, 1050888000000, 1050888000000, 2);

INSERT INTO public.wallets (id, external_id, name)
VALUES (2, '1', 'Carteira Principal - Lucas');