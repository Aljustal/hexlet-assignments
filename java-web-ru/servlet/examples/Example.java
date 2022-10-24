// import exercise.servlet.WelcomeServlet;
// import org.apache.catalina.LifecycleException;
// import org.apache.catalina.startup.Tomcat;
// import org.apache.catalina.Context;

// public class Example {
//     public static void main(String[] args) throws LifecycleException {
//         // создаём инстанс контенера сервлетов Tomcat
//         Tomcat app = new Tomcat();

//         // Tomcat требуется указать директорию для временных файлов
//         // Это метод должен быть вызван первым после создания инстанса
//         app.setBaseDir(System.getProperty("java.io.tmpdir"));

//         // Задаём порт на котором можно будет открыть приложение
//         // По умолчанию Tomcat будет использовать хост 0.0.0.0, то есть
//         // все доступные в системе, в т.ч localhost
//         app.setPort(8080);

//         // Создание контекста в программном режиме (без web.xml)
//         Context ctx = app.addContext("", new File(".").getAbsolutePath());

//         // Регистрируем сервлет
//         app.addServlet(ctx, "HelloServlet", new HelloServlet());
//         // Назначаем сервлет как отбработчик запросов по пути "/hello"
//         // На примере хоста и порта выше - "http://localhost:8080/hello"
//         ctx.addServletMappingDecoded("/hello", "HelloServlet"));

//         // Запуск сервера
//         app.start();
//         app.getServer().await();

//         // Программная остановка сервера
//         // app.stop();
//     }
// }


// import java.io.IOException;
// import java.io.PrintWriter;
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;

// public class HelloServlet extends HttpServlet {

//     @Override
//     // С помощью метода doGet() сервлет сможет обрабатывать GET запросы
//     public void doGet(HttpServletRequest request,
//                       HttpServletResponse response)
//                 throws IOException, ServletException {

//         // Помещаем в ответ строку "Hello, World!"
//         PrintWriter out = response.getWriter();
//         out.println("Hello, World!");
//     }
// }
