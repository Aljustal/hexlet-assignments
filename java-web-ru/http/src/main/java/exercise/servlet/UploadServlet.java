package exercise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

public final class UploadServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String body = request
            .getReader()
            .lines()
            .collect(Collectors.joining(System.lineSeparator()));

        if (!body.equals("my request body")) {
            response.sendError(422);
        }

        PrintWriter out = response.getWriter();
        out.println("data uploaded");
    }
}
