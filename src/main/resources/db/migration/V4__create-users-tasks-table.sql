CREATE TABLE IF NOT EXISTS users_areas(
    user_id VARCHAR(255) NOT NULL,
    area_id VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,
    FOREIGN KEY (area_id) REFERENCES areas(id)
        ON DELETE CASCADE
);