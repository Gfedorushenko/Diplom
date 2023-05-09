package ru.netology.diplomback.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LoginOut {
    private String authToken;

    @Override
    public String toString() {
        return "{\"auth-token\":\"" + authToken + "\"}";
    }
}
