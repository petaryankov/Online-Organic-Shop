package com.organicshop.domain.dto.binding;

import com.organicshop.validation.common.ValidPhoneNumber;
import com.organicshop.validation.order.ValidAddress;

public class OrderBindingDto {

    private String comment;
    @ValidAddress
    private String address;
    @ValidPhoneNumber
    private String contactNumber;


    private Double deliveryFee = 4.20;
    public Double getDeliveryFee() {
        return deliveryFee;
    }
    private Double bagFee = 0.10;

    public Double getBagFee() {
        return bagFee;
    }

    public OrderBindingDto() {
    }

    public String getComment() {
        return comment;
    }

    public OrderBindingDto setComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public OrderBindingDto setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public OrderBindingDto setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
        return this;
    }
}
