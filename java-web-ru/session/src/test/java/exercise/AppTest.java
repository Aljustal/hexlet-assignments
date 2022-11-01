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
    void testUser() throws IOException, ParseException {
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
    void testUserNotFound() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/show?id=105");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(404);
        assertThat(content).contains("Not Found");
    }

    @Test
    void testNewUser() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/new");
        CloseableHttpResponse response = client.execute(request);

        assertThat(response.getCode()).isEqualTo(200);
    }

    @Test
    void testEditUser() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/edit?id=4");
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
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users/new");

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
    void testUpdateUser() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users/edit?id=78");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", "Salvatore"));
        params.add(new BasicNameValuePair("lastName", "Kilback"));
        params.add(new BasicNameValuePair("email", "salvatore@inbox.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

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
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users/new");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", ""));
        params.add(new BasicNameValuePair("lastName", "Sidorov"));
        params.add(new BasicNameValuePair("email", "ivan@mail.ru"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(422);
    }

    @Test
    void testUpdateUserWithIncorrectData() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/users/edit?id=20");

        List<NameValuePair> params = new ArrayList<>();

        params.add(new BasicNameValuePair("firstName", "Deedra"));
        params.add(new BasicNameValuePair("lastName", ""));
        params.add(new BasicNameValuePair("email", "marlin.pfeffer@gmail.com"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(422);
    }

    @Test
    void testDelete() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/users/delete?id=4");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Vernie");
    }

    @Test
    void testDestroyUser() throws IOException, ParseException {
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

        // Проверяем, что пользователь аутентифицирован по наличию кнопки Выход и отсутствию кнопки Вход
        assertThat(content).contains("Выход");
        assertThat(content).doesNotContain("Вход");
    }

    @Test
    void testUserLogout() throws IOException, ParseException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(baseUrl + "/logout");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("email", "lavern.keeling@gmail.com"));
        params.add(new BasicNameValuePair("password", "password"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpResponse response = client.execute(postRequest);

        assertThat(response.getCode()).isEqualTo(200);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        // Проверяем, что пользователь вышел по наличию кнопки Вход и отсутствию кнопки Выход
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

        // Проверяем, что после входа выводится flash-сообщение "Вы успешно вошли"
        assertThat(response1.getCode()).isEqualTo(200);
        assertThat(content1).contains("Вы успешно вошли");

        HttpGet getRequest = new HttpGet(baseUrl);
        CloseableHttpResponse response2 = client.execute(getRequest);

        HttpEntity entity2 = response2.getEntity();
        String content2 = EntityUtils.toString(entity2);

        // Проверяем, что flash-сообщение выводится только один раз
        assertThat(content2).doesNotContain("Вы успешно вошли");

        HttpPost postRequest2 = new HttpPost(baseUrl + "/logout");
        CloseableHttpResponse response3 = client.execute(postRequest2);
        HttpEntity entity3 = response3.getEntity();
        String content3 = EntityUtils.toString(entity3);

        // Проверяем, что после выхода выводится flash-сообщение "Вы успешно вышли"
        assertThat(response3.getCode()).isEqualTo(200);
        assertThat(content3).contains("Вы успешно вышли");
    }

    @AfterAll
    public static void tearDown() throws LifecycleException {
        app.stop();
    }
}
