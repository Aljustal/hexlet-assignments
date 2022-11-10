// // Обработчик с валидацией

// public static Handler createCompany = ctx -> {
//     String name = ctx.formParam("name");
//     String workers = ctx.formParam("workers");

//     // Добавляем валидатор для каждого поля
//     Validator<String> nameValidator = ctx.formParamAsClass("firstName", String.class)
//         // Добавляем проверку, что имя не долно быть пустым
//         .check(it -> !it.isEmpty(), "Имя не должно быть пустым");

//     // Можно добавить несколько проверок
//     Validator<String> workersValidator = ctx.formParamAsClass("lastName", String.class)
//         .check(it ->  StringUtils.isNumeric(it), "Количество работников должно содержать только цифры")
//         .check(it -> !it.isEmpty(), "Количество работников не должно быть пустым");

//     // Валидируем данные из форм и собираем все ошибки валидации в словарь
//     // Если ошибок нет, словарь будет пустой
//     Map<String, List<ValidationError<? extends Object>>> errors = JavalinValidation.collectErrors(
//         nameValidator,
//         workersValidator,
//     );

//     // Если данные не валидные
//     if (!errors.isEmpty()) {
//         // Устанавливаем код ответа
//         ctx.status(422);
//         // Передаем ошибки и данные компании
//         ctx.attribute("errors", errors);
//         Company invalidCompany = new Company(name, workers);
//         ctx.attribute("company", invalidCompany);
//         ctx.render("companies/new.html");
//         return;
//     }

//     Company company = new User(name, workers);
//     company.save();

//     ctx.redirect("/companies");
// };
