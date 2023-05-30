package ru.netology.diplomback;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import java.util.stream.Stream;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class FileServiceTest {
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
    public void test_fileSave() throws IOException {
        Long userId = 3L;
        String fileName = "test.txt";
        Long size = 4L;
        byte[] data = "data".getBytes();
        FileInfo fileInfo = new FileInfo(fileName, size, data, userId,fileService.generateHash(data));
        MultipartFile multipartFile = new MockMultipartFile(fileName, data);

        fileService.fileSave(userId, fileName, multipartFile);
        Mockito.verify(fileRepository, Mockito.times(1)).save(fileInfo);
    }

    @ParameterizedTest
    @MethodSource("source")
    public void test_fileDelete(boolean fileExist, int count) {
        Long userId = 3L;
        String fileName = "test.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(fileExist);
        try {
            fileService.fileDelete(userId, fileName);
        } catch (Exception ignored) {
        }
        Mockito.verify(fileRepository, Mockito.times(count)).delete(userId, fileName);
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

    @ParameterizedTest
    @MethodSource("source")
    public void test_fileUpdate(boolean fileExist, int count) {
        Long userId = 3L;
        String fileName = "test.txt";
        String name = "111.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(fileExist);
        try {
            fileService.fileUpdate(userId, fileName, name);
        } catch (Exception ignored) {
        }
        Mockito.verify(fileRepository, Mockito.times(count)).update(userId, fileName, name);
    }

    private static Stream<Arguments> source() {
        return Stream.of(
                Arguments.of(true, 1),
                Arguments.of(false, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("source1")
    public void testException_fileSave(Long userId, String fileName, MultipartFile multipartFile, String expected) {
        String result = "Ok";

        try {
            fileService.fileSave(userId, fileName, multipartFile);
        } catch (Exception e) {
            result = e.getMessage();
        }
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> source1() {
        String fileName1 = "test.txt";
        return Stream.of(
                Arguments.of(3L, fileName1, new MockMultipartFile(fileName1, "data".getBytes()), "Ok"),
                Arguments.of(null, fileName1, new MockMultipartFile(fileName1, "data".getBytes()), "Ok")//TODO
        );
    }

    @ParameterizedTest
    @MethodSource("source2")
    public void testException_fileDelete(boolean fileExist, String expected) {
        String result = "Ok";
        Long userId = 3L;
        String fileName = "test.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(fileExist);
        try {
            fileService.fileDelete(userId, fileName);
        } catch (Exception e) {
            result = e.getMessage();
        }
        Assertions.assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("source2")
    public void testException_fileGet(boolean fileExist, String expected) {
        String result = "Ok";
        Long userId = 3L;
        String fileName = "test.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(fileExist);
        try {
            fileService.fileGet(userId, fileName);
        } catch (Exception e) {
            result = e.getMessage();
        }
        Assertions.assertEquals(expected, result);
    }

    @ParameterizedTest
    @MethodSource("source2")
    public void testException_fileUpdate(boolean fileExist, String expected) {
        String result = "Ok";
        Long userId = 3L;
        String fileName = "test.txt";
        String name = "111.txt";

        Mockito.when(fileRepository.fileExist(userId, fileName)).thenReturn(fileExist);
        try {
            fileService.fileUpdate(userId, fileName, name);
        } catch (Exception e) {
            result = e.getMessage();
        }
        Assertions.assertEquals(expected, result);
    }

    private static Stream<Arguments> source2() {
        return Stream.of(
                Arguments.of(true, "Ok"),
                Arguments.of(false, "Error input data")
        );
    }
}

