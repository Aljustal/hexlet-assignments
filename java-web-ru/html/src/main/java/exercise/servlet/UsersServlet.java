package exercise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.Objects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.ArrayUtils;

public class UsersServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String pathInfo = request.getPathInfo();

        if (pathInfo == null) {
            showUsers(request, response);
            return;
        }

        String[] pathParts = pathInfo.split("/");
        String id = ArrayUtils.get(pathParts, 1, "");

        showUser(request, response, id);
    }

    private List getUsers() throws JsonProcessingException, IOException {
        // BEGIN
        ObjectMapper mapper = new ObjectMapper();
        String content = Files.readString(Path.of("src/main/resources/users.json"));
        return mapper.readValue(content, new TypeReference<List<Map<String, String>>>() {
        });
        // END
    }

    private void showUsers(HttpServletRequest request,
                          HttpServletResponse response)
                throws IOException {
        // BEGIN
        List<Map<String, String>> users = getUsers();

        StringBuilder body = new StringBuilder();

        body.append("""
                <!DOCTYPE html>
                <html lang=\"ru\">
                    <body>
                """);

        for (var user : users) {
            body.append("<table><tr><td>" + user.get("id"));
            body.append("<td><a href=\"/users/" + user.get("id") + "\">");
            body.append(user.get("firstName") + " " + user.get("lastName"));
            body.append("</a></td></tr></table>");
        }
        body.append("""
                    </body>
                </html>
                """);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();
        out.println(body.toString());
        // END
    }

    private void showUser(HttpServletRequest request,
                         HttpServletResponse response,
                         String id)
                 throws IOException {

        // BEGIN
        List<Map<String, String>> users = getUsers();

        var user = users.stream()
                .filter(u -> Objects.equals(u.get("id"), id))
                .findAny()
                .orElse(null);

        response.setContentType("text/html;charset=UTF-8");

        if (user != null) {
            String test = "<!DOCTYPE html>\n"
                    +
                    "            <html lang=\"ru\">\n"
                    +
                    "               <table>"
                    +
                    "                   <tr>"
                    +
                    "                       <td>" + user.get("firstName") + "</td>"
                    +
                    "                   </tr>"
                    +
                    "                   <tr>"
                    +
                    "                       <td>" + user.get("lastName") + "</td>"
                    +
                    "                   </tr>"
                    +
                    "                   <tr>"
                    +
                    "                       <td>" + user.get("id") + "</td>"
                    +
                    "                   </tr>"
                    +
                    "                   <tr>"
                    +
                    "                       <td>" + user.get("email") + "</td>"
                    +
                    "                   </tr>"
                    +
                    "               </table>"
                    +
                    "            </html>";
            PrintWriter out = response.getWriter();
            out.println(test.toString());
        } else {
            response.sendError(404, "Not found");
        }
        // END
    }
}
