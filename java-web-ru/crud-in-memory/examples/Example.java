// import java.io.IOException;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.RequestDispatcher;

// import java.util.List;
// import java.util.Map;
// import java.util.HashMap;

// public class UsersServlet extends HttpServlet {

//     // Список компаний
//     private List<Map<String, String>> companies;

//     // Метод, получающий id компании из строки запроса
//     private String getId(HttpServletRequest request) {
//         return request.getParameter("id");
//     }

//     // Метод получающий экшн из пути в URL
//     private String getAction(HttpServletRequest request) {
//         String pathInfo = request.getPathInfo();
//         if (pathInfo == null) {
//             return "list";
//         }
//         String[] pathParts = pathInfo.split("/");
//         return ArrayUtils.get(pathParts, 1, "");
//     }


//     // Полный CRUD сущности

//     @Override
//     public void doGet(HttpServletRequest request,
//                       HttpServletResponse response)
//                 throws IOException, ServletException {

//         // Получаем из пути экшн
//         String action = getAction(request);
//        // В зависимости от экшена вызываем нужный обработчик
//         switch (action) {
//             // Просмотр списка всех компаний
//             case "list":
//                 showCompanies(request, response);
//                 break;
//             // Вывод страницы создания новой компании
//             case "new":
//                 newCompany(request, response);
//                 break;
//             // Вывод страницы редактирования компании
//             case "edit":
//                 editCompany(request, response);
//                 break;
//             case "show":
//             // Просмотр компании
//                 showCompany(request, response);
//                 break;
//             // Вывод страницы с подтверждением удаления
//             case "delete":
//                 deleteCompany(request, response);
//                 break;
//             // Если путь не содержит известный экшн, возвращаем код ответа 404
//             default:
//                 response.sendError(HttpServletResponse.SC_NOT_FOUND);
//         }
//     }

//     @Override
//     public void doPost(HttpServletRequest request,
//                       HttpServletResponse response)
//                 throws IOException, ServletException {

//         String action = getAction(request);

//         switch (action) {
//             // Создание новой компании
//             case "new":
//                 createCompany(request, response);
//                 break;
//             // Обновление данных компании
//             case "edit":
//                 updateCompanyUser(request, response);
//                 break;
//             // Удаление компании
//             case "delete":
//                 destroyCompany(request, response);
//                 break;
//             default:
//                 response.sendError(HttpServletResponse.SC_NOT_FOUND);
//         }
//     }

//     // Вывод страницы создания новой компании
//     private void newCompany(HttpServletRequest request,
//                          HttpServletResponse response)
//                  throws IOException, ServletException {

//         // Если данные не прошли валидацию, поля формы создания нового пользователя
//         // на странице должна отображаться ошибка и поля формы должны быть заполнены введенными данными.
//         // Но при первом заполнении формы этих данных нет.
//         // Чтобы в шаблоне не возникла ошибка, в методе нужно передать в шаблон "пустую компанию"
//         // и пустое сообщение о ошибке

//         // Создаём пустую компанию
//         Map<String, String> company = new HashMap<>();

//         // Устанавливаем атрибуты запроса
//         // Пустая компания
//         request.setAttribute("user", user);
//         // Пустое сообщение об ошибке
//         request.setAttribute("error", "");
//         // Передаем управление в шаблон
//         RequestDispatcher requestDispatcher = request.getRequestDispatcher("/new.jsp");
//         requestDispatcher.forward(request, response);

//         // Теперь при первом заполнении формы ошибка отображаться не будет,
//         // а поля формы будут пустыми

//     }
//     // Создание новой компании
//     private void createCompany(HttpServletRequest request,
//                          HttpServletResponse response)
//                  throws IOException, ServletException {

//         // BEGIN
//         // Получаем из запроса данные компании
//         String name = request.getParameter("name");
//         // Генерируем id
//         String id = getNextId();
//         // Создаём новую компанию  заполянем её введенными данными
//         Map<String, String> company = /* создание компании */

//         // Проверяем введенные данные
//         if (firstName.isEmpty() || lastName.isEmpty()) {
//             // Если данные не прошли валидацию выполянем редирект с кодом 422 на страницу создания новой компании
//             RequestDispatcher requestDispatcher = request.getRequestDispatcher("/new.jsp");
//             // Передаём туда введенные данные компании
//             request.setAttribute("company", company);
//             // И сообщение об ошибке
//             request.setAttribute("error", "Название компании не может быть пустым");
//             response.setStatus(422);
//             requestDispatcher.forward(request, response);
//             return;
//         }
//         // Если с данными все в порядке, добавляем компанию в список
//         companies.add(company);
//         // И переходим на страницу компаний
//         response.sendRedirect("/users");
//     }
// }


// // jsp-файл (страница создания новой компании)

// <%@page contentType="text/html" pageEncoding="UTF-8"%>

// <!DOCTYPE html>
// <html>
//     <head>
//         <meta charset="UTF-8">
//         <title>Add new user</title>
//         <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
//             rel="stylesheet"
//             integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
//             crossorigin="anonymous">
//     </head>
//     <body>
//         <div class="container">
//             <a href="/users">Все пользователи</a>
//             // Выводим на странице сообщение об ошибке.
//             // При первом заполнении ошибка отображаться не будет,
//             // так как мы передаём в шаблон пустое сообщение
//             <div>${error}</div>
//             // Форма создания новой компании
//             <form action="/users/new" method="post">
//                 // Текстовое поле
//                 <div class="mb-3">
//                     <label>Имя</label>
//                     // При первом заполнении формы поле не будет заполнено.
//                     // Так как мы передаём в шаблон пустую компанию,
//                     // метод getOrDefault вернет значение по умолчанию - пустую строку
//                     // В случае, если данные не прошли валидацию, в шаблон будут переданы данные компании
//                     // и поле будет заполнено введенными ранее данными
//                     // Атрибут name устанавливает имя этого поля,
//                     // По этому имени данные можно будет получить в обработчике при помощи метода getParameter()
//                     <input class="form-control" type="text" name="name" value='${company.getOrDefault("name", "")}'>
//                 </div>
//                // Кнопка
//                 <button class="btn btn-primary" type="submit">Создать</button>
//             </form>
//         </div>
//     </body>
// </html>




