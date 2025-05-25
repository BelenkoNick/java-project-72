package hexlet.code.util;

public class PreparedStatements {

    public static final String URLS_INSERT = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
    public static final String URLS_SELECT_BY_ID = "SELECT * FROM urls WHERE id = ?";
    public static final String URLS_SELECT_BY_NAME = "SELECT * FROM urls WHERE name = ?";
    public static final String URLS_SELECT_ORDER_BY_ID = "SELECT * FROM urls ORDER BY id";
    public static final String URLS_CHECK_INSERT = "INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at) VALUES (?, ?, ?, ?, ?, ?)";
    public static final String URLS_CHECK_SELECT_BY_URL_ID = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY id DESC";
    public static final String URLS_CHECK_SELECT_DISTINCT_BY_URL_ID = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
}