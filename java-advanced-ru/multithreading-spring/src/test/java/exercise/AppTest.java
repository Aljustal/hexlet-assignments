package exercise;

import exercise.model.User;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    public void testGetUsers() {
        webTestClient
            // Указываем метод запроса (GET) и адрес (*/users*)
            .get().uri("/users")
            // Указываем, какой тип контента мы хотим получить
            .accept(MediaType.APPLICATION_JSON)
            // Выполняем запрос
            .exchange()
            // Проверяем полученный ответ
            .expectStatus().isOk()
            .expectBodyList(User.class)
            .hasSize(4);
    }

    @Test
    @Order(2)
    public void testGetUser() {
        webTestClient
            .get().uri("/users/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class).value(user -> {
                assertThat(user.getFirstName()).isEqualTo("John");
                assertThat(user.getLastName()).isEqualTo("Smith");
            });
    }

    @Test
    @Order(3)
    public void testUpdateUser() {
        User newUserData = new User("Test", "User", "test@test");
        webTestClient
            .patch().uri("/users/4")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(newUserData), User.class)
            .exchange()
            .expectStatus().isOk();

        webTestClient
            .get().uri("/users/4")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(User.class).value(user -> {
                assertThat(user.getFirstName()).isEqualTo("Test");
                assertThat(user.getLastName()).isEqualTo("User");
            });
    }

    @Test
    @Order(4)
    public void testAddUser() {
        User newUserData = new User("New", "TestUser", "t@t");
        webTestClient
            .post().uri("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(newUserData), User.class)
            .exchange()
            .expectStatus().isOk();

        webTestClient
            .get().uri("/users")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(body -> {
                assertThat(body).contains("TestUser", "New", "t@t");
            });
    }

    @Test
    @Order(5)
    public void testDeleteUser() {
        webTestClient
            .delete().uri("/users/3")
            .exchange()
            .expectStatus().isOk();

        webTestClient
            .get().uri("/users")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(String.class)
            .value(body -> {
                assertThat(body).doesNotContain("Jassica", "Simpson");
            });
    }
}
