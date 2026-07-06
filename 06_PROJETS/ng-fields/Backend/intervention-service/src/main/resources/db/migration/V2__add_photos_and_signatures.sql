ALTER TABLE interventions ADD COLUMN IF NOT EXISTS manager_signature TEXT;

CREATE TABLE IF NOT EXISTS intervention_photos (
    id UUID PRIMARY KEY,
    intervention_id UUID NOT NULL REFERENCES interventions(id),
    url TEXT NOT NULL,
    type VARCHAR(10) NOT NULL,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    taken_at TIMESTAMP WITH TIME ZONE,
    original_filename VARCHAR(200),
    created_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_photos_intervention_id ON intervention_photos(intervention_id);
CREATE INDEX IF NOT EXISTS idx_photos_intervention_type ON intervention_photos(intervention_id, type);
