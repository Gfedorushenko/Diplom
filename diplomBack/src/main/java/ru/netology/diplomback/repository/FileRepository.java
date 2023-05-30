package ru.netology.diplomback.repository;

import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileNameOut;
import ru.netology.diplomback.model.FileOut;

import java.io.IOException;
import java.util.List;

public interface FileRepository {
    void save(FileInfo fileInfo) throws IOException;

    void delete(Long userId,String fileName);

    FileOut get(Long userId, String fileName);

    void update(Long userId,String fileName, String name);

    List<FileNameOut> filesList(int limit);

    boolean fileExist(Long userId, String fileName);
}
