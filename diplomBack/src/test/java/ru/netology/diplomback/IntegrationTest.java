package ru.netology.diplomback;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;

public class IntegrationTest extends AbstractIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;
    @Test
    void test_loginBack200() {
        Map<String, String> map = new HashMap();
        map.put("login", "ivan");
        map.put("password", "111");
        ResponseEntity<String> entity = restTemplate
                .postForEntity("http://localhost:" + port + "/login", map, String.class);

        String response = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        assertThat(response, CoreMatchers.containsString("auth-token"));
    }
    @Test
    void test_loginBack400() {
        Map<String, String> map = new HashMap();
        map.put("login", "ivan");
        map.put("password", "1111");
        ResponseEntity<String> entity = restTemplate
                .postForEntity("http://localhost:" + port + "/login", map, String.class);

        String response = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(entity.getBody()).isNotNull();
        Assertions.assertEquals("Bad credentials", response);
    }
    @Test
    void test_logoutBack() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "41c10aef5b249a6b983c16c16fe2cf8d");
        final HttpEntity<String> response = new HttpEntity<String>(headers);

        ResponseEntity<String> entity = restTemplate
                .postForEntity("http://localhost:" + port + "/logout", response, String.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test
    void test_getFile200() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "41c10aef5b249a6b983c16c16fe2cf8d");
        final HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> entity = restTemplate
                .exchange("http://localhost:" + port + "/file?filename=test.txt", HttpMethod.GET, httpEntity, String.class);

        String response = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertEquals("{\"hash\":\"698d51a19d8a121ce581499d7b701668\",\"file\":\"\\\\x313131\"}", response);
    }
    @Test
    void test_getFile400() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "41c10aef5b249a6b983c16c16fe2cf8d");
        final HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> entity = restTemplate
                .exchange("http://localhost:" + port + "/file?filename=test1.txt", HttpMethod.GET, httpEntity, String.class);

        String response = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        Assertions.assertEquals("Error input data", response);
    }
    @Test
    void test_getFile401() {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("auth-token", "71c10aef5b249a6b983c16c16fe2cf8d");
        final HttpEntity<String> httpEntity = new HttpEntity<String>(headers);

        ResponseEntity<String> entity = restTemplate
                .exchange("http://localhost:" + port + "/file?filename=test.txt", HttpMethod.GET, httpEntity, String.class);

        String response = entity.getBody();
        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        Assertions.assertEquals("Unauthorized error", response);
    }
    @Test
    void test_getAllFiles() {
        ResponseEntity<String> entity = restTemplate
                .getForEntity("http://localhost:" + port + "/list?limit=5", String.class);
        String response = entity.getBody();

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(entity.getBody()).isNotNull();
        Assertions.assertEquals("[{\"filename\":\"test.txt\",\"size\":3}]", response);
    }
}

