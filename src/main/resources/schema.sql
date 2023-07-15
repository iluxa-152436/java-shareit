DROP TABLE IF EXISTS app_users CASCADE;
DROP TABLE IF EXISTS items CASCADE;

CREATE TABLE IF NOT EXISTS app_users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(40) NOT NULL,
  email VARCHAR(40) NOT NULL,
  CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(40) NOT NULL,
  description VARCHAR(200) NOT NULL,
  available BOOLEAN NOT NULL,
  user_id BIGINT REFERENCES app_users (id) ON DELETE CASCADE
);