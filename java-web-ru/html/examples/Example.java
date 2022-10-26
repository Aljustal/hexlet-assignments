package exercise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

public class CompaniesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // Список всех компаний
        List<String> companies = List.of("Google", "Apple", "Samsung");

        // Получаем информацию о пути из URL-адреса, на который был выполнен запрос
        String pathInfo = request.getPathInfo();

        // Если метод вернул `null`, значит запрос был выполнен на адрес `/companies`
        // Нужно вывести список всех компаний
        if (pathInfo == null) {
            StringBuilder body = new StringBuilder();
            // Выводим список всех компаний
            // Создаем html разметку при помощи String builder
            // Подключаем стили
            body.append("""
                <!DOCTYPE html>
                <html lang=\"ru\">
                    <head>
                        <meta charset=\"UTF-8\">
                        <title>Example application | Users</title>
                        <link rel=\"stylesheet\" href=\"mysite.css\">
                    </head>
                    <body>
                """);

            for (String companyName : companies) {
                body.append("<p>" + companyName + "</p>");
            }

            body.append("""
                    </body>
                </html>
                """);


            // Устанавливаем тип содержимого ответа и кодировку
            response.setContentType("text/html;charset=UTF-8");
            // При помощи метода setStatus() можно установить код ответа
            // response.setStatus(HttpServletResponseSC_OK)

            PrintWriter out = response.getWriter();
            out.println(body.toString());

            return;
        }

        // В другом случае, если метод getPathInfo() вернул путь
        // Разбиваем строку с путем в массив, который будет содержать составные части пути
        String[] pathParts = pathInfo.split("/");

        // Получаем id пользователя из пути как второй элемент массива
        String id = ArrayUtils.get(pathParts, 1, "");

        // Получаем компанию по id при помощи гипотетического метода getCompanyName()
        String companyName = getCompanyName(id);

        // Если кампании с таким id не существует, отправляем клиенту сообщение об ошибке с кодом ответа 404
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Создаем html разметку при помощи String builder и подключаем стили
        StringBuilder body = new StringBuilder();
        body.append("""
            <!DOCTYPE html>
            <html lang=\"ru\">
                <head>
                    <meta charset=\"UTF-8\">
                    <title>Example application | Users</title>
                    <link rel=\"stylesheet\" href=\"mysite.css\">
                </head>
                <body>
            """);

        body.append("<p> + companyName + </p>");

        body.append("""
                </body>
            </html>
            """);

        // Устанавливаем тип содержимого ответа и кодировку
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(body.toString());
    }
}
