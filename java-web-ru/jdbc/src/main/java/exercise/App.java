package exercise;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import java.io.File;
import java.io.IOException;

import exercise.servlet.WelcomeServlet;
import exercise.servlet.ArticlesServlet;

import org.thymeleaf.TemplateEngine;
import javax.servlet.ServletContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.Files;


public class App {

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.valueOf(port);
        }
        return 5000;
    }

    private static String getFileContent(String fileName) throws IOException {
        Path pathToSolution = Paths.get(fileName).toAbsolutePath();
        return Files.readString(pathToSolution).trim();
    }

    public static Tomcat getApp(int port, Connection dbConnection) {

        Tomcat tomcat = new Tomcat();

        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(port);

        Context ctx = tomcat.addContext("", new File("src/main/webapp").getAbsolutePath());

        // ************************************
        // Подключаем template engine Thymeleaf
        TemplateEngine templateEngine = new TemplateEngine();

        ServletContext servletContext = ctx.getServletContext();
        servletContext.setAttribute("dbConnection", dbConnection);
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver(servletContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setCharacterEncoding("UTF-8");
        resolver.setTemplateMode(TemplateMode.HTML);

        templateEngine.setTemplateResolver(resolver);
        templateEngine.addDialect(new LayoutDialect());

        TemplateEngineUtil.storeTemplateEngine(servletContext, templateEngine);
        // ************************************

        tomcat.addServlet(ctx, WelcomeServlet.class.getSimpleName(), new WelcomeServlet());
        ctx.addServletMappingDecoded("", WelcomeServlet.class.getSimpleName());

        tomcat.addServlet(ctx, ArticlesServlet.class.getSimpleName(), new ArticlesServlet());
        ctx.addServletMappingDecoded("/articles/*", ArticlesServlet.class.getSimpleName());


        return tomcat;
    }

    public static void main(String[] args) throws LifecycleException, SQLException, IOException {
        // BEGIN
        Connection connection = DriverManager.getConnection("jdbc:h2:./hexlet");
        String initSql = getFileContent("init.sql");
        Statement statement = connection.createStatement();
        statement.execute(initSql);
        // END
        int port = getPort();
        Tomcat app = getApp(port, connection);
        app.start();
        app.getServer().await();
    }
}
