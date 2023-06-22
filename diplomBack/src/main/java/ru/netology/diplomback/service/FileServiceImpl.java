package ru.netology.diplomback.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import ru.netology.diplomback.exception.DataError;
import ru.netology.diplomback.model.*;
import ru.netology.diplomback.repository.FileRepository;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static ru.netology.diplomback.controller.MainController.logger;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    public void fileSave(Long userId, String filename, Long size,byte[] data) throws RuntimeException, IOException {
        FileInfo fileInfo;
        try {
            fileInfo = new FileInfo(filename, size, data, userId,generateHash(data));
        } catch (Exception e) {
            logger.log(Level.WARNING,"fileSave: Error input data");
            throw new DataError("Error input data");
        }
        fileRepository.save(fileInfo);
        logger.log(Level.INFO,"fileSave: Ok");
    }

    public void fileDelete(Long userId, String fileName) throws RuntimeException {
        if (!fileRepository.fileExist(userId, fileName)) {
            logger.log(Level.WARNING,"fileDelete: Error input data");
            throw new DataError("Error input data");
        }
        fileRepository.delete(userId, fileName);
        logger.log(Level.INFO,"fileDelete: Ok");
    }

    public FileOut fileGet(Long userId, String fileName) throws RuntimeException {
        if (!fileRepository.fileExist(userId, fileName)) {
            logger.log(Level.WARNING,"fileGet: Error input data");
            throw new DataError("Error input data");
        }
        logger.log(Level.INFO,"fileGet: Ok");
        return fileRepository.get(userId,fileName);
    }

    public void fileUpdate(Long userId, String fileName, String name) throws RuntimeException {
        if (!fileRepository.fileExist(userId, fileName)) {
            logger.log(Level.WARNING,"fileUpdate: Error input data");
            throw new DataError("Error input data");
        }
        fileRepository.update(userId, fileName, name);
        logger.log(Level.INFO,"fileUpdate: Ok");
    }

    public List<FileNameOut> getFilesList(int limit) throws RuntimeException {
        return fileRepository.filesList(limit);
    }

    public String generateHash(byte[] text) {
        return DigestUtils.md5Hex(text);
    }
}
