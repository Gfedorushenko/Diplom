package ru.netology.diplomback.model;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NameOut {
    private String name;

    @Override
    public String toString() {
        return "{\"name\":\"" + name + "\"}";
    }
}
