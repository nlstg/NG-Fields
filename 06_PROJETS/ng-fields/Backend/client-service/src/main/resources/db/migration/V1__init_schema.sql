CREATE TABLE IF NOT EXISTS clients (
    id           UUID           PRIMARY KEY,
    reference    VARCHAR(20)    NOT NULL UNIQUE,
    company_name VARCHAR(200)   NOT NULL,
    contact_name VARCHAR(150),
    email        VARCHAR(150)   NOT NULL UNIQUE,
    phone        VARCHAR(30),
    address      TEXT,
    latitude     DOUBLE PRECISION,
    longitude    DOUBLE PRECISION,
    active       BOOLEAN        NOT NULL DEFAULT TRUE,
    created_by   VARCHAR(100),
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_clients_email ON clients(email);
CREATE INDEX IF NOT EXISTS idx_clients_active ON clients(active);
CREATE INDEX IF NOT EXISTS idx_clients_company ON clients(company_name);
