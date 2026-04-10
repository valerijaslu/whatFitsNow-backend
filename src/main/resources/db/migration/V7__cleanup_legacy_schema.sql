-- Defensive cleanup for older local schemas that may still contain remnants.

-- Tag system (removed from codebase)
DROP TABLE IF EXISTS activity_tags;
DROP TABLE IF EXISTS tags;

-- Legacy activity columns (removed/refactored)
ALTER TABLE activities DROP COLUMN IF EXISTS description;
ALTER TABLE activities DROP COLUMN IF EXISTS duration_minutes;
ALTER TABLE activities DROP COLUMN IF EXISTS min_energy;
ALTER TABLE activities DROP COLUMN IF EXISTS max_energy;
ALTER TABLE activities DROP COLUMN IF EXISTS min_health;
ALTER TABLE activities DROP COLUMN IF EXISTS pleasure_score;
ALTER TABLE activities DROP COLUMN IF EXISTS satisfaction_score;

