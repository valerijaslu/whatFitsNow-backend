ALTER TABLE activities
  DROP COLUMN IF EXISTS pleasure_score,
  DROP COLUMN IF EXISTS satisfaction_score,
  DROP COLUMN IF EXISTS min_health;

ALTER TABLE activities
  ADD COLUMN IF NOT EXISTS health_compatibility VARCHAR(32) NOT NULL DEFAULT 'ANY';

