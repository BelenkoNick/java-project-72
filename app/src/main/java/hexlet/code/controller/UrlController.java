package hexlet.code.controller;

import hexlet.code.dto.BasePage;
import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.Constants;
import hexlet.code.util.Routes;
import hexlet.code.util.RoutesUtils;
import io.javalin.http.Context;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import hexlet.code.model.Url;
import io.javalin.http.HttpStatus;
import io.javalin.http.NotFoundResponse;
import static io.javalin.rendering.template.TemplateUtil.model;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class UrlController {

    public static void listUrls(Context ctx) throws SQLException {
        List<Url> urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> urlChecks = UrlCheckRepository.findLatestChecks();
        BasePage page = new UrlsPage(urls, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute(Constants.FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(Constants.FLASH_TYPE));
        ctx.render("urls/index.jte", model(Constants.PAGE, page));
    }

    public static void createUrl(Context ctx) throws SQLException {
        String inputUrl = ctx.formParam("url");
        URL parsedUrl;
        try {
            URI uri = new URI(inputUrl);
            parsedUrl = uri.toURL();
        } catch (Exception e) {
            ctx.sessionAttribute(Constants.FLASH, "Некорректный URL");
            ctx.sessionAttribute(Constants.FLASH_TYPE, Constants.DANGER);
            ctx.redirect(Routes.ROOT_PATH.getUrl());
            return;
        }

        String normalizedUrl = String
                .format(
                        "%s://%s%s",
                        parsedUrl.getProtocol(),
                        parsedUrl.getHost(),
                        parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
                )
                .toLowerCase();

        Url url = UrlRepository.findByName(normalizedUrl).orElse(null);

        if (url != null) {
            ctx.sessionAttribute(Constants.FLASH, "Страница уже существует");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "info");
        } else {
            Url newUrl = new Url();
            newUrl.setName(normalizedUrl);
            UrlRepository.save(newUrl);
            ctx.sessionAttribute(Constants.FLASH, "Страница успешно добавлена");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "success");
        }

        ctx.redirect(Routes.URLS_PATH.getUrl(), HttpStatus.forStatus(302));
    }

    public static void showUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = UrlRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));

        List<UrlCheck> urlChecks = UrlCheckRepository.findByUrlId(id);

        UrlPage page = new UrlPage(url, urlChecks);
        page.setFlash(ctx.consumeSessionAttribute(Constants.FLASH));
        page.setFlashType(ctx.consumeSessionAttribute(Constants.FLASH_TYPE));

        ctx.render("urls/show.jte", model(Constants.PAGE, page));
    }

    public static void checkUrl(Context ctx) throws SQLException {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

        Url url = UrlRepository.findById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));

        try {
            HttpResponse<String> response = Unirest.get(url.getName()).asString();
            Document doc = Jsoup.parse(response.getBody());

            int statusCode = response.getStatus();
            String title = doc.title();
            Element h1Element = doc.selectFirst("h1");
            String h1 = h1Element == null ? "" : h1Element.text();
            Element descriptionElement = doc.selectFirst("meta[name=description]");
            String description = descriptionElement == null ? "" : descriptionElement.attr("content");

            UrlCheck newUrlCheck = new UrlCheck(statusCode, title, h1, description);
            newUrlCheck.setUrlId(id);
            UrlCheckRepository.save(newUrlCheck);

            ctx.sessionAttribute(Constants.FLASH, "Страница успешно проверена");
            ctx.sessionAttribute(Constants.FLASH_TYPE, "success");
        } catch (UnirestException e) {
            ctx.sessionAttribute(Constants.FLASH, "Некорректный адрес");
            ctx.sessionAttribute(Constants.FLASH_TYPE, Constants.DANGER);
        } catch (Exception e) {
            ctx.sessionAttribute(Constants.FLASH, e.getMessage());
            ctx.sessionAttribute(Constants.FLASH_TYPE, Constants.DANGER);
        }

        ctx.redirect(String.format(RoutesUtils.urlPath(url.getId())));
    }
}
