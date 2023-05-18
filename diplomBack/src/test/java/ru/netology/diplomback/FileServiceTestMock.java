package ru.netology.diplomback;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileNameOut;
import ru.netology.diplomback.model.FileOut;
import ru.netology.diplomback.repository.FileRepository;
import ru.netology.diplomback.service.FileServiceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FileServiceTestMock {
    @Mock
    FileRepository fileRepository;
    @InjectMocks
    FileServiceImpl fileService;

    @Test
    public void test_getFilesList() {
        int limit = 3;
        String fileName = "test.txt";
        int size = 3;
        List<FileNameOut> list = new ArrayList<>();
        list.add(new FileNameOut(fileName, size));

        Mockito.when(fileRepository.filesList(limit)).thenReturn(list);

        List<FileNameOut> result = fileService.getFilesList(limit);
        Assertions.assertEquals(list, result);
    }

    @Test
    public void test_filePost() throws IOException {
        Long userId = 3L;
        String fileName = "test.txt";
        Long size = 4L;
        byte[] data = "data".getBytes();
        FileInfo fileInfo = new FileInfo(fileName, size, data, userId);
        MultipartFile multipartFile = new MockMultipartFile(fileName, data);

        fileService.filePost(userId, fileName, multipartFile);
        Mockito.verify(fileRepository, Mockito.times(1)).post(fileInfo);
    }

    @Test
    public void test_fileDelete() {
        Long userId = 3L;
        String fileName = "test.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(true);

        fileService.fileDelete(userId, fileName);
        Mockito.verify(fileRepository, Mockito.times(1)).delete(userId, fileName);
    }

    @Test
    public void test_fileGet() {
        Long userId = 3L;
        String fileName = "test.txt";
        FileOut file = new FileOut("1111", "123");

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(true);
        Mockito.when(fileRepository.get(userId, fileName)).thenReturn(file);

        FileOut result = fileService.fileGet(userId, fileName);
        Assertions.assertEquals(file, result);
    }

    @Test
    public void test_filePut() {
        Long userId = 3L;
        String fileName = "test.txt";
        String name = "111.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(true);

        fileService.filePut(userId, fileName, name);
        Mockito.verify(fileRepository, Mockito.times(1)).put(userId, fileName, name);
    }
}
