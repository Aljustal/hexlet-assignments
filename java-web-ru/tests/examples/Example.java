class AppTest {

    private static Javalin app;
    private static String baseUrl;
    private static Transaction transaction;

    // Метод выполнится перед всеми тестами в классе
    @BeforeAll
    public static void beforeAll() {
        // Получаем инстанс приложения
        app = App.getApp();
        // Запускаем приложение на рандомном порту
        app.start(0);
        // Получаем порт, на которм запустилось приложение
        int port = app.port();
        // Формируем базовый URL
        baseUrl = "http://localhost:" + port;
    }

    // Метод выполнится после окончания всех тестов в классе
    @AfterAll
    public static void afterAll() {
        // Останавливаем приложение
        app.stop();
    }

    @Test
    void testSonething() {

        // Выполняем POST запрос при помощи агента Unirest
        HttpResponse<String> responsePost = Unirest
            // POST запрос на URL
            .post(baseUrl + "/companies")
            // Устанавливаем значения полей
            .field("name", "Google")
            .field("anotherField", "another value")
            // Выполняем запрос и получаем тело ответ с телом в виде строки
            .asString();

        // Проверяем статус ответа
        assertThat(responsePost.getStatus()).isEqualTo(302);

        // Проверяем, что компания добавлена в БД
        User actualCompany = new QCompany()
            .name.equalTo("Google")
            .findOne();

        assertThat(actualCompany).isNotNull();

        // И что её данные соответствуют переданным
        assertThat(actualUser.getName()).isEqualTo("Google");
        assertThat(actualUser.getAnotherField()).isEqualTo("another valeu");

        // Можно проверить, что такой компании нет в БД
        assertThat(actualCompany).isNull();

        // Так можно получить содержимое тела ответа
        String content = responsePost.getBody();

        // И проверить, что оно содержит определённую строку
        assertThat(content).contains("yandex.ru");
    }
}
