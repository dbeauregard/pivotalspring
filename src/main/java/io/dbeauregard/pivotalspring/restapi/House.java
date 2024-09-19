package io.dbeauregard.pivotalspring.restapi;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.dbeauregard.pivotalspring.HouseEntity;

// House model for REST/HATEOAS API
class House {

    private final Long id;
    private final String address;
    private final Integer price;

    @JsonCreator
    House(Long id, String address, Integer price) {
        this.id = id;
        this.address = address;
        this.price = price;
    }

    // Convert this HouseEntity to this new HouseDTO
    House(HouseEntity he) {
        this(he.getId(), he.getAddress(), he.getPrice());
    }

    // Convert this HouseDTO to HouseEntity
    @JsonIgnore
    HouseEntity getEntity() {
        return new HouseEntity(id, address, price);
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
