CREATE TABLE IF NOT EXISTS tasks(
    id VARCHAR(255) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    complete INTEGER DEFAULT 0 CHECK (complete IN (0,1)) NOT NULL,
    important INTEGER DEFAULT 0 CHECK (important IN (0,1)) NOT NULL,
    urgent INTEGER DEFAULT 0 CHECK (urgent IN (0,1)) NOT NULL,
    area_id VARCHAR(255),
    user_id VARCHAR(255),
    FOREIGN KEY (area_id) REFERENCES areas(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);