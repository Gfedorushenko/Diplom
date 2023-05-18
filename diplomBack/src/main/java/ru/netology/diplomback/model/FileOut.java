package ru.netology.diplomback.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileOut {
    private String hash;
    private String file;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileOut fileOut)) return false;
        return Objects.equals(hash, fileOut.hash) && Objects.equals(file, fileOut.file);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash, file);
    }
}
