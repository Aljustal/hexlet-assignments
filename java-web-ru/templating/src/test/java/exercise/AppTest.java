package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
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
    public static void setup() throws IOException, ParseException, LifecycleException {
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
        assertThat(content).contains("Legros");
        assertThat(content).contains("Christiansen");
        assertThat(content).contains("Deckow");
    }

    @Test
    void testUser1() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=4");
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
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=15");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Rolando");
        assertThat(content).contains("Larson");
        assertThat(content).contains("galen.hickle@yahoo.com");
    }

    @Test
    void testUserNotFound1() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=101");
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

    @Test
    void testDeletePage() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/delete?id=4");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Vernie");
    }

    @Test
    void testDeleteUser() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users/delete?id=51");
        CloseableHttpResponse response1 = client.execute(postRequest);

        assertThat(response1.getCode()).isEqualTo(200);

        HttpGet getRequest = new HttpGet(baseUrl + "/users");
        CloseableHttpResponse response2 = client.execute(getRequest);
        HttpEntity entity = response2.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response2.getCode()).isEqualTo(200);
        assertThat(content).doesNotContain("Chun");
    }

    @AfterAll
    public static void tearDown() throws LifecycleException {
        app.stop();
    }
}
