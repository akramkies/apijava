package com.quest.etna.model;

public class UserDetail {
    private int id;
    private String username;
    private UserRole role;


    public UserDetail(int id, String username, UserRole role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public UserRole getRole() {
        return role;
    }
    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public String toString() {
      return "{" + "\n  \"id\" = \"" + this.id + "\"," + "\n  \"username\" = \"" + this.username + "\"," + "\n  \"role\" = \"" + this.role + '\"' + "\n}";
    }


}
