package com.orion.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orion.enums.BrandAccess;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "brands")
public class Brand extends BaseEntity {

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private BrandAccess name;

    @Column(name = "logo")
    private String logo;

    @Column(name="description")
    private String description;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> vehicles;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;

    public String getName() {
        return name.name();
    }

    public void setName(BrandAccess name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        if(logo == null || logo.isEmpty()){
            return;
        }
        this.logo = logo;
    }

    public List<Model> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Model> vehicles) {
        this.vehicles = vehicles;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
