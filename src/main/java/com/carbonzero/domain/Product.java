package com.carbonzero.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "PRODUCT")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private Long price;

    private String description;

    @Builder.Default
    private Boolean isActive = true;

    @ElementCollection
    private List<String> imageLink;

    private String category;

    private Boolean isEcoFriendly;

    private Integer carbonEmissions;

    public void changeWith(Product source) {
        this.name = source.name;
        this.brand = source.brand;
        this.price = source.price;
        this.description = source.description;
        this.isActive = source.isActive;
        this.imageLink = source.imageLink;
        this.category = source.category;
        this.isEcoFriendly = source.isEcoFriendly;
        this.carbonEmissions = source.carbonEmissions;
    }
}
