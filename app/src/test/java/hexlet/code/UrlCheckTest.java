package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import io.javalin.testtools.JavalinTest;
import okhttp3.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UrlCheckTest extends BaseTest {

    @Test
    void testStore() {
        String url = mockServer.url("/").toString().replaceAll("/$", "");

        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=" + url;
            assertThat(client.post("/urls", requestBody).code()).isEqualTo(200);

            Url actualUrl = UrlRepository.findByName(url).orElse(null);

            assertThat(actualUrl).isNotNull();
            assertThat(actualUrl.getName()).isEqualTo(url);

            client.post("/urls/" + actualUrl.getId() + "/checks");

            Response responce = client.get("/urls/" + actualUrl.getId());
            assertThat(responce.code()).isEqualTo(200);
            assertThat(responce.body().string()).contains(url);

            UrlCheck actualCheckUrl = UrlCheckRepository
                    .findLatestChecks().get(actualUrl.getId());

            assertThat(actualCheckUrl).isNotNull();
            assertThat(actualCheckUrl.getStatusCode()).isEqualTo(200);
            assertThat(actualCheckUrl.getTitle()).isEqualTo("Test page");
            assertThat(actualCheckUrl.getH1()).isEqualTo("Do not expect a miracle, miracles yourself!");
            assertThat(actualCheckUrl.getDescription()).contains("statements of great people");
        });
    }
}
