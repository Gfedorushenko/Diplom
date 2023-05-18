package ru.netology.diplomback.repository;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.netology.diplomback.model.UserInfo;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UsersRepositoryImpl implements UsersRepository {
    private static final String FIND_USER_BY_LOGIN = "SELECT id, login, password from filestorage.users where login=:login";
    private static final String FIND_LOGIN_BY_AUTH_TOKEN = "SELECT Login from filestorage.users where auth_token=:authtoken";
    private static final String FIND_ID_BY_AUTH_TOKEN = "SELECT id from filestorage.users where auth_token=:authtoken";
    private static final String UPDATE_AUTH_TOKEN = "UPDATE filestorage.users set auth_token=:authToken where login=:login";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UsersRepositoryImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public UserInfo findUserByUserName(String userName) {
        Map paramMap = new HashMap();
        paramMap.put("login", userName);
        return namedParameterJdbcTemplate.queryForObject(FIND_USER_BY_LOGIN, paramMap, rowMapper());
    }

    public String addAuthToken(UserInfo userInfo) {
        String authToken = generateKey(userInfo.getLogin());
        updateAuthToken(userInfo.getLogin(), authToken);
        return authToken;
    }

    public void deleteAuthToken(String authToken) {
        String userLogin = findLoginByAuthToken(replacePref(authToken));
        if (userLogin != null)
            updateAuthToken(userLogin, null);
    }

    private void updateAuthToken(String userLogin, String authToken) {
        Map paramMap = new HashMap();
        paramMap.put("login", userLogin);
        paramMap.put("authToken", authToken);
        namedParameterJdbcTemplate.update(UPDATE_AUTH_TOKEN, paramMap);
    }

    public Long findIdByAuthToken(String authToken) {
        Map paramMap = new HashMap();
        paramMap.put("authtoken",replacePref(authToken));
        return namedParameterJdbcTemplate.queryForObject(FIND_ID_BY_AUTH_TOKEN, paramMap, Long.class);
    }
    private String findLoginByAuthToken(String authToken) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("authtoken", replacePref(authToken));
        return namedParameterJdbcTemplate.queryForObject(FIND_LOGIN_BY_AUTH_TOKEN, paramMap, String.class);
    }

    private RowMapper<UserInfo> rowMapper() {
        return (rs, rowNum) -> UserInfo.builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .build();
    }
    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now().toString());
    }
    private String replacePref(String text){return text.replace("Bearer ","");}
}
