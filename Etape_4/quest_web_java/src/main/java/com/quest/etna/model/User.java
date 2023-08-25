package com.quest.etna.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    @Column(length = 11)
    private int id;
    @Column(name = "username", length=255, unique = true, nullable = false)
    private String username;
    @Column(name = "password", length=255, nullable = false)
    private String password;
    @Column(name = "role", length=255)
    private String role;
    @Column(name = "creation_date")
    private Date creationDate;
    @Column(name = "updated_date")
    private Date updatedDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Address> recordings = new HashSet<>();


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

    public User(String username, String password, String role)
    {
        Date date = new Date();
        this.username = username;
        this.password = password;
        this.role = role;
        this.creationDate = date;
        this.updatedDate = date;
    }

    public User(String username)
    {
        Date date = new Date();
        this.username = username;
        this.password = null;
        this.role = UserRole.ROLE_USER.name();
        this.creationDate = date;
        this.updatedDate = date;
    }

    public int getId() {
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

    public void setId(int id) {
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
