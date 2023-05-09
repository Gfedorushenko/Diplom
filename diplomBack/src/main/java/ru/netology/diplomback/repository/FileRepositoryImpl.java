package ru.netology.diplomback.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.netology.diplomback.exception.DataError;
import ru.netology.diplomback.model.FileInfo;
import ru.netology.diplomback.model.FileNameOut;

import javax.sql.DataSource;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FileRepositoryImpl implements FileRepository {
    private static final String ADD_NEW_FILE = "INSERT INTO ddl.files(file_name, file_size, file_hash, content_type, upload_date, user_id) VALUES (:filename, :filesize,:filehash,:contenttype,:uploaddate,:userid)";
    private static final String DELETE_FILE_BY_NAME = "DELETE FROM ddl.files WHERE file_name =:filename";
    private static final String SELECT_FILES_WITH_LIMIT = "SELECT file_name, file_size from ddl.files LIMIT :limit";
    private static final String SELECT_ID_BY_NAME = "SELECT id from ddl.files where file_name=:filename";
    private static final String SELECT_HASH_BY_NAME = "SELECT file_hash from ddl.files where file_name=:filename";
    private static final String FIND_FILE_INFO_BY_NAME = "SELECT file_name, file_size, file_hash, upload_date, user_id from ddl.files where file_name=:filename";
    private static final String UPDATE_FILE_NAME = "UPDATE ddl.files set file_name=:name where file_name=:filename";
    private static final String COUNT_BY_HASH = "SELECT count(*) from ddl.files where file_hash=:filehash";

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
        parameters.put("contenttype", fileInfo.getContentType());
        parameters.put("uploaddate", Date.valueOf(uploadDate));
        parameters.put("userid", fileInfo.getUserId());
        SqlParameterSource paramSource = new MapSqlParameterSource(parameters);
        namedParameterJdbcTemplate.update(ADD_NEW_FILE, paramSource, keyHolder);
    }

    public String delete(String fileName, Long userId) throws RuntimeException {
        if (findIdByName(fileName) == null)
            throw new DataError("Error input data");

        Map parameters = new HashMap();
        parameters.put("filename", fileName);
        String hash = namedParameterJdbcTemplate.queryForObject(SELECT_HASH_BY_NAME, parameters, String.class);
        namedParameterJdbcTemplate.update(DELETE_FILE_BY_NAME, parameters);
        return hash;
    }

    public String get(String fileName) throws RuntimeException {
        if (findIdByName(fileName) == null)
            throw new DataError("Error input data");
        Map parameters = new HashMap();
        parameters.put("filename", fileName);
        return namedParameterJdbcTemplate.queryForObject(SELECT_HASH_BY_NAME, parameters, String.class);
    }

    public void put(String fileName, String name) throws RuntimeException {
        if (findIdByName(fileName) == null || name == null)
            throw new DataError("Error input data");

        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("filename", fileName);
        paramMap.put("name", name);
        namedParameterJdbcTemplate.update(UPDATE_FILE_NAME, paramMap);
    }

    public List<FileNameOut> filesList(int limit) {
        Map<String, Integer> paramMap = new HashMap<String, Integer>();
        paramMap.put("limit", limit);
        return namedParameterJdbcTemplate.query(SELECT_FILES_WITH_LIMIT, paramMap, (rs, rowNum) -> {
            FileNameOut fileOut = new FileNameOut();
            fileOut.setFilename(rs.getString("file_name"));
            fileOut.setSize(rs.getInt("file_size"));
            return fileOut;
        });
    }

    public Long findIdByName(String fileName) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("filename", fileName);
        return namedParameterJdbcTemplate.queryForObject(SELECT_ID_BY_NAME, paramMap, Long.class);
    }

    public int filesWithHashCount(String fileHash) {
        Map parameters = new HashMap();
        parameters.put("filehash", fileHash);
        return namedParameterJdbcTemplate.queryForObject(COUNT_BY_HASH, parameters, int.class);
    }

    private RowMapper<FileInfo> rowMapper() {
        return (rs, rowNum) -> FileInfo.builder()
                .name(rs.getString("file_name"))
                .size(rs.getLong("file_size"))
                .hash(rs.getString("file_hash"))
                .userId(rs.getLong("user_id"))
                .build();
    }
}
