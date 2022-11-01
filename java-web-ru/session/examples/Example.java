 public class Example {
     public static void main(String[] args) throws LifecycleException {

         Tomcat tomcat = new Tomcat();

         tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
         tomcat.setPort(port);

         Context ctx = tomcat.addWebapp("", new File("src/main/webapp").getAbsolutePath());

         tomcat.addServlet(ctx, WelcomeServlet.class.getSimpleName(), new WelcomeServlet());
         ctx.addServletMappingDecoded("", WelcomeServlet.class.getSimpleName());

         tomcat.addServlet(ctx, SessionServlet.class.getSimpleName(), new SessionServlet());

         // Один сервлет можно назначить, как обработчик запросов для нескольких путей
         ctx.addServletMappingDecoded("/path1", SessionServlet.class.getSimpleName());
         ctx.addServletMappingDecoded("/path2", SessionServlet.class.getSimpleName());
         ctx.addServletMappingDecoded("/path3", SessionServlet.class.getSimpleName());

         tomcat.start();
         tomcat.getServer().await();
     }
 }


 // Сервлет

 public class SessionServlet extends HttpServlet {

     public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
                 throws IOException, ServletException {
         // Маршрутизация по адресу
         switch (request.getRequestURI()) {
             case "/login":
                 login(request, response);
                 break;
             case "/logout":
                 logout(request, response);
                 break;
             default:
                 response.sendError(HttpServletResponse.SC_NOT_FOUND);
         }
     }

     private void login(HttpServletRequest request,
                                HttpServletResponse response)
                  throws IOException, ServletException {

         // Получение сессии
         HttpSession session = request.getSession();

         // Получаем пользователя по логину
         Map<String, String> user = users.find(name);

         // Далее нужно проверить, зарегестрирован ли такой пользователь

         // Установка атрибутов сессии
         // Вход в систему сводится к записи данных пользователя в сессию
         session.setAttribute("user", user);
         // Механизм работы флеш-сообщений тоже основан на сессии
         // Устанавливаем в сессию атрибут с текстом сообщения
         // Далее мы сможем получить эти данные в шаблонах
         session.setAttribute("flash", "Вы успешно вошли");

         // Выполняем редирект на главную страницу
         response.sendRedirect("/");

     }

     private void logout(HttpServletRequest request,
                                HttpServletResponse response)
                  throws IOException, ServletException {

         // Для выхода пользователя из системы нужно удалить его данные из сессии
         HttpSession session = request.getSession();
         // Удаляем атрибут из сессии
         session.removeAttribute("userId");
         response.sendRedirect("/");
     }
 }

 // Файл application.tag

 // В этом файле можно разместить общую для всех шаблонов часть.

 <%@tag description="Layout" pageEncoding="UTF-8"%>
 <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

 <!DOCTYPE html>
 <html>
     <head>
         <title>Welcome</title>
         <meta charset="UTF-8">
         <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css"
             rel="stylesheet"
             integrity="sha384-KyZXEAg3QhqLMpG8r+8fhAXLRk2vvoC2f3B09zVXn8CA5QIVfZOJ3BCsw2P0p/We"
             crossorigin="anonymous">
     </head>
     <body>
         <nav class="navbar navbar-expand-lg navbar-light bg-light">
             <div class="container">
                 <a class="navbar-brand" href="/">Главная</a>
                 <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                     <li class="nav-item">
                         <a class="nav-link" href="/users">Пользователи</a>
                     </li>
                 </ul>
                 <ul class="navbar-nav">
                     // Если пользователь вошел в систему (в сессии есть его его данные),
                     // для него отображается кнопка "Выход"
                     <c:choose>
                         // Так можно обратиться к атрибутам сессии
                         <c:when test='${sessionScope.userId != null}'>
                             <form action='/logout' method="post">
                                 <button type="submit" class="btn btn-link nav-link">Выход</button>
                             </form>
                         </c:when>
                         // Если пользователь не аутентифицирован,
                         // для него отображаются ссылки "Регистрация" и "Вход"
                         <c:otherwise>
                             <li class="nav-item">
                                 <a class="nav-link" href="/users/new">Регистрация</a>
                             </li>
                             <li class="nav-item">
                                 <a class="nav-link" href="/login">Вход</a>
                             </li>
                         </c:otherwise>
                     </c:choose>
                 </ul>
             </div>
         </nav>
         <div class="container mt-3">
             // В jsp-файлы можно вставлять java-код
             // Удаление атрибута из сессии
             <% session.removeAttribute("attribute"); %>

             // Сюда вставляется переменное содержимое jsp-файла
             <jsp:doBody />
         </div>
     </body>
 </html>


 // Файл jsp

 <%@page contentType="text/html" pageEncoding="UTF-8"%>

 // Подключаем директорию с файлами tag
 <%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>

 // Указываем имя tag-файла: application
 // Часть, заключенная между тегами, будет вставлена в tag файл на место тега <jsp:doBody />
 <tag:application>
     <h1>Вход</h1>
     <form action="/login" method="post">
         <div class="mb-3">
             <label>Email</label>
             <input class="form-control" type="email" name="email" value='${user.getOrDefault("email", "")}'>
         </div>
         <div class="mb-3">
             <label>Пароль</label>
             <input class="form-control" type="password" name="password">
         </div>
         <button class="btn btn-primary" type="submit">Войти</button>
     </form>
 </tag:application>
