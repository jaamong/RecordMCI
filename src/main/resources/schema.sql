CREATE TABLE IF NOT EXISTS 'users' (
    users_id INTEGER NOT NULL,
    username TEXT NULL,
    password TEXT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("users_id" AUTOINCREMENT)
);

CREATE TABLE IF NOT EXISTS 'daily_record' (
    daily_record_id INTEGER NOT NULL,
    users_id INTEGER NOT NULL,
    record_date TEXT NULL,
    memo TEXT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("daily_record_id" AUTOINCREMENT),
    CONSTRAINT FK_users_TO_daily_record 
        FOREIGN KEY(users_id) 
        REFERENCES users(users_id) 
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS 'activity' (
    activity_id INTEGER NOT NULL,
    daily_record_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    completed INTEGER NOT NULL DEFAULT 0,
    total_steps SMALLINT NULL,
    total_hours INTEGER NULL,
    total_minutes INTEGER NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("activity_id" AUTOINCREMENT),
    CONSTRAINT FK_daily_record_TO_activity
        FOREIGN KEY(daily_record_id)
        REFERENCES daily_record(daily_record_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS 'food' (
    food_id INTEGER NOT NULL,
    daily_record_id INTEGER NOT NULL,
    name TEXT NOT NULL,
    consumed INTEGER NOT NULL DEFAULT 0,
    nutrient_type VARCHAR(10) NOT NULL,
    created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY ("food_id" AUTOINCREMENT),
    CONSTRAINT FK_daily_record_TO_food
        FOREIGN KEY(daily_record_id)
        REFERENCES daily_record(daily_record_id)
        ON DELETE CASCADE ON UPDATE CASCADE
);