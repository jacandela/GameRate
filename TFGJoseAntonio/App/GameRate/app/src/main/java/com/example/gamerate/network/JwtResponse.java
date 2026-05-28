package com.example.gamerate.network;

import com.google.gson.annotations.SerializedName;

public class JwtResponse {

    @SerializedName(value = "token", alternate = {"accessToken", "jwt"})
    private String token;

    @SerializedName(value = "role", alternate = {"rol", "user_role"})
    private String role;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRol() {
        return role;
    }

    public void setRol(String role) {
        this.role = role;
    }
}