CREATE TABLE interventions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reference VARCHAR(50) UNIQUE NOT NULL,
    client_id UUID NOT NULL,
    client_name VARCHAR(255),
    client_email VARCHAR(255),
    client_phone VARCHAR(20),
    client_address TEXT,
    equipment_type VARCHAR(100),
    equipment_brand VARCHAR(100),
    equipment_model VARCHAR(100),
    equipment_serial VARCHAR(100),
    reported_issue TEXT,
    diagnosis TEXT,
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    intervention_date TIMESTAMP WITH TIME ZONE,
    created_by UUID,
    assigned_to UUID,
    site_address TEXT,
    site_city VARCHAR(100),
    estimated_cost DECIMAL(10,2),
    total_cost DECIMAL(10,2),
    client_signature TEXT,
    technician_signature TEXT,
    signed_at TIMESTAMP WITH TIME ZONE,
    notes TEXT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE intervention_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    intervention_id UUID NOT NULL REFERENCES interventions(id),
    type VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity INTEGER NOT NULL DEFAULT 1,
    unit_price DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_interventions_client ON interventions(client_id);
CREATE INDEX idx_interventions_status ON interventions(status);
CREATE INDEX idx_interventions_date ON interventions(intervention_date);
CREATE INDEX idx_intervention_items_intervention ON intervention_items(intervention_id);
