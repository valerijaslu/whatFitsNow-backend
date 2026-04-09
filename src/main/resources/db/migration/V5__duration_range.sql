ALTER TABLE activities ADD COLUMN min_duration_minutes INT;
ALTER TABLE activities ADD COLUMN max_duration_minutes INT;

UPDATE activities
SET min_duration_minutes = duration_minutes,
    max_duration_minutes = duration_minutes
WHERE min_duration_minutes IS NULL OR max_duration_minutes IS NULL;

ALTER TABLE activities ALTER COLUMN min_duration_minutes SET NOT NULL;
ALTER TABLE activities ALTER COLUMN max_duration_minutes SET NOT NULL;

ALTER TABLE activities DROP COLUMN duration_minutes;

