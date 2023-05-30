package ru.netology.diplomback;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.netology.diplomback.model.UserInfo;

//@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileIntagrationTest{
    @Autowired
    TestRestTemplate restTemplate;

    @Container
    private static final GenericContainer<?> myAppDev = new GenericContainer<>("backapp:latest")
            .withExposedPorts(8081)//8081
            .withNetwork(Network.newNetwork());

    @BeforeAll
    public static void setUp() {
        myAppDev.start();
    }

    @Test
    void contextLoads() {
        ResponseEntity<String> forEntityDev = restTemplate.getForEntity("http://localhost:" + myAppDev.getMappedPort(8081)+"/ok", String.class);
        Assertions.assertEquals("ok", forEntityDev.getBody());
    }

}
