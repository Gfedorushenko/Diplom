package ru.netology.diplomback.repository;

import ru.netology.diplomback.model.UserInfo;

public interface UsersRepository {
    UserInfo findUserByUserName(String userName);

    String addAuthToken(UserInfo userInfo);

    void deleteAuthToken(String authToken);

    Long findIdByAuthToken(String authToken);
}
