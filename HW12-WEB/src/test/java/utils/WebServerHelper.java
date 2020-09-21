package utils;

import org.eclipse.jetty.http.HttpMethod;

import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.util.List;

import static utils.HttpUrlConnectionHelper.sendRequest;

public final class WebServerHelper {

  private static final String COOKIE_NAME_JSESSIONID = "JSESSIONID";

  public static HttpCookie login(String url, String login, String password) throws Exception {
    CookieManager cookieManager = new CookieManager();
    CookieManager.setDefault(cookieManager);

    HttpURLConnection connection = sendRequest(url, HttpMethod.POST);
    try {
      connection.setDoOutput(true);
      try(OutputStream os = connection.getOutputStream()) {
        os.write(String.format("login=%s&password=%s", login, password).getBytes());
        os.flush();
      }

      int responseCode = connection.getResponseCode();
      if(responseCode == HttpURLConnection.HTTP_OK) {
        List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
        return cookies.stream().filter(c -> c.getName().equalsIgnoreCase(COOKIE_NAME_JSESSIONID)).findFirst().orElse(null);
      }
    } finally {
      connection.disconnect();
    }
    return null;
  }
}
