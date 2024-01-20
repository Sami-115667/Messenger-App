package com.example.smessenger;

public class Users {
    String profilepic, name, email, password,userid;

    public Users() {
    }

    public Users(String profilepic, String name, String email, String password, String userid) {
        this.profilepic = profilepic;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userid = userid;
    }



    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
