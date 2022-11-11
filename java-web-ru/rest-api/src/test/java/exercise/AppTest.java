package exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;


import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;
import io.ebean.Transaction;

import exercise.domain.User;
import exercise.domain.query.QUser;

import java.util.List;

class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static Transaction transaction;

    private static Path getFixturePath(String fileName) {
        return Paths.get("src", "test", "resources", "fixtures", fileName)
        .toAbsolutePath().normalize();
    }

    private static String readFixture(String fileName) throws Exception {
        Path filePath = getFixturePath(fileName);
        return Files.readString(filePath).trim();
    }

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port + "/api/v1";
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    // При использовании БД запускать каждый тест в транзакции -
    // является хорошей практикой
    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testGetAll() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");

        List<User> actualUsers = DB.json().toList(User.class, content);
        List<User> expectedUsers = new QUser()
            .orderBy()
                .id.asc()
            .findList();

        assertThat(actualUsers).isEqualTo(expectedUsers);
    }

    @Test
    void testGetOne() {

        String id = "5";

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users/" + id)
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getHeaders().getFirst("Content-Type")).isEqualTo("application/json");

        User actualUser = DB.json().toBean(User.class, content);
        User expectedUser = new QUser()
            .id.equalTo(Long.parseLong(id))
            .findOne();

        assertThat(actualUser).isEqualTo(expectedUser);
    }

    @Test
    void testCreate() {

        // String body = readFixture("newUser.json");

        // HttpResponse<String> responsePost = Unirest
        //     .post(baseUrl + "/users")
        //     .header("Content-Type", "application/json")
        //     .body(body)
        //     .asEmpty();

        User user = new User("Aleksandr", "Beloff", "albel@hotmail.com", "12344321");

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .header("Content-Type", "application/json")
            .body(user)
            .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        User actualUser = new QUser()
            .lastName.equalTo("Beloff")
            .findOne();

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getFirstName()).isEqualTo("Aleksandr");
        assertThat(actualUser.getLastName()).isEqualTo("Beloff");
        assertThat(actualUser.getEmail()).isEqualTo("albel@hotmail.com");
        assertThat(actualUser.getPassword()).isEqualTo("12344321");
    }

    @Test
    void testUpdate() {

        User updatedUser = new User("Nikolay", "Chernoff", "chernota@hotmail.com", "000000");

        HttpResponse<String> responsePatch = Unirest
            .patch(baseUrl + "/users/1")
            .header("Content-Type", "application/json")
            .body(updatedUser)
            .asEmpty();

        assertThat(responsePatch.getStatus()).isEqualTo(200);

        User actualUser = new QUser()
            .id.equalTo(1)
            .findOne();

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getFirstName()).isEqualTo("Nikolay");
        assertThat(actualUser.getLastName()).isEqualTo("Chernoff");
        assertThat(actualUser.getEmail()).isEqualTo("chernota@hotmail.com");
        assertThat(actualUser.getPassword()).isEqualTo("000000");
    }

    @Test
    void testDelete() {

        HttpResponse<String> responseDelete = Unirest
            .delete(baseUrl + "/users/10")
            .asEmpty();

        assertThat(responseDelete.getStatus()).isEqualTo(200);

        User actualUser = new QUser()
            .id.equalTo(10)
            .findOne();

        assertThat(actualUser).isNull();
    }
}
