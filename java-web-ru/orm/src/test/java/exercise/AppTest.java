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
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.ParseException;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import exercise.domain.Article;
import exercise.domain.query.QArticle;

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
    void testArticles() throws IOException, ParseException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/articles");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("The Man Within");
        assertThat(content).doesNotContain("East of Eden");

        HttpGet request2 = new HttpGet(baseUrl + "/articles?page=2");
        CloseableHttpResponse response2 = client.execute(request2);

        HttpEntity entity2 = response2.getEntity();
        String content2 = EntityUtils.toString(entity2);

        assertThat(response2.getCode()).isEqualTo(200);

        assertThat(content2).contains("?page=1");
        assertThat(content2).contains("?page=3");

        assertThat(content2).contains("East of Eden");
        assertThat(content2).doesNotContain("Down to a Sunless Sea");
    }

    @Test
    void testArticle() throws IOException, ParseException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/articles/17");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("To a God Unknown");
        assertThat(content).contains("Fear cuts deeper than swords.");
        // Проверяем, что статья содержит название категории
        assertThat(content).contains("Cinema");
    }

    @Test
    void testNewArticle() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/articles/new");
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);

        assertThat(response.getCode()).isEqualTo(200);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        // Проверяем, что на странице создания выводится список категорий
        assertThat(content).contains("Common");
        assertThat(content).contains("Cinema");
        assertThat(content).contains("Sport");
        assertThat(content).contains("Pets");
        assertThat(content).contains("Music");
    }

    @Test
    void testCreateArticle() throws IOException, ParseException {
        HttpPost postRequest = new HttpPost(baseUrl + "/articles");

        List<NameValuePair> params = new ArrayList<>();

        String actualTitle = "My new test article-lala";
        String actualBody = "Text of article";

        params.add(new BasicNameValuePair("title", actualTitle));
        params.add(new BasicNameValuePair("body", actualBody));
        params.add(new BasicNameValuePair("categoryId", "3"));
        postRequest.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(postRequest);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        assertThat(content).contains("Статья успешно создана");

        assertThat(response.getCode()).isEqualTo(200);

        // Проверяем, что добавленаая статья есть в базе данных
        Article actualArticle = new QArticle()
            .title.equalTo(actualTitle)
            .findOne();

        assertThat(actualArticle).isNotNull();
        assertThat(actualArticle.getTitle()).isEqualTo(actualTitle);
        assertThat(actualArticle.getBody()).isEqualTo(actualBody);
        assertThat(actualArticle.getCategory().getName()).isEqualTo("Sport");
    }

    @AfterAll
    public static void tearDown() throws LifecycleException, IOException {
        app.stop();
    }
}
