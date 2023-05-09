package ru.netology.diplomback.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.diplomback.exception.DataError;
import ru.netology.diplomback.exception.UnauthorizedError;
import ru.netology.diplomback.model.*;
import ru.netology.diplomback.repository.DAORepository;
import ru.netology.diplomback.repository.FileRepository;
import ru.netology.diplomback.repository.UsersRepository;

import java.io.IOException;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    private final UsersRepository usersRepository;
    private final FileRepository fileRepository;
    private final DAORepository daoRepository;

    public FileServiceImpl(UsersRepository usersRepository, FileRepository fileRepository, DAORepository daoRepository) {
        this.usersRepository = usersRepository;
        this.fileRepository = fileRepository;
        this.daoRepository = daoRepository;
    }

    public String userAuthorization(UserInfo user) throws RuntimeException {
        if (user.equals(usersRepository.findUserByUserName(user.getLogin())))
            return new LoginOut(usersRepository.addAuthToken(user)).toString();
        else
            throw new DataError("Bad credentials");
    }

    public void userLogout(String authToken) {
        usersRepository.deleteAuthToken(authToken);
    }

    public void filePost(String authToken, String filename, MultipartFile multipartFile) throws RuntimeException, IOException {
        Long userId = usersRepository.findIdByAuthToken(authToken);
        if (userId == null)
            throw new UnauthorizedError("Unauthorized error");

        FileInfo fileInfo;
        try {
            fileInfo = new FileInfo(filename, multipartFile, userId);
        } catch (Exception e) {
            throw new DataError("Error input data");
        }

        fileRepository.post(fileInfo);
        if(fileRepository.filesWithHashCount(fileInfo.getHash())==1)
            daoRepository.upload(fileInfo);
    }

    public void fileDelete(String authToken, String fileName) throws RuntimeException, IOException {
        Long UserId = usersRepository.findIdByAuthToken(authToken);
        if (UserId == null)
            throw new UnauthorizedError("Unauthorized error");
        String hash = fileRepository.delete(fileName, UserId);
        if(fileRepository.filesWithHashCount(hash)==0)
            daoRepository.delete(hash);
    }

    public FileOut fileGet(String authToken, String fileName) throws RuntimeException, IOException {
        Long UserId = usersRepository.findIdByAuthToken(authToken);
        if (UserId == null)
            throw new UnauthorizedError("Unauthorized error");
        String hash = fileRepository.get(fileName);
        return daoRepository.download(hash);
    }

    public void filePut(String authToken, String fileName, String name) throws RuntimeException {
        Long UserId = usersRepository.findIdByAuthToken(authToken);
        if (UserId == null)
            throw new UnauthorizedError("Unauthorized error");
       fileRepository.put(fileName, name);
    }

    public List<FileNameOut> getFilesList(int limit) throws RuntimeException {
        return fileRepository.filesList(limit);
    }
}
