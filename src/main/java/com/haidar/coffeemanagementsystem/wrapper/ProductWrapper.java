package com.haidar.coffeemanagementsystem.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class ProductWrapper {

    private Integer id;
    private String name;
    private String description;
    private Integer categoryId;
    private Integer price;
    private String status;
    private String categoryName;

    public ProductWrapper() {

    }

    public ProductWrapper(Integer id, String name, String description,
                          Integer categoryId, Integer price, String status, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.status = status;
        this.categoryName = categoryName;
    }
}
