package ru.netology.diplomback.service;

import ru.netology.diplomback.model.UserInfo;

public interface UserService {
    String userAuthentication(UserInfo user) throws RuntimeException;

    void userLogout(String authToken);

    long userAuthorization(String authToken) throws RuntimeException;

}
