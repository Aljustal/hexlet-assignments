package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Map;

import static exercise.App.getUsers;
import exercise.Users;
import exercise.LoggerFactory;
import exercise.TemplateEngineUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionServlet extends HttpServlet {

    private Users users = getUsers();

    // Получаем экземпляр логгера
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionServlet.class);

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        if (request.getRequestURI().equals("/login")) {
            showLoginPage(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

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

    private void showLoginPage(HttpServletRequest request,
                               HttpServletResponse response)
                 throws IOException, ServletException {

        Map<String, String> user = users.build();
        request.setAttribute("user", user);
        TemplateEngineUtil.render("session/login.html", request, response);
    }

    private void login(HttpServletRequest request,
                               HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        Map<String, String> userData = users.build(email);

        Map<String, String> user = users.findByEmail(email);

        // BEGIN
        LOGGER.log(Level.INFO, email + "");
        // END

        if (user == null || !user.get("password").equals(password)) {
            request.setAttribute("user", userData);
            session.setAttribute("flash", "Неверные логин или пароль");
            response.setStatus(422);
            TemplateEngineUtil.render("session/login.html", request, response);
            return;
        }

        session.setAttribute("userId", user.get("id"));
        session.setAttribute("flash", "Вы успешно вошли");
        response.sendRedirect("/");
    }

    private void logout(HttpServletRequest request,
                               HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();
        session.removeAttribute("userId");
        session.setAttribute("flash", "Вы успешно вышли");
        response.sendRedirect("/");
    }
}
