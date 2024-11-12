package io.dbeauregard.pivotalspring;

import com.fasterxml.jackson.annotation.JsonCreator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

//Spring JPA Entity
@Entity(name="houses")
public class HouseEntity {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private Integer price;
    private Integer bdrm;
    private Integer bath;
    private Integer sqft;

    protected HouseEntity() {} //for JPA

    public HouseEntity(String address, Integer price, Integer bdrm, Integer bath, Integer sqft) {
        this.address = address;
        this.price = price;
        this.bdrm = bdrm;
        this.bath = bath;
        this.sqft = sqft;
    }

    @JsonCreator
    public HouseEntity(Long id, String address, Integer price, Integer bdrm, Integer bath, Integer sqft) {
        this.id = id;
        this.address = address;
        this.price = price;
        this.bdrm = bdrm;
        this.bath = bath;
        this.sqft = sqft;
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

    public Integer getBdrm() {
        return bdrm;
    }

    public Integer getBath() {
        return bath;
    }

    public Integer getSqft() {
        return sqft;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setBdrm(Integer bdrm) {
        this.bdrm = bdrm;
    }

    public void setBath(Integer bath) {
        this.bath = bath;
    }

    public void setSqft(Integer sqft) {
        this.sqft = sqft;
    }

    @Override
    public String toString() {
        return "HouseEntity [id=" + id + ", address=" + address + ", price=" + price + ", bdrm=" + bdrm + ", bath="
                + bath + ", sqft=" + sqft + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime * result + ((price == null) ? 0 : price.hashCode());
        result = prime * result + ((bdrm == null) ? 0 : bdrm.hashCode());
        result = prime * result + ((bath == null) ? 0 : bath.hashCode());
        result = prime * result + ((sqft == null) ? 0 : sqft.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HouseEntity other = (HouseEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (price == null) {
            if (other.price != null)
                return false;
        } else if (!price.equals(other.price))
            return false;
        if (bdrm == null) {
            if (other.bdrm != null)
                return false;
        } else if (!bdrm.equals(other.bdrm))
            return false;
        if (bath == null) {
            if (other.bath != null)
                return false;
        } else if (!bath.equals(other.bath))
            return false;
        if (sqft == null) {
            if (other.sqft != null)
                return false;
        } else if (!sqft.equals(other.sqft))
            return false;
        return true;
    }
    
}
