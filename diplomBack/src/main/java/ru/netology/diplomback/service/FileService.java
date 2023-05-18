package ru.netology.diplomback.service;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomback.model.*;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileNameOut> getFilesList(int limit) throws RuntimeException;

    void filePost(Long userId, String filename, MultipartFile multipartFile) throws RuntimeException, IOException;

    void fileDelete(Long userId, String fileName) throws RuntimeException, IOException;

    FileOut fileGet(Long userId, String fileName) throws RuntimeException, IOException;

    void filePut(Long userId, String fileName, String name) throws RuntimeException;
}
