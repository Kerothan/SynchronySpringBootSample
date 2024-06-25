package com.kerothan.synchronyapichallenge.user;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micrometer.common.lang.NonNull;
import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_name"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue()
    @Column(unique = true)
    private Long userId;

    @Column(name = "user_name", unique = true)
    @NonNull
    private String username;

    @Column(name = "full_name", unique = true)
    @NonNull
    private String fullname;

    @NonNull
    @Column(name = "email", unique = true)
    private String email;

    @NonNull
    @Column(name = "pw", unique = false)
    private String password;

    @Column(name = "imgurl", unique = false)
    private String imgurl;

    @Column(name = "imgdel", unique = false)
    private String imgdel;

    public User() {
    }

    public User(Long userId, @NonNull String username, @NonNull String fullname, @NonNull String email, @NonNull String password) {
        this.fullname = fullname;
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    @NonNull
    public String getFullname() {
        return fullname;
    }

    public void setFullname(@NonNull String fullname) {
        this.username = fullname;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getPassword() {
        return password;
    }

    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(@NonNull String imgurl) {
        this.imgurl = imgurl;
    }

    public String getImgdel() {
        return imgdel;
    }

    public void setImgdel(@NonNull String imgdel) {
        this.imgdel = imgdel;
    }

    @Override
    public String toString() {
        return "User{" +
                "fullName='" + fullname + '\'' +
                ", userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }


}