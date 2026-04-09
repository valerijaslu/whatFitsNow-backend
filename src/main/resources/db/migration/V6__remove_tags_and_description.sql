ALTER TABLE activity_tags DROP CONSTRAINT IF EXISTS activity_tags_activity_id_fkey;
ALTER TABLE activity_tags DROP CONSTRAINT IF EXISTS activity_tags_tag_id_fkey;

DROP TABLE IF EXISTS activity_tags;
DROP TABLE IF EXISTS tags;

ALTER TABLE activities DROP COLUMN IF EXISTS description;

