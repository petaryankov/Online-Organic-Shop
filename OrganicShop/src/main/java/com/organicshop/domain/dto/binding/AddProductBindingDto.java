package com.organicshop.domain.dto.binding;

import com.organicshop.domain.enums.ProductCategoryEnum;
import com.organicshop.validation.product.ValidProductName;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

import static com.organicshop.constants.ValidationErrorMessages.*;

public class AddProductBindingDto {
    @ValidProductName
    private String name;
    @Positive(message = POSITIVE_PRICE)
    @NotNull(message = PRICE_REQUIRED)
    private BigDecimal price;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ProductCategoryEnum category;
    @NotEmpty(message = DESCRIPTION_REQUIRED)
    private String description;

    public AddProductBindingDto() {
    }

    public String getName() {
        return name;
    }

    public AddProductBindingDto setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public AddProductBindingDto setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public ProductCategoryEnum getCategory() {
        return category;
    }

    public AddProductBindingDto setCategory(ProductCategoryEnum category) {
        this.category = category;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AddProductBindingDto setDescription(String description) {
        this.description = description;
        return this;
    }

}
