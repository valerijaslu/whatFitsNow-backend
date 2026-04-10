-- Weather + health removed (effort + indoor/outdoor are enough)
ALTER TABLE activities DROP COLUMN IF EXISTS weather_compatibility;
ALTER TABLE activities DROP COLUMN IF EXISTS health_compatibility;

