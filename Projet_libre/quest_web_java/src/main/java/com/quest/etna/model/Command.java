package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "command")
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "num_command", length = 11)
    private Integer numCommand;
    @ManyToOne
    @JoinColumn( name="user_id")
    private User user;
    @Column(name = "transporter", length=100, nullable = false)
    private String transporter;
    @Column(name = "command_date")
    private Date commandDate;
    @Column(name = "notes", length=255, nullable = false)
    private String notes;
    @Column(name = "urgent", nullable = false)
    private Boolean urgent;

    public Command() {}

    public Command(Integer numCommand, String transporter, String notes, Boolean urgent, int userID)
    {
        Date date = new Date();
        this.numCommand = numCommand;
        this.transporter = transporter;
        this.notes = notes;
        this.urgent = urgent;
        this.commandDate = date;
        this.user = new User();
        this.user.setId(userID);
    }


    public Integer getNumCommand() {
      return this.numCommand;
    }

    public String getTransporter() {
      return this.transporter;
    }

    public String getNotes() {
        return this.notes;
    }

    public Boolean isUrgent() {
      return this.urgent;
    }

    public int getUserID() {
      return this.user.getId();
    }

    public Date getCommandDate() {
        return this.commandDate;
    }

    public void setNumCommand(Integer numCommand) {
      this.numCommand = numCommand;
    }

    public void setTRansporter(String transporter) {
      this.transporter = transporter;
    }

    public void setNotes(String notes) {
      this.notes = notes;
    }

    public void setUrgent(Boolean urgent) {
      this.urgent = urgent;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o)
        return true;
      if (!(o instanceof Command))
        return false;
      Command command = (Command) o;
      return Objects.equals(this.numCommand, command.numCommand) && Objects.equals(this.notes, command.notes)
          && Objects.equals(this.transporter, command.transporter) && Objects.equals(this.urgent, command.urgent)
          && Objects.equals(this.commandDate, command.commandDate);
    }

    @Override
    public int hashCode() {
      return Objects.hash(this.numCommand, this.transporter, this.notes, this.commandDate, this.urgent);
    }

    @Override
    public String toString() {
      return "Command {" + "numCommand=" + this.numCommand + ", transporter='" + this.transporter + '\'' + ", notes='" + this.notes + "', commandDate = '" + this.commandDate + "', urgent = '"+ this.urgent + "}";
    }
}
