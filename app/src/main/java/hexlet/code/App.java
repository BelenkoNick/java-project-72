package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {

    public static void main(String[] args){
        Javalin app = getApp();

        app.start(Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"))); // for example
    }

    public static Javalin getApp() {

        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
        });

        app.get("/", ctx -> ctx.result("Hello World"));

        return app;
    }
}
