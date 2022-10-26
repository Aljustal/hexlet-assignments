package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;

class AppTest {
    private static int port;
    private static String hostname = "localhost";
    private static Tomcat app;
    private static String baseUrl;

    @BeforeAll
    public static void setup() throws LifecycleException, IOException {
        app = App.getApp(0);
        app.start();
        port = app.getConnector().getLocalPort();
        baseUrl = "http://" + hostname + ":" + port;
    }

    @Test
    void testUsersAll() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Wendell Legros");
        assertThat(content).contains("Horace Christiansen");
        assertThat(content).contains("Kasey Deckow");
    }

    @Test
    void testUser1() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/4");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Vernie");
        assertThat(content).contains("Larkin");
        assertThat(content).contains("aubrey.boyer@hotmail.com");
    }

    @Test
    void testUser2() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/51");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Chun");
        assertThat(content).contains("Berge");
        assertThat(content).contains("sachiko.roberts@gmail.com");
    }

    @Test
    void testUser3() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/82");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Kip");
        assertThat(content).contains("Bosco");
        assertThat(content).contains("julia.cole@hotmail.com");
    }

    @Test
    void testUserNotFound1() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/101");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(404);
        assertThat(content).contains("Not Found");
    }

    @Test
    void testUserNotFound2() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(404);
        assertThat(content).contains("Not Found");
    }

    @AfterAll
    public static void tearDown() throws LifecycleException {
        app.stop();
    }
}
