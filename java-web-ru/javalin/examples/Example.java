// private static void addRoutes(Javalin app) {

//     // Добавляем маршруты в приложение. Имя метода соответствует глаголу HTTP
//     // Метод get добавляет обработчик, который будет выполняться для GET запроса по указанному пути
//     app.get("/", RootController.welcome);

//     app.get("companies", CompanyController.listCompanies);

//     // Добавляем обработчик для POST запроса по пути */companies*
//     app.post("companies", CompanyController.createCompany);

//     // При помощи методов routes() и path() маршруты можно группировать по пути

//     app.routes(() -> {
//         path("companies", () -> {
//             // GET /companies
//             get(CompanyController.listCompanies);
//             // POST /companies
//             post(CompanyController.createCompany);
//             // GET /companies/new
//             get("new", CompanyController.newCompany);
//         });
//     });

//     // Создание динамического маршрута, в котором часть пути переменная

//     // GET /companies/1
//     // GET /companies/12
//     // GET /companies/101

//     app.get("companies/{id}", CompanyController.showCompany);

//     // В обработчике можно будет получить переменную часть пути при помощи метода ctx.pathParam("id")
// }

// // Контроллер CompanyController

// // Описываем в классе обработчики как статические свойства

// public static Handler handler = ctx -> {

//     // Получаем переменную часть пути, это id компании
//     // Так можно получить данные сразу в нужном формате
//     long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);

//     // Получение данных из формы
//     String title = ctx.formParam("title");

//     // Так можно получить данные формы в нужном формате
//     int userId = ctx.formParamAsClass("userId", Integer.class).getOrDefault(null);

//     // Получаем компанию из базы по id
//     Company company = new QCompany()
//         .id.equalTo(id)
//         .findOne();


    // // Так данные компании можно обновить
    // // Используем query builder
    // new QCompany()
    //         .id.equalTo(id)
    //         .asUpdate() // делает из запроса update-запрос
    //             .set("name", "new Name")
    //             // Так можно обновить связанную сущность, например владельца компании
    //             // Нужно установить id нового владельца
    //             .set("owner", "5")
    //             .update();


//     // Устанавливаем атрибут запроса
//     // Так данные компании попадут в шаблон
//     ctx.attribute("article", article);

//     // Создание flash-соощения
//     // Для этого нужно установить в сессию атрибут
//     ctx.sessionAttribute("flash", "Сообщение");

//     // Рендерим нужный шаблон, указав путь до него
//     ctx.render("companies/show.html");

//     // Или выполняем редирект на указанный адрес
//     ctx.redirect("/");
// };
