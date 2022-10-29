// import org.apache.catalina.LifecycleException;
// import org.apache.catalina.startup.Tomcat;
// import org.apache.catalina.Context;
// import java.io.File;

// public class App {

//     public static void main(String[] args) throws LifecycleException {

//         Tomcat tomcat = new Tomcat();

//         tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
//         tomcat.setPort(8080);

//         // Добавляем в контекст шаблоны
//         // Указывем путь к директории, в которой будут находиться jsp-файлы
//         Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());


//         // Регистрируем сервлет
//         tomcat.addServlet(ctx, UsersServlet.class.getSimpleName(), new UsersServlet());
//         // Назначаем сервлет как отбработчик запросов по пути "/companies"
//         ctx.addServletMappingDecoded("/companies", UsersServlet.class.getSimpleName());
//         tomcat.start();
//         tomcat.getServer().await();
//     }
// }

// // Сервлет

// import java.io.IOException;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
// import javax.servlet.RequestDispatcher;

// import java.util.List;
// import java.util.Map;

// public class UsersServlet extends HttpServlet {

//     // Список всех компаний
//     List<Map<String, String>> companies;

//     @Override
//     public void doGet(HttpServletRequest request,
//                       HttpServletResponse response)
//                 throws IOException, ServletException {
//         // Получаем информацию о пути из URL-адреса, на который был выполнен запрос
//         String pathInfo = request.getPathInfo();

//         // Если метод вернул `null`, значит запрос был выполнен на адрес `/companies`
//         // Нужно вывести список всех компаний
//         if (pathInfo == null) {
//             // Вызываем обработчик, выводящий список всех компаний
//             showCompanies(request, response);
//             return;
//         }

//         // Получаем экшн из пути. Например, в запросе к URL "/companies/show?id=1"
//         // будет экшн "show", обозначающий, что нужно вывести станицу компании с id = 1
//         String[] pathParts = pathInfo.split("/");
//         String action = ArrayUtils.get(pathParts, 1, "");

//         // Проверям экшн
//         switch (action) {
//             case "show":
//                 // Если получен экшн "show", вызываем обработчик, выводящий страницу компании
//                 showCompany(request, response);
//                 break;
//             default:
//                 // Если получен неизвестный экшн, возвращаем код ответа 404
//                 response.sendError(HttpServletResponse.SC_NOT_FOUND);
//         }
//     }

     // Обработчик, выводящий список всех компаний
     private void showCompanies(HttpServletRequest request,
                           HttpServletResponse response)
                 throws IOException, ServletException {

         // Сохраняем атрибут в запросе. Так мы сможем передать данные в шаблон
         request.setAttribute("companies", companies);
         // Передаем управление в шаблон
         RequestDispatcher requestDispatcher = request.getRequestDispatcher("/company.jsp");
         requestDispatcher.forward(request, response);

     }

//     // Обработчик, выводящий  страницу компании
//     private void showCompany(HttpServletRequest request,
//                          HttpServletResponse response)
//                  throws IOException, ServletException {

//         // Получаем id компании из строки запроса
//         String id = request.getParameter("id");

//         // Получаем компанию по её id
//         Map<String, String> user = getCompanyById(id);

//         // Если компания не найдена, нужно вернуть код ответа 404
//         if (user == null) {
//             response.sendError(HttpServletResponse.SC_NOT_FOUND);
//             return;
//         }
//         // Сохраняем атрибут в запросе. Так мы сможем передать данные компании в шаблон
//         request.setAttribute("user", user);
//         // Передаем управление в шаблон
//         RequestDispatcher requestDispatcher = request.getRequestDispatcher("/show.jsp");
//         requestDispatcher.forward(request, response);

//     }
// }

// // jsp-файл

// // Устанавливаем тип содержимого и кодировку
// <%@page contentType="text/html" pageEncoding="UTF-8"%>
// // Подключаем библиотеку jstl
// <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
//
// <!DOCTYPE html>
// <html>
//     <head>
//         <meta charset="UTF-8">
//         <title>User</title>
//         // Подключаем стили Bootstrap
//         <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
//             rel="stylesheet"
//             integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
//             crossorigin="anonymous">
//     </head>
//     <body>
//         // В цикле выводим всех пользователей
//         // Внутри jsp-файла нам доступна переменная users, которую мы установили в сервлете
//         <c:forEach var="user" items="${users}">
//             <tr>
//             <td>${user.get("id")}</td>
//             <td><a href='/users/show?id=${user.get("id")}'>${user.get("firstName")} ${user.get("lastName")}</a></td>
//             </tr>
//         </c:forEach>
//     </body>
// </html>
