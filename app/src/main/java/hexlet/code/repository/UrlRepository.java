package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.util.Constants;
import hexlet.code.util.PreparedStatements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws SQLException {
        Timestamp datetime = new Timestamp(System.currentTimeMillis());
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(
                     PreparedStatements.URLS_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, datetime);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
                url.setCreatedAt(datetime);
            } else {
                throw new SQLException("DB error happened");
            }
        }
    }

    public static Optional<Url> find(Long id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(PreparedStatements.URLS_SELECT_BY_ID)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp(Constants.CREATED_AT);
                Url url = new Url();
                url.setName(name);
                url.setId(id);
                url.setCreatedAt(createdAt);

                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findByName(String urlName) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(PreparedStatements.URLS_SELECT_BY_NAME)) {
            stmt.setString(1, urlName);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp(Constants.CREATED_AT);
                Long id = resultSet.getLong("id");
                Url url = new Url();
                url.setName(name);
                url.setId(id);
                url.setCreatedAt(createdAt);

                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static Optional<Url> findById(Long id) throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(PreparedStatements.URLS_SELECT_BY_ID)) {
            stmt.setLong(1, id);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp(Constants.CREATED_AT);
                Long urlId = resultSet.getLong("id");
                Url url = new Url();
                url.setName(name);
                url.setId(urlId);
                url.setCreatedAt(createdAt);

                return Optional.of(url);
            }
            return Optional.empty();
        }
    }

    public static List<Url> getEntities() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(PreparedStatements.URLS_SELECT_ORDER_BY_ID)) {
            ResultSet resultSet = stmt.executeQuery();
            List<Url> result = new ArrayList<>();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp(Constants.CREATED_AT);
                Url url = new Url();
                url.setName(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                result.add(url);
            }
            return result;
        }
    }

}
