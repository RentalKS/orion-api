 CREATE TABLE IF NOT EXISTS  roles (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       name VARCHAR(255) NOT NULL UNIQUE
     );

 -- Insert default roles
INSERT IGNORE INTO roles (id, name) VALUES (1,'USER'), (2,'ADMIN'), (3,'MANAGER'),(4,'TENANT'), (5,'CLIENT'), (6,'AGENCY'), (7,'EMPLOYEE');