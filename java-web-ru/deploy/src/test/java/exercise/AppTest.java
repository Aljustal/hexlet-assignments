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

import java.util.List;
import static exercise.Data.getCompanies;

class AppTest {
    private static int port;
    private static String hostname = "localhost";
    private static Tomcat app;
    private static String baseUrl;
    private static CloseableHttpClient client;

    @BeforeAll
    public static void setup() throws LifecycleException, IOException {
        app = App.getApp(0);
        app.start();
        port = app.getConnector().getLocalPort();
        baseUrl = "http://" + hostname + ":" + port;
        client = HttpClients.createDefault();
    }

    @Test
    void testWelcome() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl);
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        assertThat(content.trim()).isEqualTo("open something like /companies or /companies?search=ol");
    }

    @Test
    void testCompaniesAll() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/companies");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        List<String> companies = List.of(content.trim().split("\n"));
        assertThat(companies).isEqualTo(getCompanies());
    }

    @Test
    void testCompaniesFilterEmpty() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/companies?search=");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        List<String> companies = List.of(content.trim().split("\n"));
        assertThat(companies).isEqualTo(getCompanies());
    }

    @Test
    void testCompaniesFilter1() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/companies?search=ol");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        List<String> companies = List.of(content.trim().split("\n"));
        List<String> expected = List.of(
            "Lueilwitz, Reynolds and Schumm Inc",
            "Volkman-Morar and Sons",
            "Ward, Dickens and Gerhold and Sons",
            "Wolff, Carter and Beatty and Sons",
            "Legros, Cruickshank and Nikolaus LLC",
            "Gerhold Group and Sons",
            "Wiegand-Pollich Inc",
            "Herman, Wolff and Cassin Group",
            "Bartoletti and Sons LLC",
            "Goldner, Christiansen and Botsford LLC"
        );
        assertThat(companies).isEqualTo(expected);
    }

    @Test
    void testCompaniesFilter2() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/companies?search=ov");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        List<String> companies = List.of(content.trim().split("\n"));
        List<String> expected = List.of(
            "Cartwright-Glover and Sons",
            "Hermann, Macejkovic and Brekke Group"
        );
        assertThat(companies).isEqualTo(expected);
    }

    @Test
    void testCompaniesFilter3() throws IOException, ParseException {
        HttpGet request = new HttpGet(baseUrl + "/companies?search=tl");
        CloseableHttpResponse response = client.execute(request);

        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);

        assertThat(response.getCode()).isEqualTo(200);
        List<String> companies = List.of(content.trim().split("\n"));
        List<String> expected = List.of("Companies not found");
        assertThat(companies).isEqualTo(expected);
    }

    @AfterAll
    public static void tearDown() throws LifecycleException {
        app.stop();
    }
}
