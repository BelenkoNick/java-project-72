package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlTest extends BaseTest {

    @Test
    void testIndex() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/urls").code()).isEqualTo(200);
        });
    }

    @Test
    void testShow() throws SQLException {
        Url url = new Url();
        url.setName("http://test.io");
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            Response responce = client.get("/urls/" + url.getId());
            assertThat(responce.code()).isEqualTo(200);
        });
    }

    @Test
    void testStore() throws SQLException {
        String inputUrl = "https://ru.hexlet.io";

        JavalinTest.test(app, (server, client) -> {
            String requestBody = "url=" + inputUrl;
            Response response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
        });

        Url actualUrl = UrlRepository.findByName(inputUrl).orElse(null);

        assertThat(actualUrl).isNotNull();
        assertThat(actualUrl.getName()).isEqualTo(inputUrl);
    }

    @Test
    void testStoreInvalidUrl() {
        String badUrl = "ht!tp://bad_url";

        JavalinTest.test(app, (server, client) -> {
            String requestBody = "url=" + badUrl;
            Response response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);

            // Проверяем, что в базе нет такой записи
            var urlFromDb = UrlRepository.findByName(badUrl);
            assertThat(urlFromDb).isEmpty();
        });
    }
}
