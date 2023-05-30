package ru.netology.diplomback.service;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomback.exception.DataError;
import ru.netology.diplomback.model.*;
import ru.netology.diplomback.repository.FileRepository;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    public void fileSave(Long userId, String filename, MultipartFile multipartFile) throws RuntimeException, IOException {
        FileInfo fileInfo;
        try {
            fileInfo = new FileInfo(filename, multipartFile.getSize(), multipartFile.getBytes(), userId,generateHash(multipartFile.getBytes()));
        } catch (Exception e) {
            throw new DataError("Error input data");
        }
        fileRepository.save(fileInfo);
    }

    public void fileDelete(Long userId, String fileName) throws RuntimeException {
        if (!fileRepository.fileExist(userId, fileName)) {
            throw new DataError("Error input data");
        }
        fileRepository.delete(userId, fileName);
    }

    public FileOut fileGet(Long userId, String fileName) throws RuntimeException {
        if (!fileRepository.fileExist(userId, fileName)) {
            throw new DataError("Error input data");
        }
        return fileRepository.get(userId,fileName);
    }

    public void fileUpdate(Long userId, String fileName, String name) throws RuntimeException {
        if (!fileRepository.fileExist(userId, fileName)) {
            throw new DataError("Error input data");
        }
        fileRepository.update(userId, fileName, name);
    }

    public List<FileNameOut> getFilesList(int limit) throws RuntimeException {
        return fileRepository.filesList(limit);
    }

    public String generateHash(byte[] text) {
        return DigestUtils.md5Hex(text);
    }
}
