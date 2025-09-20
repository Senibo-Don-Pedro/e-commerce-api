package com.senibo.e_commerce_api.model.product;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ProductCategory",
        description = "Available product categories"
)
public enum ProductCategory {
    ELECTRONICS,
    CLOTHING,
    HOME_GARDEN,
    BOOKS_MEDIA,
    SPORTS_OUTDOORS,
    HEALTH_BEAUTY,
    AUTOMOTIVE,
    TOYS_GAMES,
    FOOD_BEVERAGES,
    ACCESSORIES,
    PET_SUPPLIES,
    OFFICE_SUPPLIES
}
