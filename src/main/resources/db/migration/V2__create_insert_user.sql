CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id)
    );
INSERT IGNORE INTO user (id, first_name,last_name ,email, password, role_id) VALUES (1,'orion','dev', 'orion@dev.com', '$2y$10$ezT6UySUMKuyUXTe3jnIeuq3/sM51M06u7YKVDJWoveF2EsyxgK2G', 4);

CREATE TABLE IF NOT EXISTS token (
                                     id bigint AUTO_INCREMENT PRIMARY KEY,
                                     token VARCHAR(255) NOT NULL,
    user_id bigint,
    FOREIGN KEY (user_id) REFERENCES user(id)
    );
