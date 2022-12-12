package exercise;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;
import com.github.database.rider.junit5.api.DBRider;
import com.github.database.rider.core.api.dataset.DataSet;

@SpringBootTest
// При тестировании можно вообще не запускать сервер
// Spring будет обрабатывать HTTP запрос и направлять его в контроллер
// Код вызывается точно так же, как если бы он обрабатывал настоящий запрос
// Такие тесты обходятся дешевле в плане ресурсов
// Для этого нужно внедрить MockMvc

// BEGIN
@AutoConfigureMockMvc
// END

// Чтобы исключить влияние тестов друг на друга,
// каждый тест будет выполняться в транзакции.
// После завершения теста транзакция автоматически откатывается
@Transactional
// Для наполнения БД данными перед началом тестирования
// воспользуемся возможностями библиотеки Database Rider
@DBRider
// Файл с данными для тестов (фикстуры)
@DataSet("people.yml")
public class AppTest {

    // Автоматическое создание экземпляра класса MockMvc
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRootPage() throws Exception {
        // Выполняем запрос и получаем ответ
        MockHttpServletResponse response = mockMvc
            // Выполняем GET запрос по указанному адресу
            .perform(get("/"))
            // Получаем результат MvcResult
            .andReturn()
            // Получаем ответ MockHttpServletResponse из класса MvcResult
            .getResponse();

        // Проверяем статус ответа
        assertThat(response.getStatus()).isEqualTo(200);
        // Проверяем, что ответ содержит определенный текст
        assertThat(response.getContentAsString()).contains("Welcome to Spring");
    }

    @Test
    void testGetPeople() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/people"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        // Проверяем, что тип содержимого в ответе JSON
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        // Проверяем, что тело ответа содержит данные сущностей
        assertThat(response.getContentAsString()).contains("John", "Smith");
        assertThat(response.getContentAsString()).contains("Jack", "Doe");
    }

    @Test
    void testCreatePerson() throws Exception {
        MockHttpServletResponse responsePost = mockMvc
            .perform(
                // Выполняем POST-запрос
                post("/people")
                    // Устанавливаем тип содержимого тела запроса
                    .contentType(MediaType.APPLICATION_JSON)
                    // Добавляем содержимое тела
                    .content("{\"firstName\": \"Jackson\", \"lastName\": \"Bind\"}")
            )
            .andReturn()
            .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        // Проверяем, что сущность добавилась в базу
        // Выполняем GET-запрос на страницу вывода всех сущностей
        MockHttpServletResponse response = mockMvc
            .perform(get("/people"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        // Проверяем, что созданная сущность появилась в базе
        assertThat(response.getContentAsString()).contains("Jackson", "Bind");
    }

    // BEGIN
    @Test
    void testShowPerson() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(get("/people/1"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("John", "Smith");
    }

    @Test
    void testUpdatePerson() throws Exception {
        MockHttpServletResponse responsePatch = mockMvc
                .perform(
                        patch("/people/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"firstName\": \"Andrew\", \"lastName\": \"Wick\"}")
                )
                .andReturn()
                .getResponse();

        assertThat(responsePatch.getStatus()).isEqualTo(200);

        MockHttpServletResponse response = mockMvc
                .perform(get("/people"))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).contains("Andrew", "Wick");
        assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
    }

    @Test
    void testDeletePerson() throws Exception {
        MockHttpServletResponse responseDelete = mockMvc
                .perform(delete("/people/1"))
                .andReturn()
                .getResponse();

        assertThat(responseDelete.getStatus()).isEqualTo(200);

        MockHttpServletResponse response = mockMvc
                .perform(get("/people"))
                .andReturn()
                .getResponse();

        assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
    }
    // END
}
