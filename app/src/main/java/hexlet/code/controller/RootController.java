package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.MainPage;
import hexlet.code.util.Constants;
import io.javalin.http.Context;

import java.util.Collections;

public class RootController {

    public static void mainPage(Context ctx) {
        BasePage page = new MainPage();
        page.setFlash(ctx.consumeSessionAttribute(Constants.FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(Constants.FLASH_TYPE));
        ctx.render("index.jte", Collections.singletonMap(Constants.PAGE, page));
    }

}
