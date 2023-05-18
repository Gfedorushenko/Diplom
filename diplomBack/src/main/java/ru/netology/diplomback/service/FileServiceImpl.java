package ru.netology.diplomback.service;

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
    public void filePost(Long userId, String filename, MultipartFile multipartFile) throws RuntimeException, IOException {
        FileInfo fileInfo;
        try {
            fileInfo = new FileInfo(filename, multipartFile.getSize(), multipartFile.getBytes(), userId);
        } catch (Exception e) {
            throw new DataError("Error input data");
        }
        fileRepository.post(fileInfo);
    }

    public void fileDelete(Long userId, String fileName) throws RuntimeException {
        if (fileRepository.fileExist(userId, fileName)) {
            fileRepository.delete(userId, fileName);
        } else
            throw new DataError("Error input data");
    }

    public FileOut fileGet(Long userId, String fileName) throws RuntimeException {
        if (fileRepository.fileExist(userId, fileName)) {
            return fileRepository.get(userId,fileName);
        } else
            throw new DataError("Error input data");
    }

    public void filePut(Long userId, String fileName, String name) throws RuntimeException {
        if (fileRepository.fileExist(userId, fileName)) {
            fileRepository.put(userId, fileName, name);
        } else
            throw new DataError("Error input data");
    }

    public List<FileNameOut> getFilesList(int limit) throws RuntimeException {
        return fileRepository.filesList(limit);
    }
}
