// import exercise.domain.query.QCompany;

// // Пейджинг

// int companiesPerPage = 10; // Количество компаний на странице
// int offset = 20; // Смещение

// PagedList<Company> pagedCompanies = new QCompany()
//     // Устанавливаем смещение
//     .setFirstRow(offset)
//     // Устанавливаем максимальное количество записей в результате
//     .setMaxRows(companiesPerPage)
//     // Задаём сортировку по имени компании
//     .orderBy()
//         .name.asc()
//     // Получаем список PagedList, который представляет одну страницу результата
//     .findPagedList();
// // Получаем список компаний
// List<Company> companies = pagedCompanies.getList();

// // Получение данных одной компании по её имени

// Company company = new QCompany()
//     .name.equalTo("Google")
//     .findOne();


// // Получение списка всех компаний из базы

// List<Company> companies = new QCompany().findList();

// // Добавление новой компании в базу данных

// Company newCompany = new Company("Apple");
// // Благодаря наследованию класса модели от класса io.ebean.Model,
// // на модели можно вызывать метод save() для добавления новой записи в базу
// company.save();
