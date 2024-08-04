package com.orion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.orion.enums.vehicle.VehicleStatus;
import com.orion.enums.vehicle.FuelType;
import com.orion.enums.vehicle.TransmissionType;
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

    @Column(name = "registration_number", nullable = false, unique = true)
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;

    @Column(name = "year")
    private String year;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private VehicleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type")
    private FuelType fuelType;

    @Column(name="mileage")
    private Long mileage;

    @Enumerated(EnumType.STRING)
    @Column(name="transmission")
    private TransmissionType transmission;

    @Column(name="color")
    private String color;

    @Column(name="description")
    private String description;

    @Column(name="image")
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<MaintenanceRecord> maintenanceRecords;

    @JsonIgnore
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<InsurancePolicy> insurancePolicies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_dates_id", referencedColumnName = "id")
    private RateDates rateDates;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    private Tenant tenant;

}
