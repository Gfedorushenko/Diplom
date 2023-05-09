package ru.netology.diplomback.repository;

import org.springframework.stereotype.Repository;
import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileOut;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
public class DAORepositoryImpl implements DAORepository  {
    String DIRECTORY_PATH = "D://Games/";
    public void upload(FileInfo fileInfo) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH, fileInfo.getHash());
        Path file = Files.createFile(path);
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file.toString());
            stream.write(fileInfo.getData());
        } finally {
            stream.close();
        }
    }

    public void delete(String hash) throws IOException {
        Path path = Paths.get(DIRECTORY_PATH + hash);
        Files.delete(path);
    }

    public FileOut download(String hash) throws IOException {
        FileInputStream inputStream = new FileInputStream(DIRECTORY_PATH+hash);
        byte[] buffer = new byte[10000];
        while (inputStream.available() > 0)
        {
            int count = inputStream.read(buffer);
        }
        inputStream.close();
        return new FileOut(hash,new String(buffer));
    }
}
