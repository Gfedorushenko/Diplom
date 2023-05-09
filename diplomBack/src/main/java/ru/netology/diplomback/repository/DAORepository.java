package ru.netology.diplomback.repository;

import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileOut;


import java.io.IOException;

public interface DAORepository {
    void upload(FileInfo fileInfo) throws IOException;

    void delete(String hash) throws IOException;

    FileOut download(String hash) throws IOException;
}
