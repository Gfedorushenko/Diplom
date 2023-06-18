package ru.netology.diplomback.repository;

import ru.netology.diplomback.model.UsersInfo;

public interface UsersRepository {
    UsersInfo findUserByUserName(String userName);

    String addAuthToken(UsersInfo userInfo);

    void deleteAuthToken(String authToken);

    Long findIdByAuthToken(String authToken);
}
