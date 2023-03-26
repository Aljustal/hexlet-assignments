@SpringBootTest
@AutoConfigureMockMvc

// Аннотация позволяет автоматически запускать и останавливать в тестах все контейнеры
@Testcontainers
// Все тесты выполняем в транзакции
@Transactional
public class AppTest {

    @Autowired
    private MockMvc mockMvc;

    // Аннотация отмечает контейнер, который будет автоматически запущен
    @Container
    // Создаём контейнер с СУБД PostgreSQL
    // В конструктор передаём имя образа, который будет скачан с Dockerhub
    // Если не указать версию, будет скачана последняя версия образа
    private static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
        // Создаём базу данных с указанным именем
        .withDatabaseName("dbname")
        // Указываем имя пользователя и пароль
        .withUsername("sa")
        .withPassword("sa")
        // Скрипт, который будет выполнен при запуске контейнера и наполнит базу тестовыми данными
        .withInitScript("script.sql");

    // Так как мы не можем знать заранее, какой URL будет у базы данных в контейнере
    // Нам потребуется установить это свойство динамически
    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        // Устанавливаем URL базы данных
        registry.add("spring.datasource.url", database::getJdbcUrl);
        // Имя пользователя и пароль для подключения
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
        // Эти значения приложение будет использовать при подключении к базе данных
    }

    // Тестируем приложение
    @Test
    void testCreatePerson() throws Exception {
        // Добавляем нового пользователя
        MockHttpServletResponse responsePost = mockMvc
            .perform(
                post("/people")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"firstName\": \"Jackson\", \"lastName\": \"Bind\"}")
            )
            .andReturn()
            .getResponse();

        assertThat(responsePost.getStatus()).isEqualTo(200);

        // И проверяем, что пользователь добавился в базу
        MockHttpServletResponse response = mockMvc
            .perform(get("/people"))
            .andReturn()
            .getResponse();

        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
        assertThat(response.getContentAsString()).contains("Jackson", "Bind");
    }

    // Остальные тесты
}
