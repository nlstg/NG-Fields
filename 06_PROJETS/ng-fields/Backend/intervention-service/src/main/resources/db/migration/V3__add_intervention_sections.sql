ALTER TABLE interventions ADD COLUMN IF NOT EXISTS departure_time TIMESTAMP WITH TIME ZONE;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS arrival_time TIMESTAMP WITH TIME ZONE;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS start_time TIMESTAMP WITH TIME ZONE;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS end_time TIMESTAMP WITH TIME ZONE;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS duration_minutes INTEGER;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS work_done TEXT;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS result VARCHAR(20);
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS recommendations TEXT;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS billable BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS billing_amount NUMERIC(38,2);
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS billing_notes TEXT;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS local_id VARCHAR(100) UNIQUE;
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS equipment_location VARCHAR(200);
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS openproject_ticket_id VARCHAR(50);
ALTER TABLE interventions ADD COLUMN IF NOT EXISTS openproject_ticket_url VARCHAR(500);

CREATE INDEX IF NOT EXISTS idx_interventions_status ON interventions(status);
CREATE INDEX IF NOT EXISTS idx_interventions_technician ON interventions(assigned_to);
CREATE INDEX IF NOT EXISTS idx_interventions_local_id ON interventions(local_id);
