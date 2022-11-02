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

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class AppTest {
    private static int port;
    private static Connection connection;
    private static String hostname = "localhost";
    private static Tomcat app;
    private static String baseUrl;

    private static String getFileContent(String fileName) throws IOException {
        Path logPath = Paths.get(fileName).toAbsolutePath().normalize();
        return Files.readString(logPath);
    }

    @BeforeAll
    public static void setup() throws LifecycleException, SQLException, IOException {
        connection = DriverManager.getConnection("jdbc:h2:mem:");
        app = App.getApp(0, connection);
        app.start();
        port = app.getConnector().getLocalPort();
        baseUrl = "http://" + hostname + ":" + port;

        Statement statement = connection.createStatement();
        String initSql = getFileContent("init.sql");
        statement.execute(initSql);
    }

    @Test
    void testArticles() throws IOException, ParseException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/articles");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("Tiger! Tiger!");
        assertThat(content).contains("In Dubious Battle");
        assertThat(content).doesNotContain("A Monstrous Regiment of Women");

        HttpGet request2 = new HttpGet(baseUrl + "/articles?page=2");
        CloseableHttpResponse response2 = client.execute(request2);

        HttpEntity entity2 = response2.getEntity();
        String content2 = EntityUtils.toString(entity2);

        assertThat(response2.getCode()).isEqualTo(200);

        assertThat(content2).contains("?page=1");
        assertThat(content2).contains("?page=3");

        assertThat(content2).contains("A Monstrous Regiment of Women");
        assertThat(content2).doesNotContain("Down to a Sunless Sea");
    }

    @Test
    void testArticle() throws IOException, ParseException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/articles/39");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content).contains("A Passage to India");
        assertThat(content).contains("When you play a game of thrones you win or you die.");

    }

    @Test
    void testArticleNotFound() throws IOException, ParseException {

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(baseUrl + "/articles/200");
        CloseableHttpResponse response = client.execute(request);

        assertThat(response.getCode()).isEqualTo(404);
    }

    @AfterAll
    public static void tearDown() throws LifecycleException, IOException, SQLException {
        connection.close();
        app.stop();
    }
}
