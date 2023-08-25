package com.quest.etna.model;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username", length=255, nullable = false)
    private String username;
    @Column(name = "password", length=255, nullable = false)
    private String password;
    @Column(name = "role", length=255)
    private String role;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "updated_date")
    private Date updatedDate;
  
    public User() {}
  
    public User(String username, String password) 
    {
        Date date = new Date();  
        this.username = username;
        this.password = password;
        this.role = UserRole.ROLE_USER.name();
        this.creationDate = date;
        this.updatedDate = date;
    }
  
    public Long getId() {
      return this.id;
    }
  
    public String getUsername() {
      return this.username;
    }

    public String getPassword() {
        return this.password;
    }
  
    public String getRole() {
      return this.role;
    }
  
    public void setId(Long id) {
      this.id = id;
    }
  
    public void setUsername(String username) {
      this.username = username;
    }

    public void setPassword(String password) {
      this.password = password;
    }
  
    public void setRole(String role) {
      this.role = role;
    }
  
    @Override
    public boolean equals(Object o) {
  
      if (this == o)
        return true;
      if (!(o instanceof User))
        return false;
      User user = (User) o;
      return Objects.equals(this.id, user.id) && Objects.equals(this.username, user.username)
          && Objects.equals(this.role, user.role);
    }
  
    @Override
    public int hashCode() {
      return Objects.hash(this.id, this.username, this.role);
    }
  
    @Override
    public String toString() {
      return "User{" + "id=" + this.id + ", username='" + this.username + '\'' + ", role='" + this.role + '\'' + '}';
    }
}
