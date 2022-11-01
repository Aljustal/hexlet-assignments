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

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

class AppTest {
    private static int port;
    private static String hostname = "localhost";
    private static Tomcat app;
    private static String baseUrl;
    private static String logFileName = "app.log";

    private static Path getAbsolutePath(String fileName) {
        return Paths.get(logFileName).toAbsolutePath().normalize();
    }

    private static String getLogContent() throws IOException {
        return Files.readString(getAbsolutePath(logFileName));
    }

    @BeforeAll
    public static void setup() throws IOException, ParseException, LifecycleException {
        Files.deleteIfExists(getAbsolutePath(logFileName));
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
        assertThat(content).contains("Wendell Legros");
        assertThat(content).contains("Iona Larkin");
        assertThat(content).contains("devin.rosenbaum@hotmail.com");
    }

    @Test
    void testCreateUser() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", "Ivan"));
        params.add(new BasicNameValuePair("lastName", "Petrov"));
        params.add(new BasicNameValuePair("email", "ivan@mail.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

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
    void testCreateUserWithIncorrectData() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", ""));
        params.add(new BasicNameValuePair("lastName", "Sidorov"));
        params.add(new BasicNameValuePair("email", "ivan@mail.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(422);
    }

    @Test
    void testUserLogin() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/login");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", "lavern.keeling@gmail.com"));
        params.add(new BasicNameValuePair("password", "password"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(200);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(content).contains("Выход");
        assertThat(content).doesNotContain("Вход");
    }

    @Test
    void testUserLogout() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost loginRequest = new HttpPost(baseUrl + "/login");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", "lavern.keeling@gmail.com"));
        params.add(new BasicNameValuePair("password", "password"));
        loginRequest.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse loginResponse = client.execute(loginRequest);

        HttpPost logoutRequest = new HttpPost(baseUrl + "/logout");
        CloseableHttpResponse logoutResponse = client.execute(logoutRequest);

        assertThat(logoutResponse.getCode()).isEqualTo(200);

        HttpEntity entity = logoutResponse.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(content).contains("Вход");
        assertThat(content).doesNotContain("Выход");
    }

    @Test
    void testUserIncorrectLogin() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/login");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", "lavern.keeling@gmail.com"));
        params.add(new BasicNameValuePair("password", "pass"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(422);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(content).contains("lavern.keeling@gmail.com");
    }

    @Test
    void testFlash() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest1 = new HttpPost(baseUrl + "/login");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", "lavern.keeling@gmail.com"));
        params.add(new BasicNameValuePair("password", "password"));
        postRequest1.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response1 = client.execute(postRequest1);
        HttpEntity entity1 = response1.getEntity();
        String content1 = EntityUtils.toString(entity1);

        assertThat(response1.getCode()).isEqualTo(200);
        assertThat(content1).contains("Вы успешно вошли");

        HttpGet getRequest = new HttpGet(baseUrl);
        CloseableHttpResponse response2 = client.execute(getRequest);

        HttpEntity entity2 = response2.getEntity();
        String content2 = EntityUtils.toString(entity2);

        assertThat(content2).doesNotContain("Вы успешно вошли");

        HttpPost postRequest2 = new HttpPost(baseUrl + "/logout");
        CloseableHttpResponse response3 = client.execute(postRequest2);
        HttpEntity entity3 = response3.getEntity();
        String content3 = EntityUtils.toString(entity3);

        assertThat(response3.getCode()).isEqualTo(200);
        assertThat(content3).contains("Вы успешно вышли");
    }

    // Проверяем, что логи приложения записываются в файл app.log
    // при входе пользователей в приложение
    @Test
    void testLogging() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();

        String beforeLoginContent = getLogContent();

        assertThat(beforeLoginContent).doesNotContain("shoshana.haley@hotmail.com");
        assertThat(beforeLoginContent).doesNotContain("suzann.barrows@hotmail.com");

        // Выполняем логин с корректными данными
        HttpPost postRequest1 = new HttpPost(baseUrl + "/login");
        List<NameValuePair> params1 = new ArrayList<>();
        params1.add(new BasicNameValuePair("email", "shoshana.haley@hotmail.com"));
        params1.add(new BasicNameValuePair("password", "password"));
        postRequest1.setEntity(new UrlEncodedFormEntity(params1));
        client.execute(postRequest1);

        // Выполняем логин с неправильным паролем
        HttpPost postRequest2 = new HttpPost(baseUrl + "/login");
        List<NameValuePair> params2 = new ArrayList<>();
        params2.add(new BasicNameValuePair("email", "suzann.barrows@hotmail.com"));
        params2.add(new BasicNameValuePair("password", "incorrect"));
        postRequest2.setEntity(new UrlEncodedFormEntity(params2));
        client.execute(postRequest2);

        String afterLoginContent = getLogContent();

        // Все попытки входа должны быть отражены в логе
        assertThat(afterLoginContent).contains("shoshana.haley@hotmail.com");
        assertThat(afterLoginContent).contains("suzann.barrows@hotmail.com");
    }

    // BEGIN
    @Test
    void testNewUser() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet getRequest = new HttpGet(baseUrl + "/users/new");
        CloseableHttpResponse response = client.execute(getRequest);

        assertThat(response.getCode()).isEqualTo(200);
    }
    // END

    @AfterAll
    public static void tearDown() throws LifecycleException, IOException {
        app.stop();
    }
}
