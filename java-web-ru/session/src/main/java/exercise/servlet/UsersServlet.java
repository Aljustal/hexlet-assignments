package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;

import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import static exercise.App.getUsers;
import exercise.Users;

public class UsersServlet extends HttpServlet {

    private Users users = getUsers();

    private String getId(HttpServletRequest request) {
        return request.getParameter("id");
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "list";
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 1, "");
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                showUsers(request, response);
                break;
            case "new":
                newUser(request, response);
                break;
            case "edit":
                editUser(request, response);
                break;
            case "show":
                showUser(request, response);
                break;
            case "delete":
                deleteUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    // По правилам семантики HTTP изменение сущности правильно делать в методе doPut,
    // а удаление в doDelete. Но мы работаем с формами, а браузер умеет отправлять
    // запросы только методами GET и POST.
    // Большинство фреймворков обходят это ограничение при помощи специального механизма.
    // Так как пока мы не работаем с фреймворком, в домашних работах для простоты
    // мы будем обрабатывать эти запросы в методе doPost, чтобы не использовать Javascript.
    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "new":
                createUser(request, response);
                break;
            case "edit":
                updateUser(request, response);
                break;
            case "delete":
                destroyUser(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showUsers(HttpServletRequest request,
                          HttpServletResponse response)
                throws IOException, ServletException {

        request.setAttribute("users", users.getAll());
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/users.jsp");
        requestDispatcher.forward(request, response);
    }


    private void showUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {
        String id = getId(request);

        Map<String, String> user = users.findById(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("user", user);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/show.jsp");
        requestDispatcher.forward(request, response);
    }

    private void newUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        Map<String, String> user = users.build();

        request.setAttribute("user", user);
        request.setAttribute("error", "");

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/new.jsp");
        requestDispatcher.forward(request, response);
    }

    private void createUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        Map<String, String> user = users.build(firstName, lastName, email);

        if (firstName.isEmpty() || lastName.isEmpty()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/new.jsp");
            request.setAttribute("user", user);
            session.setAttribute("flash", "Имя и Фамилия не могут быть пустыми");
            response.setStatus(422);
            requestDispatcher.forward(request, response);
            return;
        }

        users.add(user);
        session.setAttribute("flash", "Пользователь успешно создан");
        response.sendRedirect("/users");
    }

    private void editUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        String id = getId(request);

        Map<String, String> user = users.findById(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("user", user);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/edit.jsp");
        requestDispatcher.forward(request, response);
    }

    private void updateUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();

        String id = getId(request);

        Map<String, String> user = users.findById(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        Map<String, String> updatedUserData = users.build(firstName, lastName, email);

        if (firstName.isEmpty() || lastName.isEmpty()) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/edit.jsp");
            request.setAttribute("user", updatedUserData);
            session.setAttribute("flash", "Имя и Фамилия не могут быть пустыми");
            response.setStatus(422);
            requestDispatcher.forward(request, response);
            return;
        }

        users.update(user, updatedUserData);
        session.setAttribute("flash", "Пользователь успешно изменён");
        response.sendRedirect("/users");
    }

    private void deleteUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        String id = getId(request);

        Map<String, String> user = users.findById(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        request.setAttribute("user", user);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/delete.jsp");
        requestDispatcher.forward(request, response);

    }

    private void destroyUser(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();

        String id = getId(request);

        Map<String, String> user = users.findById(id);

        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        users.remove(user);
        session.setAttribute("flash", "Пользователь успешно изменён");
        response.sendRedirect("/users");
    }
}
