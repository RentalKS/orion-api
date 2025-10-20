package com.orion.entity;

import com.orion.enums.Permission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.orion.enums.Permission.*;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Getter
    private String name;

    @ElementCollection(targetClass = Permission.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission", nullable = false)
    private Set<Permission> permissions;

    // Example roles
    @ManyToOne
    @JoinColumn(name = "user_id")
    public static final Role USER = new Role("USER", Collections.emptySet());
    @ManyToOne
    @JoinColumn(name = "admin_id")
    public static final Role ADMIN = new Role("ADMIN", Set.of(
            ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE,
            MANAGER_READ, MANAGER_UPDATE, MANAGER_DELETE, MANAGER_CREATE));

        @ManyToOne
    @JoinColumn(name = "tenant_id")
    public static final Role TENANT = new Role("TENANT", Set.of(
            ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE,
            MANAGER_READ, MANAGER_UPDATE, MANAGER_DELETE, MANAGER_CREATE));

    @ManyToOne
    @JoinColumn(name = "agency_id")
    public static final Role AGENCY = new Role("AGENCY", Set.of(
            ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE, ADMIN_CREATE,
            MANAGER_READ, MANAGER_UPDATE, MANAGER_DELETE, MANAGER_CREATE));

    public Role(String name, Set<Permission> permissions) {
        this.name = name;
        this.permissions = permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name));
        return authorities;
    }
}
