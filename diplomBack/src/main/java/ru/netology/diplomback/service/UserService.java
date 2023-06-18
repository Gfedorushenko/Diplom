package ru.netology.diplomback.service;

import ru.netology.diplomback.model.UsersInfo;

public interface UserService {
    String userAuthentication(UsersInfo user) throws RuntimeException;

    void userLogout(String authToken);

    long userAuthorization(String authToken) throws RuntimeException;

}
