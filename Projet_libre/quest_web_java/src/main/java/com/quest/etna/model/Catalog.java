package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "catalog")
public class Catalog {
    @Id
    @Column(name = "reference", length=50, nullable = false)
    private String reference;
    @Column(name = "family", length=100, nullable = false)
    private String family;
    @Column(name = "designation", length=100, nullable = false)
    private String designation;
    @Column(name = "cost_price", nullable = false)
    private Float costPrice;
    @Column(name = "sale_price", nullable = false)
    private Float salePrice;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "img_url", length=255, nullable = false)
    private String imgUrl;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    public Catalog() {}

    public Catalog(String reference, String family, String designation, Float costPrice, Float salePrice, Integer amount, String imgUrl)
    {
        Date date = new Date();
        this.reference = reference;
        this.family = family;
        this.designation = designation;
        this.costPrice = costPrice;
        this.salePrice = salePrice;
        this.amount = amount;
        this.imgUrl = imgUrl;
        this.creationDate = date;
        this.updatedDate = date;
    }

    public String getReference() {
      return this.reference;
    }

    public String getFamily() {
      return this.family;
    }

    public String getDesignation() {
        return this.designation;
    }

    public Float getCostPrice() {
      return this.costPrice;
    }

    public Float getSalePrice() {
      return this.salePrice;
    }

    public Integer getAmount() {
      return this.amount;
    }

    public String getImgUrl() {
      return this.imgUrl;
    }

    public void setReference(String reference) {
      this.reference = reference;
    }

    public void setFamily(String family) {
      this.family = family;
    }

    public void setCostPrice(Float costPrice) {
      this.costPrice = costPrice;
    }

    public void setSalePrice(Float salePrice) {
      this.salePrice = salePrice;
    }

    public void setAmount(Integer amount) {
      this.amount = amount;
    }

    public void setImgUrl(String imgUrl) {
      this.imgUrl = imgUrl;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o)
        return true;
      if (!(o instanceof Catalog))
        return false;
      Catalog command = (Catalog) o;
      return Objects.equals(this.reference, command.reference) && Objects.equals(this.family, command.family)
          && Objects.equals(this.designation, command.designation) && Objects.equals(this.costPrice, command.costPrice)
          && Objects.equals(this.salePrice, command.salePrice)
          && Objects.equals(this.amount, command.amount)
          && Objects.equals(this.imgUrl, command.imgUrl);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.reference, this.family, this.designation, this.costPrice, this.salePrice, this.imgUrl);
    }

    @Override
    public String toString() {
      return "Catalog {" + "reference=" + this.reference + ", family='" + this.family + '\'' + ", designation='" + this.designation + "', salePrice = '" + this.salePrice + "', costPrice = '"+ this.costPrice + "', imgUrl = '" + this.imgUrl + "', amount = '" + this.amount + "}";
    }
}

