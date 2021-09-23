import javax.servlet.http.Cookie;

public static void createCookie() {
  Cookie cookie = new Cookie("name", "value");
    cookie.setMaxAge(60 * 60 * 24 * 365);
    cookie.setPath("/");
    response.addCookie(cookie);
}