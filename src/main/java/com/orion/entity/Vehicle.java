package com.orion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orion.enums.vehicle.VehicleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private String year;

    @Column(name = "price")
    private String price;

    @Column(name = "image")
    private String image;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private VehicleStatus status;

    @Column(name="fuel_type")
    private String fuelType;

    @Column(name="mileage")
    private Long mileage;

    @Column(name="engine")
    private String engine;

    @Column(name="transmission")
    private String transmission;

    @Column(name="color")
    private String color;

    @Column(name="interior")
    private String interior;

    @Column(name="exterior")
    private String exterior;

    @Column(name="vin")
    private String vin;

    @Column(name="stock_number")
    private String stockNumber;

    @Column(name="mpg")
    private String mpg;

    @Column(name="features")
    private String features;

    @Column(name="options")
    private String options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fleet_id", referencedColumnName = "id")
    private Fleet fleet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_type_id", referencedColumnName = "id")
    private VehicleType vehicleType;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<MaintenanceRecord> maintenanceRecords;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<InsurancePolicy> insurancePolicies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;

}
