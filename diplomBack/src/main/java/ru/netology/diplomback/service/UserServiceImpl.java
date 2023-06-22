package ru.netology.diplomback.service;

import org.springframework.stereotype.Service;
import ru.netology.diplomback.exception.DataError;
import ru.netology.diplomback.exception.UnauthorizedError;
import ru.netology.diplomback.model.LoginOut;
import ru.netology.diplomback.model.UsersInfo;
import ru.netology.diplomback.repository.UsersRepository;

import java.util.logging.Level;

import static ru.netology.diplomback.controller.MainController.logger;

@Service
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public String userAuthentication(UsersInfo user) throws RuntimeException {
        if (user.equals(usersRepository.findUserByUserName(user.getLogin()))) {
            logger.log(Level.INFO, "Authentication: Ok");
            return new LoginOut(usersRepository.addAuthToken(user)).toString();
        }
        logger.log(Level.INFO, "Authentication: Bad credentials");
        throw new DataError("Bad credentials");
    }

    public void userLogout(String authToken) {
        usersRepository.deleteAuthToken(authToken);
    }

    public long userAuthorization(String authToken) throws RuntimeException {
        Long userId;
        try {
            userId = usersRepository.findIdByAuthToken(authToken);
            logger.log(Level.INFO, "Authorization: Ok");
        } catch (Exception e) {
            logger.log(Level.INFO, "Authorization: Unauthorized error");
            throw new UnauthorizedError("Unauthorized error");
        }
        return userId;
    }
}
