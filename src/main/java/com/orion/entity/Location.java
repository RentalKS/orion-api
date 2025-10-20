package com.orion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "locations")
public class Location extends BaseEntity {

    @Column(name = "tables", nullable = false)
    private String tables;

    @Column(name = "address")
    private String address;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country", nullable = false)
    private String country;

    @JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Section> sections;

    @JsonIgnore
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if(address == null || address.isEmpty()){
            return;
        }
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        if(zipCode == null || zipCode.isEmpty()){
            return;
        }
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
