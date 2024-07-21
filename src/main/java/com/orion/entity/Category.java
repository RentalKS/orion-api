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
@Getter
@Setter
@Table(name = "categories")
public class Category extends BaseEntity {

        @Column(name = "category_name")
        private String categoryName; // summer cars

        @Column(name = "category_description")
        private String categoryDescription;

        @JsonIgnore
        @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
        private List<Section> sections;


        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "tenant_id", referencedColumnName = "id")
        private Tenant tenant;

        @JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "company_id", referencedColumnName = "id")
        private Company company;
}
