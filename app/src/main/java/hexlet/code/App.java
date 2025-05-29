package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.config.DBConfig;
import hexlet.code.controller.RootController;
import hexlet.code.controller.UrlController;
import hexlet.code.util.Routes;
import hexlet.code.util.RoutesUtils;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;

import java.io.IOException;
import java.sql.SQLException;

public class App {

    private static int getPort() {
        return Integer.parseInt(System.getenv().getOrDefault("PORT", "8080")); // for example
    }

    private static String getDatabaseUrl() {
        return System.getenv().getOrDefault("JDBC_DATABASE_URL", "jdbc:h2:mem:project");  // for local
    }

    private static String getDatabaseUser() {
        return System.getenv().getOrDefault("DATABASE_USER", "");
    }

    private static String getDatabasePass() {
        return System.getenv().getOrDefault("DATABASE_PASS", "");
    }

    public static void main(String[] args) throws SQLException, IOException {
        Javalin app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() throws SQLException, IOException {
        DBConfig.init(getDatabaseUrl(), getDatabaseUser(), getDatabasePass());

        Javalin app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));
        });

        app.get("/", RootController::mainPage);
        app.get(Routes.URLS_PATH.getUrl(), UrlController::listUrls);
        app.post(Routes.URLS_PATH.getUrl(), UrlController::createUrl);
        app.get(RoutesUtils.urlPath("{id}"), UrlController::showUrl);
        app.post(RoutesUtils.urlChecksPath("{id}"), UrlController::checkUrl);


        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }
}
