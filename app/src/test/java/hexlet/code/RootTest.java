package hexlet.code;

import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RootTest extends BaseTest {

    @Test
    void testIndex() {
        JavalinTest.test(app, (server, client) -> {
            assertThat(client.get("/").code()).isEqualTo(200);
        });
    }
}
