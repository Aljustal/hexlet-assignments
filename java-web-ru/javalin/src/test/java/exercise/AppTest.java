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

import exercise.domain.Article;
import exercise.domain.query.QArticle;

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
    void testRoot() {
        HttpResponse<String> response = Unirest.get(baseUrl).asString();
        assertThat(response.getStatus()).isEqualTo(200);
    }

    @Test
    void testArticles() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/articles")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("The Man Within");
        assertThat(content).doesNotContain("East of Eden");

        HttpResponse<String> response2 = Unirest
            .get(baseUrl + "/articles?page=2")
            .asString();
        String content2 = response2.getBody();

        assertThat(response2.getStatus()).isEqualTo(200);
        assertThat(content2).contains("?page=1");
        assertThat(content2).contains("?page=3");
        assertThat(content2).contains("East of Eden");
        assertThat(content2).doesNotContain("Down to a Sunless Sea");
    }

    @Test
    void testArticle() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/articles/17")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("To a God Unknown");
        assertThat(content).contains("Fear cuts deeper than swords.");
        assertThat(content).contains("Cinema");
    }

    @Test
    void testNewArticle() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/articles/new")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("Common");
        assertThat(content).contains("Cinema");
        assertThat(content).contains("Sport");
        assertThat(content).contains("Pets");
        assertThat(content).contains("Music");
    }

    @Test
    void testCreateArticle() {

        String actualTitle = "My new test article-lala";
        String actualBody = "Text of article";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/articles")
            .field("title", actualTitle)
            .field("body", actualBody)
            .field("categoryId", "3")
            .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/articles");

        Article actualArticle = new QArticle()
            .title.equalTo(actualTitle)
            .findOne();

        assertThat(actualArticle).isNotNull();
        assertThat(actualArticle.getTitle()).isEqualTo(actualTitle);
        assertThat(actualArticle.getBody()).isEqualTo(actualBody);
        assertThat(actualArticle.getCategory().getName()).isEqualTo("Sport");
    }

    @Test
    void testEditArticle() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/articles/7/edit")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("A Passage to India");
        assertThat(content).contains("Power is a curious thing");
        assertThat(content).contains("Cinema");
    }

    @Test
    void testUpdateArticle() {

        String updatedBody = "The Man";
        String updatedTitle = "Updated article body";

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/articles/1/edit")
            .field("title", updatedTitle)
            .field("body", updatedBody)
            .field("categoryId", "1")
            .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/articles");

        Article actualArticle = new QArticle()
            .title.equalTo(updatedTitle)
            .findOne();

        assertThat(actualArticle).isNotNull();
        assertThat(actualArticle.getTitle()).isEqualTo(updatedTitle);
        assertThat(actualArticle.getBody()).isEqualTo(updatedBody);
        assertThat(actualArticle.getCategory().getName()).isEqualTo("Common");
    }

    @Test
    void testDeleteArticle() {

        HttpResponse<String> response = Unirest
            .get(baseUrl + "/articles/3/delete")
            .asString();
        String content = response.getBody();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(content).contains("The Lathe of Heaven");
    }

    @Test
    void testDestroyArticle() {

        HttpResponse<String> responsePost = Unirest
            .post(baseUrl + "/articles/5/delete")
            .asEmpty();

        assertThat(responsePost.getStatus()).isEqualTo(302);
        assertThat(responsePost.getHeaders().getFirst("Location")).isEqualTo("/articles");

        Article actualArticle = new QArticle()
            .title.equalTo("Eyeless in Gaza")
            .findOne();

        assertThat(actualArticle).isNull();
    }
}
