package ru.netology.diplomback.service;

import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomback.model.*;

import java.io.IOException;
import java.util.List;

public interface FileService {

    List<FileNameOut> getFilesList(int limit) throws RuntimeException;

    void fileSave(Long userId, String filename, Long size,byte[] data) throws RuntimeException, IOException;

    void fileDelete(Long userId, String fileName) throws RuntimeException, IOException;

    FileOut fileGet(Long userId, String fileName) throws RuntimeException, IOException;

    void fileUpdate(Long userId, String fileName, String name) throws RuntimeException;

    String generateHash(byte[] text);
}
