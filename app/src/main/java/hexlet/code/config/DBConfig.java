package hexlet.code.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.Utils;
import hexlet.code.repository.BaseRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class DBConfig {

    public static void init(String databaseUrl, String user, String pass) throws IOException, SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(databaseUrl);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(pass);

        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        String schema = Utils.readResourceFile("schema.sql");

        log.info(schema);
        try (Statement statement = dataSource.getConnection().createStatement()) {
            statement.execute(schema);
        }
        BaseRepository.dataSource = dataSource;
    }
}
