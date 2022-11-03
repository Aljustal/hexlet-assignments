package exercise.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import io.ebean.PagedList;

import exercise.TemplateEngineUtil;
import exercise.domain.Article;
import exercise.domain.Category;
// Эти классы создаются автоматически для каждой сущности
// К названию добавляется префикс Q
import exercise.domain.query.QArticle;
import exercise.domain.query.QCategory;

public class ArticlesServlet extends HttpServlet {

    private String getId(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return null;
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 1, null);
    }

    private String getAction(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            return "list";
        }
        String[] pathParts = pathInfo.split("/");
        return ArrayUtils.get(pathParts, 2, getId(request));
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                showArticles(request, response);
                break;
            case "new":
                newArticle(request, response);
                break;
            default:
                showArticle(request, response);
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
                throws IOException, ServletException {

        String action = getAction(request);

        switch (action) {
            case "list":
                createArticle(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void showArticles(HttpServletRequest request,
                          HttpServletResponse response)
                throws IOException, ServletException {

        int articlesPerPage = 10;
        String page = request.getParameter("page");
        int normalizedPage = page == null ? 1 : Integer.parseInt(page);
        int offset = (normalizedPage - 1) * articlesPerPage;

        // BEGIN
        PagedList<Article> pagedArticles = new QArticle()
                .setFirstRow(offset)
                .setMaxRows(articlesPerPage)
                .orderBy()
                .id.asc()
                .findPagedList();
        List<Article> articles = pagedArticles.getList();
        request.setAttribute("articles", articles);
        // END
        request.setAttribute("page", normalizedPage);
        TemplateEngineUtil.render("articles/index.html", request, response);
    }

    private void showArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        long id = Long.parseLong(getId(request));

        // BEGIN
        Article article = new QArticle()
                .id.equalTo(id)
                .findOne();
        request.setAttribute("article", article);
        // END
        TemplateEngineUtil.render("articles/show.html", request, response);
    }

    private void newArticle(HttpServletRequest request,
                            HttpServletResponse response)
                    throws IOException, ServletException {

        // BEGIN
        List<Category> category = new QCategory().findList();
        request.setAttribute("categories", category);
        // END
        TemplateEngineUtil.render("articles/new.html", request, response);
    }

    private void createArticle(HttpServletRequest request,
                         HttpServletResponse response)
                 throws IOException, ServletException {

        HttpSession session = request.getSession();
        String title = request.getParameter("title");
        String body = request.getParameter("body");
        String categoryId = request.getParameter("categoryId");

        // BEGIN
        Category category = new QCategory()
                .id.equalTo(Integer.parseInt(categoryId))
                .findOne();
        Article article = new Article(title, body, category);
        article.save();
        // END

        session.setAttribute("flash", "Статья успешно создана");
        response.sendRedirect("/articles");
    }
}
