package ru.netology.diplomback.model;

import lombok.*;

import java.util.Arrays;
import java.util.Objects;

@Getter
public class FileInfo {

    @NonNull
    private String name;
    private Long size;
    private String hash;
    @NonNull
    private Long userId;
    private byte[] data;

    public FileInfo(String name, Long size, byte[] data, Long userId, String hash) {
        this.name = name;
        this.size = size;
        this.data = data;
        this.userId = userId;
        this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileInfo fileInfo)) return false;
        return name.equals(fileInfo.name) && Objects.equals(size, fileInfo.size) && Objects.equals(hash, fileInfo.hash) && userId.equals(fileInfo.userId) && Arrays.equals(data, fileInfo.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, size, hash, userId);
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }
}

