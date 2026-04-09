CREATE TABLE activities (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),

  title VARCHAR(120) NOT NULL,
  description VARCHAR(1000),
  duration_minutes INT NOT NULL,

  effort_level VARCHAR(16) NOT NULL,
  pleasure_score INT NOT NULL,
  satisfaction_score INT NOT NULL,

  location_type VARCHAR(16) NOT NULL,
  social_type VARCHAR(16) NOT NULL,
  weather_compatibility VARCHAR(16) NOT NULL,

  min_energy INT NOT NULL,
  max_energy INT NOT NULL,
  min_health INT NOT NULL,

  is_active BOOLEAN NOT NULL DEFAULT true,

  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX idx_activities_user_id ON activities(user_id);
CREATE INDEX idx_activities_user_id_is_active ON activities(user_id, is_active);

CREATE TABLE tags (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  name VARCHAR(40) NOT NULL,
  created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
  CONSTRAINT uq_tags_user_id_name UNIQUE (user_id, name)
);

CREATE INDEX idx_tags_user_id ON tags(user_id);

CREATE TABLE activity_tags (
  activity_id BIGINT NOT NULL REFERENCES activities(id) ON DELETE CASCADE,
  tag_id BIGINT NOT NULL REFERENCES tags(id) ON DELETE CASCADE,
  PRIMARY KEY (activity_id, tag_id)
);

CREATE INDEX idx_activity_tags_tag_id ON activity_tags(tag_id);

