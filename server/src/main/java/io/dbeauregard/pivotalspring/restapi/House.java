package io.dbeauregard.pivotalspring.restapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.dbeauregard.pivotalspring.HouseEntity;

// House model for REST/HATEOAS API
class House {

    private final Long id;
    private final String address;
    private final Integer price;
    private final Integer bdrm;
    private final Integer bath;
    private final Integer sqft;

    @JsonCreator
    House(Long id, String address, Integer price, Integer bdrm, Integer bath, Integer sqft) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.bdrm = bdrm;
        this.bath = bath;
        this.sqft = sqft;
    }

    // Convert this HouseEntity to this new HouseDTO
    House(HouseEntity he) {
        this(he.getId(), he.getAddress(), he.getPrice(), he.getBdrm(), he.getBath(), he.getSqft());
    }

    // Convert this HouseDTO to HouseEntity
    @JsonIgnore
    HouseEntity getEntity() {
        return new HouseEntity(id, address, price, bdrm, bath, sqft);
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "House [id=" + id + ", address=" + address + ", price=" + price + "]";
    }

}
