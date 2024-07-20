CREATE TABLE IF NOT EXISTS tenants
(
    updated_at datetime     null,
    name       varchar(255) not null,
    domain     varchar(255) not null,
    deleted_at datetime     null,
    created_at datetime     null,
    id         bigint auto_increment
    primary key,
    constraint UK_ehgpgu3yilhiprwm3wbipxt43
    unique (domain)
    );
INSERT IGNORE INTO tenants (id, created_at, deleted_at, domain, name, updated_at) VALUES (1, NOW(), null, 'dev.orion.de', 'Orion', NOW());