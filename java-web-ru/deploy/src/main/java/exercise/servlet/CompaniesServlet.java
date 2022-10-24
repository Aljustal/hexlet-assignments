package exercise.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import static exercise.Data.getCompanies;

public class CompaniesServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        // BEGIN
        String requestParameter = request.getParameter("search");
        PrintWriter out = response.getWriter();
        List<String> companies = getCompanies();
        boolean isSearch = false;

        if (requestParameter != null && !requestParameter.isEmpty()) {
            for (var companie: companies) {
                if (companie.contains(requestParameter)) {
                    out.println(companie);
                    isSearch = true;
                }
            }
            if (!isSearch) {
                out.println("Companies not found");
            }
        } else {
            for (var companie: companies) {
                out.println(companie);
            }
        }
        // END
    }
}
