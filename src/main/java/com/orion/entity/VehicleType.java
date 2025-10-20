//package com.orion.entity;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.List;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter
//@Table(name = "vehicle_types")
//public class VehicleType extends BaseEntity {
//
//    @Column(name = "type_name", nullable = false)
//    private String typeName;
//
//    @Column(name = "description")
//    private String description;
//
//    @JsonIgnore
//    @OneToMany(mappedBy = "vehicleType", cascade = CascadeType.ALL)
//    private List<Vehicle> vehicles;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
//    private Tenant tenant;
//}
