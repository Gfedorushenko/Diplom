package ru.netology.diplomback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class FileInfo {
    private String name;
    private Long size;
    private String hash;
    private String contentType;
    private Long userId;
    private byte[] data;

    public FileInfo(String name, MultipartFile multipartFile, Long userId) throws IOException {
        this.name = name;
        this.size = multipartFile.getSize();
        this.data = multipartFile.getBytes();
        this.contentType = multipartFile.getContentType();
        this.userId = userId;
        this.hash = generateHash(data);
    }

    private String generateHash(byte[] text) {
        return DigestUtils.md5Hex(text);
    }
}

