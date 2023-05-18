package ru.netology.diplomback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;

@Data
public class FileInfo {

    @NonNull
    private String name;
    private Long size;
    private String hash;
    @NonNull
    private Long userId;
    private byte[] data;

    public FileInfo(String name, Long size, byte[] data, Long userId) {
        this.name = name;
        this.size = size;
        this.data = data;
        this.userId = userId;
        this.hash = generateHash(data);
    }

    private String generateHash(byte[] text) {
        return DigestUtils.md5Hex(text);
    }
}

