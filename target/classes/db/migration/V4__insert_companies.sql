CREATE TABLE IF NOT EXISTS companies (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         company_name VARCHAR(255) NOT NULL,
    company_email VARCHAR(255) NOT NULL,
    city VARCHAR(255),
    company_phone VARCHAR(255) NOT NULL,
    state VARCHAR(255),
    zip_code VARCHAR(255) NOT NULL,
    company_address VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id)
    );