package ru.netology.diplomback.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ru.netology.diplomback.model.*;
import ru.netology.diplomback.repository.UsersRepository;
import ru.netology.diplomback.service.FileService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Transactional
@RestController
public class FileController {
    @Autowired
    UsersRepository usersRepository;
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/login")
    public String loginBack(@RequestBody Map<String, String> map) throws RuntimeException {  //TODO ??? cразу читать как UserInfo
        return fileService.userAuthorization(new UserInfo(map.get("login"), map.get("password")));
    }

    @PostMapping("/logout")
    public void logoutBack(@RequestHeader("auth-token") String authToken) {
        fileService.userLogout(authToken);
    }

    @GetMapping("/list")
    public List<FileNameOut> getAllFiles(//@RequestParam(name = "auth-token") String authToken, //Error По описанию есть Фактически нет
                                         @RequestParam(name = "limit") int limit) {
        return fileService.getFilesList(limit);
    }

    @PostMapping("/file")
    public void addFile(@RequestHeader("auth-token") String authToken,
                        @RequestParam("filename") String fileName,
                        @RequestParam(name = "file") MultipartFile multipartFile) throws IOException {
        fileService.filePost(authToken, fileName, multipartFile);
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam("filename") String fileName) throws IOException {
        fileService.fileDelete(authToken, fileName);
    }

    @GetMapping("/file")
    public FileOut getFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam("filename") String fileName) throws IOException {
        return fileService.fileGet(authToken, fileName);
    }

    @PutMapping("/file")
    public void putFile(@RequestHeader("auth-token") String authToken,
                        @RequestParam("filename") String fileName,
                        @RequestBody Map<String, String> map) {
        fileService.filePut(authToken, fileName, map.get("filename"));//Error по описанию name
    }
}