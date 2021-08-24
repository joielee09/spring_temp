package com.carbonzero.dto;

import com.github.dozermapper.core.Mapping;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class ProductRequestData {
    @NotBlank
    @Mapping("name")
    private String name;

    @NotBlank
    @Mapping("brand")
    private String brand;

    @NotNull
    @Mapping("price")
    private Long price;

    @Mapping("description")
    private String description;

    @Mapping("imageLink")
    private List<String> imageLink;

    @NotBlank
    @Mapping("category")
    private String category;

    @Mapping("isEcoFriendly")
    private Boolean isEcoFriendly;

    @Mapping("carbonEmissions")
    private Integer carbonEmissions;
}
