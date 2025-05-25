package hexlet.code.util;

public class RoutesUtils {

    public static String urlPath(String id) {
        return String.format(Routes.URLS_PATH_WITH_ID.getUrl(), id);
    }

    public static String urlChecksPath(String id) {
        return String.format(Routes.URLS_CHECK_PATH.getUrl(), id);
    }

}
