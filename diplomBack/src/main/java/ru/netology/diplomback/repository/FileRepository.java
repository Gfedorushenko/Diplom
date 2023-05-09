package ru.netology.diplomback.repository;

import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileNameOut;

import java.io.IOException;
import java.util.List;

public interface FileRepository {
    void post(FileInfo fileInfo) throws IOException;

    String delete(String file, Long userId);

    String get(String file);

    void put(String fileName, String name);

    List<FileNameOut> filesList(int limit);

    int filesWithHashCount(String fileHash);
}
