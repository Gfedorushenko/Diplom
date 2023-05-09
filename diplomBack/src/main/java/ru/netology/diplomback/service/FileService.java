package ru.netology.diplomback.service;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomback.model.*;

import java.io.IOException;
import java.util.List;

public interface FileService {
    String userAuthorization(UserInfo user) throws RuntimeException;

    void userLogout(String authToken);

    List<FileNameOut> getFilesList(int limit) throws RuntimeException;

    void filePost(String authToken, String filename, MultipartFile file) throws RuntimeException, IOException;

    void fileDelete(String authToken, String fileName) throws RuntimeException, IOException;

    FileOut fileGet(String authToken, String fileName) throws RuntimeException, IOException;

    void filePut(String authToken, String fileName, String name) throws RuntimeException;
}
