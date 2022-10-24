package exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.LifecycleException;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.Socket;
import java.io.IOException;

import rawhttp.core.RawHttp;
import rawhttp.core.RawHttpRequest;
import rawhttp.core.RawHttpResponse;
import rawhttp.core.RequestLine;
import rawhttp.core.HttpVersion;

class AppTest {
    private static int port;
    private static String host = "localhost";
    private static Tomcat app;
    private static String solution;

    @BeforeAll
    public static void setup() throws LifecycleException, IOException {
        app = App.getApp(0);
        app.start();
        port = app.getConnector().getLocalPort();

        Path pathToSolution = Paths.get("solution").toAbsolutePath();
        solution = Files.readString(pathToSolution).trim();
    }

    @Test
    void test() throws LifecycleException, IOException {
        RawHttp http = new RawHttp();
        RawHttpRequest request = http.parseRequest(solution);
        RequestLine rl = request.getStartLine();

        assertThat(rl.getHttpVersion()).isEqualTo(HttpVersion.HTTP_1_1);
        assertThat(rl.getMethod()).isEqualTo("POST");

        String contentLength = request.getHeaders().getFirst("Content-Length").orElse(null);

        assertThat(Integer.parseInt(contentLength)).isEqualTo(15);

        Socket socket = new Socket(host, port);
        request.writeTo(socket.getOutputStream());
        RawHttpResponse<?> response = http.parseResponse(socket.getInputStream());
        String responseBody = response.getBody().get().decodeBodyToString(UTF_8).trim();

        assertThat(response.getStatusCode()).isEqualTo(200);
        assertThat(responseBody).isEqualTo("data uploaded");
    }

    @AfterAll
    public static void tearDown() throws LifecycleException {
        app.stop();
    }
}
