package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue
    @Column(length = 11)
    private int id;
    @ManyToOne
    @JoinColumn( name="user_id")
    private User user;
    @Column(name = "street", length=100, nullable = false)
    private String street;
    @Column(name = "postal_code", length=30, nullable = false)
    private String postalCode;
    @Column(name = "city", length=50, nullable = false)
    private String city;
    @Column(name = "country", length=50, nullable = false)
    private String country;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    public Address() {}

    public Address(String street, String postalCode, String city, String country, int userID)
    {
        Date date = new Date();
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
        this.creationDate = date;
        this.updatedDate = date;
        this.user = new User();
        this.user.setId(userID);
    }


    public int getId() {
      return this.id;
    }

    public int getUserID() {
        return this.user.getId();
      }

    public String getStreet() {
      return this.street;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public String getCity() {
      return this.city;
    }

    public String getCountry() {
        return this.country;
      }

    public void setId(int id) {
      this.id = id;
    }

    public void setStreet(String street) {
      this.street = street;
    }

    public void setCity(String city) {
      this.city = city;
    }

    public void setCountry(String country) {
      this.country = country;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o)
        return true;
      if (!(o instanceof User))
        return false;
      Address user = (Address) o;
      return Objects.equals(this.id, user.id) && Objects.equals(this.city, user.city)
          && Objects.equals(this.country, user.country) && Objects.equals(this.postalCode, user.postalCode);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.street, this.postalCode, this.city, this.country);
    }

    @Override
    public String toString() {
      return "Address{" + "id=" + this.id + ", street='" + this.street + '\'' + ", postal code='" + this.postalCode + "', city = '" + this.city + "', country = '"+ this.country +"'', userID = "+ this.getUserID() +"}";
    }
}
