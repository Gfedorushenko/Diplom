package ru.netology.diplomback.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileNameOut;
import ru.netology.diplomback.model.FileOut;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FileRepositoryImpl implements FileRepository {
    private static final String ADD_NEW_FILE = "INSERT INTO filestorage.files(file_name, file_size, file_hash,file_data, upload_date, user_id) VALUES (:filename, :filesize,:filehash,:filedata,:uploaddate,:userid)";
    private static final String DELETE_FILE_BY_ID_AND_NAME = "DELETE FROM filestorage.files WHERE user_id=:userid and file_name =:filename";
    private static final String COUNT_BY_ID_AND_NAME = "SELECT count(*) from filestorage.files where user_id=:userid and file_name =:filename";
    private static final String UPDATE_FILE_BY_ID_AND_NAME = "UPDATE filestorage.files set file_name=:name where user_id=:userid and file_name=:filename";
    private static final String SELECT_HASH_BY_ID_AND_NAME = "SELECT file_hash, file_data from filestorage.files where user_id=:userid and file_name=:filename";
    private static final String SELECT_FILES_WITH_LIMIT = "SELECT file_name, file_size from filestorage.files LIMIT :limit";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FileRepositoryImpl(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void post(FileInfo fileInfo) throws RuntimeException {
        LocalDate uploadDate = LocalDate.now();
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        Map parameters = new HashMap();
        parameters.put("filename", fileInfo.getName());
        parameters.put("filesize", fileInfo.getSize());
        parameters.put("filehash", fileInfo.getHash());
        parameters.put("filedata", fileInfo.getData());
        parameters.put("uploaddate", Date.valueOf(uploadDate));
        parameters.put("userid", fileInfo.getUserId());
        SqlParameterSource paramSource = new MapSqlParameterSource(parameters);
        namedParameterJdbcTemplate.update(ADD_NEW_FILE, paramSource, keyHolder);
    }

    public void delete(Long userId, String fileName) {
        Map parameters = new HashMap();
        parameters.put("userid", userId);
        parameters.put("filename", fileName);
        namedParameterJdbcTemplate.update(DELETE_FILE_BY_ID_AND_NAME, parameters);
    }

    public FileOut get(Long userId, String fileName) throws RuntimeException {
        Map parameters = new HashMap();
        parameters.put("userid", userId);
        parameters.put("filename", fileName);
        return namedParameterJdbcTemplate.queryForObject(SELECT_HASH_BY_ID_AND_NAME, parameters, rowMapper());
    }

    public void put(Long userId, String fileName, String name) throws RuntimeException {
        Map parameters = new HashMap();
        parameters.put("userid", userId);
        parameters.put("filename", fileName);
        parameters.put("name", name);
        namedParameterJdbcTemplate.update(UPDATE_FILE_BY_ID_AND_NAME, parameters);
    }

    public List<FileNameOut> filesList(int limit) {
        Map parameters = new HashMap();
        parameters.put("limit", limit);
        return namedParameterJdbcTemplate.query(SELECT_FILES_WITH_LIMIT, parameters, (rs, rowNum) -> {
            FileNameOut fileOut = new FileNameOut();
            fileOut.setFilename(rs.getString("file_name"));
            fileOut.setSize(rs.getInt("file_size"));
            return fileOut;
        });
    }

    public boolean fileExist(Long userId, String fileName) {
        Map parameters = new HashMap();
        parameters.put("userid", userId);
        parameters.put("filename", fileName);
        return namedParameterJdbcTemplate.queryForObject(COUNT_BY_ID_AND_NAME, parameters, int.class) > 0;
    }

    private RowMapper<FileOut> rowMapper() {
        return (rs, rowNum) -> FileOut.builder()
                .hash(rs.getString("file_hash"))
                .file(rs.getString("file_data"))
                .build();
    }
}
