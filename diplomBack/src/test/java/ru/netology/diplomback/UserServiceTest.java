package ru.netology.diplomback;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import ru.netology.diplomback.model.UserInfo;
import ru.netology.diplomback.repository.UsersRepository;
import ru.netology.diplomback.service.UserServiceImpl;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    @Mock
    UsersRepository usersRepository;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void test_userAuthentication() {
        String login = "ivan";
        String password = "111";
        String authToken = "1234567890123456";
        UserInfo userInfo = new UserInfo(login, password);

        Mockito.when(usersRepository.findUserByUserName(login)).thenReturn(userInfo);
        Mockito.when(usersRepository.addAuthToken(userInfo)).thenReturn(authToken);

        String result = userService.userAuthentication(userInfo);
        String expected = "{\"auth-token\":\"" + authToken + "\"}";
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void test_userLogout() {
        String authToken = "1234567890123456";
        userService.userLogout(authToken);
        Mockito.verify(usersRepository, Mockito.times(1)).deleteAuthToken(authToken);
    }

    @Test
    public void test_userAuthorization() {
        String authToken = "1234567890123456";

        Mockito.when(usersRepository.findIdByAuthToken(authToken)).thenReturn(2L);

        Long result = userService.userAuthorization(authToken);
        Assertions.assertEquals(2L, result);
    }
}
