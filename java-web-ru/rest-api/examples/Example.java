// // Метод routes() добавляет в приложение пять маршрутов для работы с сущностью

// app.routes(() -> {
//     crud("companies/{id}", new UserController());
// });

// // interface CrudHandler {
// //     getAll(ctx)
// //     getOne(ctx, resourceId)
// //     create(ctx)
// //     update(ctx, resourceId)
// //     delete(ctx, resourceId)
// // }


// // Обработчики
// // Преобразование списка сущностей в JSON представление

// List<Company> companies = new QCompany()
//     .orderBy()
//         .id.asc()
//     .findList();

// String json = DB.json().toJson(companies);

// // Аналогично можно представить в виде JSON отдельную сущность

// Company company = new QCompany()
//     .id.equalTo(id)
//     .findOne();

// String json = DB.json().toJson(companies);

// // Так можно отправить JSON строку в ответе, установив при этом правильный тип содержимого
// ctx.json(json);

// // Получение тела запроса

// String body = ctx.body();

// // Из JSON представления, полученного из тела запроса, можно получить экземпляр модели

// Company company = DB.json().toBean(Company.class, body);
