package exercise;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;
import java.io.File;

import exercise.servlet.WelcomeServlet;
import exercise.servlet.UsersServlet;
import exercise.servlet.SessionServlet;

import org.thymeleaf.TemplateEngine;
import javax.servlet.ServletContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;

public class App {

    private static Users users;

    public static Users getUsers() {
        return users;
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.valueOf(port);
        }
        return 5000;
    }

    public static Tomcat getApp(int port) {
        // Инициализируем логгер при старте приложения
        LoggerFactory.initLogger();

        users = new Users();

        Tomcat tomcat = new Tomcat();

        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(port);

        Context ctx = tomcat.addContext("", new File("src/main/webapp").getAbsolutePath());

        // ************************************
        // Подключаем template engine Thymeleaf
        TemplateEngine templateEngine = new TemplateEngine();

        ServletContext servletContext = ctx.getServletContext();
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

        tomcat.addServlet(ctx, UsersServlet.class.getSimpleName(), new UsersServlet());
        ctx.addServletMappingDecoded("/users/*", UsersServlet.class.getSimpleName());

        tomcat.addServlet(ctx, SessionServlet.class.getSimpleName(), new SessionServlet());
        ctx.addServletMappingDecoded("/login", SessionServlet.class.getSimpleName());
        ctx.addServletMappingDecoded("/logout", SessionServlet.class.getSimpleName());

        return tomcat;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat app = getApp(getPort());
        app.start();
        app.getServer().await();
    }
}
