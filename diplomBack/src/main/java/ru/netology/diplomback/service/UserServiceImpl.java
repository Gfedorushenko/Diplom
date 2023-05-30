package ru.netology.diplomback.service;

import org.springframework.stereotype.Service;
import ru.netology.diplomback.exception.DataError;
import ru.netology.diplomback.exception.UnauthorizedError;
import ru.netology.diplomback.model.LoginOut;
import ru.netology.diplomback.model.UserInfo;
import ru.netology.diplomback.repository.UsersRepository;

@Service
public class UserServiceImpl implements UserService{
    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String userAuthentication(UserInfo user) throws RuntimeException {
        if (user.equals(usersRepository.findUserByUserName(user.getLogin()))) {
            return new LoginOut(usersRepository.addAuthToken(user)).toString();
        }
        throw new DataError("Bad credentials");
    }
    public void userLogout(String authToken) {
        usersRepository.deleteAuthToken(authToken);
    }

    public long userAuthorization(String authToken)throws RuntimeException{
        Long userId = usersRepository.findIdByAuthToken(authToken);
        if (userId == null) {
            throw new UnauthorizedError("Unauthorized error");
        }
        return userId;
    }
}
