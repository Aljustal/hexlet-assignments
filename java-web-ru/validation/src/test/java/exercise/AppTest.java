package exercise;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import io.javalin.Javalin;
import io.ebean.DB;
import io.ebean.Transaction;

import exercise.domain.User;
import exercise.domain.query.QUser;

class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static Transaction transaction;

    @BeforeAll
    public static void beforeAll() {
        app = App.getApp();
        app.start(0);
        int port = app.port();
        baseUrl = "http://localhost:" + port;
    }

    @AfterAll
    public static void afterAll() {
        app.stop();
    }

    @BeforeEach
    void beforeEach() {
        transaction = DB.beginTransaction();
    }

    @AfterEach
    void afterEach() {
        transaction.rollback();
    }

    @Test
    void testRoot() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testUsers() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("Wendell Legros");
        assertThat(content).contains("Larry Powlowski");
    }

    @Test
    void testUser() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users/5")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("Rolando Larson");
        assertThat(content).contains("galen.hickle@yahoo.com");
    }

    @Test
    void testNewUser() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/users/new")
            .asString();

        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testCreateUser() {

        String firstName = "Aleksandr";
        String lastName = "Vasiliev";
        String email = "aleks@yandex.ru";
        String password = "123456";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .field("firstName", firstName)
            .field("lastName", lastName)
            .field("email", email)
            .field("password", password)
            .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/users");

        User actualUser = new QUser()
            .lastName.equalTo(lastName)
            .findOne();

        assertThat(actualUser).isNotNull();
        assertThat(actualUser.getFirstName()).isEqualTo(firstName);
        assertThat(actualUser.getLastName()).isEqualTo(lastName);
        assertThat(actualUser.getEmail()).isEqualTo(email);
    }

    @Test
    void testCreateUserWithIncorrectName1() {

        String firstName = "Aleksandr";
        String lastName = "";
        String email = "al@yandex.ru";
        String password = "12345";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .field("firstName", firstName)
            .field("lastName", lastName)
            .field("email", email)
            .field("password", password)
            .asString();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        String content = responsePost.getBody();

        assertThat(content).contains("Aleksandr");
        assertThat(content).contains("al@yandex.ru");
    }

    @Test
    void testCreateUserWithIncorrectName2() {

        String firstName = "";
        String lastName = "Petrov";
        String email = "petrov@mail.ru";
        String password = "12345";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .field("firstName", firstName)
            .field("lastName", lastName)
            .field("email", email)
            .field("password", password)
            .asString();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        String content = responsePost.getBody();

        assertThat(content).contains("Petrov");
        assertThat(content).contains("petrov@mail.ru");
    }

    @Test
    void testCreateUserWithIncorrectEmail() {

        String firstName = "Ivan";
        String lastName = "Petrov";
        String email = "ivanpetrov.ru";
        String password = "12345";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .field("firstName", firstName)
            .field("lastName", lastName)
            .field("email", email)
            .field("password", password)
            .asString();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        String content = responsePost.getBody();

        assertThat(content).contains("Ivan");
        assertThat(content).contains("Petrov");
        assertThat(content).contains("ivanpetrov.ru");
    }

    @Test
    void testCreateUserWithIncorrectPassword1() {

        String firstName = "Valery";
        String lastName = "Zdanov";
        String email = "val@gmail.com";
        String password = "12";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .field("firstName", firstName)
            .field("lastName", lastName)
            .field("email", email)
            .field("password", password)
            .asString();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        String content = responsePost.getBody();

        assertThat(content).contains("Valery");
        assertThat(content).contains("Zdanov");
    }

    @Test
    void testCreateUserWithIncorrectPassword2() {

        String firstName = "Valery";
        String lastName = "Zdanov";
        String email = "val@gmail.com";
        String password = "asd";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/users")
            .field("firstName", firstName)
            .field("lastName", lastName)
            .field("email", email)
            .field("password", password)
            .asString();

        assertThat(responsePost.getStatus()).isEqualTo(422);

        String content = responsePost.getBody();

        assertThat(content).contains("Valery");
        assertThat(content).contains("Zdanov");
    }
}
