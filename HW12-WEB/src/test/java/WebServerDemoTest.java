import com.oneso.hwhibernate.core.service.ServiceUser;
import com.oneso.web.server.UsersWebServer;
import com.oneso.web.server.UsersWebServerImpl;
import com.oneso.web.services.AuthService;
import com.oneso.web.services.InitUserService;
import com.oneso.web.services.InitUserServiceImpl;
import com.oneso.web.services.TemplateProcessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.HttpCookie;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static utils.HttpUrlConnectionHelper.buildUrl;
import static utils.WebServerHelper.login;

@DisplayName("Web Server Demo should")
class WebServerDemoTest {

  private static final int WEB_SERVER_PORT = 8989;
  private static final String WEB_SERVER_URL = "http://localhost:" + WEB_SERVER_PORT + "/";
  private static final String LOGIN_URL = "login";

  private static final String DEFAULT_USER_LOGIN = "admin";
  private static final String DEFAULT_USER_PASSWORD = "admin";
  private static final String INCORRECT_USER_LOGIN = "-";

  private static UsersWebServer webServer;

  @BeforeAll
  static void setUp() throws Exception {
    TemplateProcessor templateProcessor = mock(TemplateProcessor.class);
    ServiceUser serviceUser = mock(ServiceUser.class);
    AuthService authService = mock(AuthService.class);
    InitUserService initUserService = new InitUserServiceImpl();

    given(authService.authenticate(DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(true);
    given(authService.authenticate(INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD)).willReturn(false);


    webServer = new UsersWebServerImpl(WEB_SERVER_PORT, templateProcessor, authService, serviceUser, initUserService);
    webServer.start();
  }

  @AfterAll
  static void tearDown() throws Exception {
    webServer.stop();
  }

  @Test
  @DisplayName("should return JSessionId when logging in with correct data")
  void shouldReturnJSessionIdWhenLoggingInWithCorrectData() throws Exception {
    HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), DEFAULT_USER_LOGIN, DEFAULT_USER_PASSWORD);
    assertThat(jSessionIdCookie).isNotNull();
  }

  @Test
  @DisplayName("should not return JSessionId when logging in with correct data")
  void shouldNotReturnJSessionIdWhenLoggingInWithIncorrectData() throws Exception {
    HttpCookie jSessionIdCookie = login(buildUrl(WEB_SERVER_URL, LOGIN_URL, null), INCORRECT_USER_LOGIN, DEFAULT_USER_PASSWORD);
    assertThat(jSessionIdCookie).isNull();
  }
}
