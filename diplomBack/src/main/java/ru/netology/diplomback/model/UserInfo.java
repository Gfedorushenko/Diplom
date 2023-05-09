package ru.netology.diplomback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;


@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class UserInfo implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String login;
    private String password;

    public UserInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserInfo userInfo)) return false;
        return Objects.equals(login, userInfo.login) && Objects.equals(password, userInfo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }
}
