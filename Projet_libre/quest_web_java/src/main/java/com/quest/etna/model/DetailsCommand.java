package com.quest.etna.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "detailsCommand")
public class DetailsCommand {
    @Id
    @GeneratedValue
    @Column(length = 11)
    private int id;
    @ManyToOne
    @JoinColumn( name="num_command")
    private Command command;
    @ManyToOne
    @JoinColumn( name="reference")
    private Catalog catalog;
    @Column(name = "remise", nullable = false)
    private Boolean remise;
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    DetailsCommand() {}

    public DetailsCommand(Integer numCommand, String reference, Boolean remise, Integer quantity)
    {
        this.command = new Command();
        this.command.setNumCommand(numCommand);
        this.catalog = new Catalog();
        this.catalog.setReference(reference);
        this.remise = remise;
        this.quantity = quantity;
    }

    public Integer getNumCommand() {
        return this.command.getNumCommand();
    }

    public String getReference() {
        return this.catalog.getReference();
    }

    public Boolean isRemise() {
        return this.remise;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setId(int id) {
      this.id = id;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o)
        return true;
      if (!(o instanceof DetailsCommand))
        return false;
      DetailsCommand detailsCommand = (DetailsCommand) o;
      return Objects.equals(this.catalog, detailsCommand.catalog) && Objects.equals(this.command, detailsCommand.command)
          && Objects.equals(this.remise, detailsCommand.remise) && Objects.equals(this.quantity, detailsCommand.quantity);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.catalog, this.command, this.quantity, this.remise);
    }

    @Override
    public String toString() {
      return "DetailsCommand {" + "numCommand=" + this.command.getNumCommand() + ", reference='" + this.catalog.getReference() + '\'' + ", quantity='" + this.quantity + "', remise = '" + this.remise + "}";
    }
}
