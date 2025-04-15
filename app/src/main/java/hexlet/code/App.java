package hexlet.code;

import hexlet.code.config.DBConfig;
import io.javalin.Javalin;

import java.io.IOException;
import java.sql.SQLException;

public class App {

    private static int getPort() {
        return Integer.parseInt(System.getenv().getOrDefault("PORT", "8080")); // for example
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");  // for local
    }


    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {

        DBConfig.init(getDatabaseUrl());

        var app = Javalin.create(config -> config.bundledPlugins.enableDevLogging());

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }
}
