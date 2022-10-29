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
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

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
        HttpGet request = new HttpGet(baseUrl + "/users");
        CloseableHttpClient client = HttpClients.createDefault();
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
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=4");
        CloseableHttpClient client = HttpClients.createDefault();
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
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=15");
        CloseableHttpClient client = HttpClients.createDefault();
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
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=101");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(404);
        assertThat(content).contains("Not Found");
    }

    @Test
    void testUserNotFound2() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/users/");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(404);
        assertThat(content).contains("Not Found");
    }

    @Test
    void testNewUser() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/users/new");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);

        assertThat(response.getCode()).isEqualTo(200);
    }

    @Test
    void testEditUser() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/users/edit?id=4");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Vernie");
        assertThat(content).contains("Larkin");
        assertThat(content).contains("aubrey.boyer@hotmail.com");
    }

    @Test
    void testCreateUser() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(baseUrl + "/users/new");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", "Ivan"));
        params.add(new BasicNameValuePair("lastName", "Petrov"));
        params.add(new BasicNameValuePair("email", "ivan@mail.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response1 = client.execute(postRequest);

        assertThat(response1.getCode()).isEqualTo(200);

        HttpGet getRequest = new HttpGet(baseUrl + "/users");
        CloseableHttpResponse response2 = client.execute(getRequest);
        HttpEntity entity = response2.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response2.getCode()).isEqualTo(200);
        assertThat(content).contains("Ivan Petrov");
    }

    @Test
    void testUpdateUser() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(baseUrl + "/users/edit?id=78");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", "Salvatore"));
        params.add(new BasicNameValuePair("lastName", "Kilback"));
        params.add(new BasicNameValuePair("email", "salvatore@inbox.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response1 = client.execute(postRequest);

        HttpGet getRequest = new HttpGet(baseUrl + "/users/show?id=78");
        CloseableHttpResponse response2 = client.execute(getRequest);
        HttpEntity entity = response2.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response2.getCode()).isEqualTo(200);
        assertThat(content).contains("Salvatore Kilback");
        assertThat(content).contains("salvatore@inbox.ru");
    }

    @Test
    void testCreateUserWithIncorrectData() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(baseUrl + "/users/new");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", ""));
        params.add(new BasicNameValuePair("lastName", "Sidorov"));
        params.add(new BasicNameValuePair("email", "ivan@mail.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(422);
    }

    @Test
    void testUpdateUserWithIncorrectData() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(baseUrl + "/users/edit?id=20");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", "Deedra"));
        params.add(new BasicNameValuePair("lastName", ""));
        params.add(new BasicNameValuePair("email", "marlin.pfeffer@gmail.com"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(422);
    }

    @Test
    void testDelete() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/users/delete?id=4");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Vernie");
    }

    @Test
    void testDestroyUser() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(baseUrl + "/users/delete?id=51");
        CloseableHttpClient client = HttpClients.createDefault();
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
