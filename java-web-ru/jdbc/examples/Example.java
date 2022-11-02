// public class App {

//     public static void main(String[] args) throws LifecycleException, SQLException, IOException {
//         // Создеём новое соединение с базой данных H2
//         // Указывам путь до файла, в котором будет находиться база данных
//         // База hexlet будет находиться в файле в текущей директории
//         Connection connection = DriverManager.getConnection("jdbc:h2:./hexlet");
//         // Выполняем запросы из файла init.sql

//         Statement statement = connection.createStatement();
//         // Читаем запросы из файла
//         String initSql = getFileContent("init.sql");
//         // Выполняем запросы
//         statement.execute(initSql);
//         int port = getPort();
//         Tomcat app = getApp(port, connection);
//         app.start();
//         app.getServer().await();
//     }
// }

// public class CompanyServlet extends HttpServlet {

//     // Метод для получения id компании, как часть пути
//     private String getId(HttpServletRequest request) {
//         String pathInfo = request.getPathInfo();
//         if (pathInfo == null) {
//             return null;
//         }
//         String[] pathParts = pathInfo.split("/");
//         return ArrayUtils.get(pathParts, 1, null);
//     }

//     // Метод ля получения экшена
//     private String getAction(HttpServletRequest request) {
//         String pathInfo = request.getPathInfo();
//         if (pathInfo == null) {
//             return "list";
//         }
//         String[] pathParts = pathInfo.split("/");
//         return ArrayUtils.get(pathParts, 2, getId(request));
//     }

//     @Override
//     public void doGet(HttpServletRequest request,
//                       HttpServletResponse response)
//                 throws IOException, ServletException {

//         String action = getAction(request);

//         switch (action) {
//             case "list":
//                 showArticles(request, response);
//                 break;
//             default:
//                 showArticle(request, response);
//                 break;
//         }
//     }

//     private void showCompanies(HttpServletRequest request,
//                           HttpServletResponse response)
//                 throws IOException, ServletException {

//         // Получаем из контекста соединение с базой данных
//         ServletContext context = request.getServletContext();
//         Connection connection = (Connection) context.getAttribute("dbConnection");

//         List<Map<String, String>> companies = new ArrayList<>();

//         // Запрос для получения данных компании. Вместо знака ? буду подставлены определенные значения
//         String query = "SELECT id, name FROM companies LIMIT ?";

//         try {
//             // Используем PreparedStatement
//             // Он позволяет подставить в запрос значения вместо знака ?
//             PreparedStatement statement = connection.prepareStatement(query);
//             // Указываем номер позиции в запросе (номер начинается с 1) и значение,
//             // которое будет подставлено
//             statement.setInt(1, 20);
//             // Выполняем запрос
//             // Результат выполнения представлен объектом ResultSet
//             ResultSet rs = statement.executeQuery();

//             // При помощи метода next() можно итерировать по строкам в результате
//             // Указатель перемещается на следующую строку в результатах
//             while (rs.next()) {
//                 companies.add(Map.of(
//                     // Так можно получить значение нужного поля в текущей строке
//                     "id", rs.getString("id"),
//                     "name", rs.getString("name")
//                     )
//                 );
//             }

//         } catch (SQLException e) {
//             // Если произошла ошибка, устанавливаем код ответа 500
//             response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//             return;
//         }
//         // Устанавливаем значения атрибутов
//         request.setAttribute("companies", companies);
//         // Передаём данные в шаблон
//         TemplateEngineUtil.render("companies/index.html", request, response);
//     }
// }


// // Шаблон

// <!DOCTYPE html>
// <html xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout"
//   layout:decorate="~{layouts/application.html}">
// <head>
//   <title>Companies</title>
// </head>

// <section layout:fragment="content">
//   <table class="table">
//     <thead>
//       <th>ID</th>
//       <th>Название</th>
//     </thead>
//     <tbody>
//       // Вывод результата в цикле
//       <tr th:each="company : ${companies}" th:object="${article}">
//         <td th:text="*{id}"></td>
//         <td>
//           <a th:text="*{title}" th:href="@{/articles/{id}(id=*{id})}"></a>
//         </td>
//       </tr>
//   </table>
//   <div class="container">
//    // Так при помощи модификатора th:href можно динамически
//    // сформировать ссылку со строкой запроса
//    // Эквивалентно <a href="/companies?key=15">Ссылка</a> при value=5
//     <a th:href="@{/companies(key=${value + 10})}">Ссылка</a>
//   </div>
//   <!-- END -->
// </section>
