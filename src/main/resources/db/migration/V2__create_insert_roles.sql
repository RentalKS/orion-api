CREATE TABLE IF NOT EXISTS  roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(255) NOT NULL UNIQUE
);

-- Insert default roles
INSERT IGNORE INTO roles (name) VALUES ('USER'), ('ADMIN'), ('MANAGER');