CREATE TABLE cities (
    ibge_id INT UNSIGNED NOT NULL,
    uf VARCHAR(2) NOT NULL,
    name VARCHAR(50) NOT NULL,
    capital BOOLEAN NOT NULL,
    lon DECIMAL NOT NULL,
    lat DECIMAL NOT NULL,
    no_accents VARCHAR(50) NOT NULL,
    alternative_names VARCHAR(50),
    microregion VARCHAR(50) NOT NULL,
    mesoregion VARCHAR(50) NOT NULL,
    optlock BIGINT DEFAULT 0 NOT NULL,
    PRIMARY KEY (ibge_id)
);