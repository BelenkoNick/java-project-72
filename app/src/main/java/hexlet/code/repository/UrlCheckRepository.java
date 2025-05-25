package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import hexlet.code.util.PreparedStatements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlCheckRepository extends BaseRepository {
    
    public static void save(UrlCheck check) throws SQLException {
        Timestamp datetime = new Timestamp(System.currentTimeMillis());
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(PreparedStatements.URLS_CHECK_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, check.getUrlId());
            preparedStatement.setInt(2, check.getStatusCode());
            preparedStatement.setString(3, check.getH1());
            preparedStatement.setString(4, check.getTitle());
            preparedStatement.setString(5, check.getDescription());
            preparedStatement.setTimestamp(6, datetime);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                check.setId(generatedKeys.getLong(1));
                check.setCreatedAt(datetime);
            } else {
                throw new SQLException("DB error happened");
            }
        }
    }

    public static List<UrlCheck> findByUrlId(long urlId) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(PreparedStatements.URLS_CHECK_SELECT_BY_URL_ID)) {
            stmt.setLong(1, urlId);
            ResultSet resultSet = stmt.executeQuery();
            List result = new ArrayList<UrlCheck>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Integer statusCode = resultSet.getInt("status_code");
                String title = resultSet.getString("title");
                String h1 = resultSet.getString("h1");
                String description = resultSet.getString("description");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                UrlCheck check = new UrlCheck(statusCode, title, h1, description);
                check.setId(id);
                check.setUrlId(urlId);
                check.setCreatedAt(createdAt);
                result.add(check);
            }
            return result;
        }
    }

    public static Map<Long, UrlCheck> findLatestChecks() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(PreparedStatements.URLS_CHECK_SELECT_DISTINCT_BY_URL_ID)) {
            ResultSet resultSet = stmt.executeQuery();
            Map result = new HashMap<Long, UrlCheck>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long urlId = resultSet.getLong("url_id");
                Integer statusCode = resultSet.getInt("status_code");
                String title = resultSet.getString("title");
                String h1 = resultSet.getString("h1");
                String description = resultSet.getString("description");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                UrlCheck check = new UrlCheck(statusCode, title, h1, description);
                check.setId(id);
                check.setUrlId(urlId);
                check.setCreatedAt(createdAt);
                result.put(urlId, check);
            }
            return result;
        }
    }

}
