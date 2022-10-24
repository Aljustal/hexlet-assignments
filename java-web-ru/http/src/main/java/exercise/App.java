package exercise;

import exercise.servlet.UploadServlet;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Context;

import java.io.File;

public class App {

    private static int getPort() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.valueOf(port);
        }
        return 5000;
    }

    public static Tomcat getApp(int port) {
        Tomcat app = new Tomcat();

        app.setBaseDir(System.getProperty("java.io.tmpdir"));
        app.getHost().setName("hexlet.local");
        app.setPort(port);

        Context ctx = app.addContext("", new File(".").getAbsolutePath());

        app.addServlet(ctx, UploadServlet.class.getSimpleName(), new UploadServlet());
        ctx.addServletMappingDecoded("/upload", UploadServlet.class.getSimpleName());

        return app;
    }

    public static void main(String[] args) throws LifecycleException {
        Tomcat app = getApp(getPort());
        app.start();
        app.getServer().await();
    }
}
