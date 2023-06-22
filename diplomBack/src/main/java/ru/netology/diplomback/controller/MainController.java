package ru.netology.diplomback.controller;

import jakarta.transaction.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ru.netology.diplomback.model.*;
import ru.netology.diplomback.service.FileService;
import ru.netology.diplomback.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Transactional
@RestController
public class MainController {
    private final FileService fileService;
    private final UserService userService;
    private Long userId;
    public static final Logger logger = Logger.getLogger("outFile.txt");

    public MainController(UserService userService, FileService fileService) {
        this.userService = userService;
        this.fileService = fileService;
    }

    @PostMapping("/login")
    public String loginBack(@RequestBody Map<String, String> map) throws RuntimeException {
        logger.log(Level.INFO, "PostMapping /login login:" + map.get("login") + ", password:" + map.get("password"));
        return userService.userAuthentication(new UsersInfo(map.get("login"), map.get("password")));
    }

    @PostMapping("/logout")
    public void logoutBack(@RequestHeader("auth-token") String authToken) {
        logger.log(Level.INFO, "PostMapping /logout auth-token:" + authToken);
        userService.userLogout(authToken);
    }

    @PostMapping("/file")
    public void addFile(@RequestHeader("auth-token") String authToken,
                        @RequestParam(name = "filename") String fileName,
                        @RequestParam(name = "file") MultipartFile multipartFile) throws IOException {
        logger.log(Level.INFO, "PostMapping /file auth-token:" + authToken + ", filename:" + fileName);
        userId = userService.userAuthorization(authToken);
        fileService.fileSave(userId, fileName, multipartFile.getSize(), multipartFile.getBytes());
    }

    @DeleteMapping("/file")
    public void deleteFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam(name = "filename") String fileName) throws IOException {
        logger.log(Level.INFO, "DeleteMapping /file auth-token:" + authToken + ", filename:" + fileName);
        userId = userService.userAuthorization(authToken);
        fileService.fileDelete(userId, fileName);
    }

    @GetMapping("/file")
    public FileOut getFile(@RequestHeader("auth-token") String authToken,
                           @RequestParam(name = "filename") String fileName) throws IOException {
        logger.log(Level.INFO, "GetMapping /file auth-token:" + authToken + ", filename:" + fileName);
        userId = userService.userAuthorization(authToken);
        return fileService.fileGet(userId, fileName);
    }

    @PutMapping("/file")
    public void putFile(@RequestHeader("auth-token") String authToken,
                        @RequestParam(name = "filename") String fileName,
                        @RequestBody Map<String, String> map) {
        logger.log(Level.INFO, "PutMapping /file auth-token:" + authToken + ", filename:" + fileName + ", newfilename:" + map.get("filename"));
        userId = userService.userAuthorization(authToken);
        fileService.fileUpdate(userId, fileName, map.get("filename"));
    }

    @GetMapping("/list")
    public List<FileNameOut> getAllFiles(@RequestParam(name = "limit") int limit) {
        logger.log(Level.INFO, "GetMapping /list limit:" + limit);
        return fileService.getFilesList(limit);
    }
}