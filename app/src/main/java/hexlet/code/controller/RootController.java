package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.MainPage;
import io.javalin.http.Context;

import java.util.Collections;

public class RootController {

    public static void mainPage(Context ctx) {
        BasePage page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashType(ctx.consumeSessionAttribute("flash-type"));
        ctx.render("index.jte", Collections.singletonMap("page", page));
    };

}