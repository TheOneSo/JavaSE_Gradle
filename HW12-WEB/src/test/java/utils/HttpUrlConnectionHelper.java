package utils;

import org.eclipse.jetty.http.HttpMethod;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public final class HttpUrlConnectionHelper {

  public static HttpURLConnection sendRequest(String url, HttpMethod method) throws Exception {
    URL obj = new URL(url);
    HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
    connection.setRequestMethod(method.asString());
    return connection;
  }

  public static String buildUrl(String host, String path, List<String> pathParams) {
    StringBuilder stringBuilder = new StringBuilder(host);
    stringBuilder.append(path);
    Optional.ofNullable(pathParams).ifPresent(params -> params.forEach(p -> stringBuilder.append("/").append(p)));
    return stringBuilder.toString();
  }
}
