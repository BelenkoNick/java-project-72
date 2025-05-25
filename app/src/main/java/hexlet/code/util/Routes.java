package hexlet.code.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Routes {
    ROOT_PATH("/"),
    URLS_PATH("/urls"),
    URLS_PATH_WITH_ID("/urls/%s"),
    URLS_CHECK_PATH("/urls/%s/checks");

    private final String url;
}
