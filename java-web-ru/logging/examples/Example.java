// // Файл конфигурации логгера

// // Обработчики
// // Вывод логов в файл и в консоль
// handlers = java.util.logging.FileHandler, java.util.logging.ConsoleHandler

// // Настраиваем обработчик FileHandler, который пишет логи в файл
// // Уровень логгирования
// java.util.logging.FileHandler.level     = INFO
// // Формат вывода
// java.util.logging.FileHandler.formatter = java.util.logging.SimpleFormatter
// java.util.logging.FileHandler.append    = true
// // Файл, в который будет происходить запись лога
// java.util.logging.FileHandler.pattern   = log.txt


// // Сервлет

// public class WelcomeServlet extends HttpServlet {
//     // Получаем логгер
//     private static final Logger LOGGER = LoggerFactory.getLogger(WelcomeServlet.class);

//     @Override
//     public void doGet(HttpServletRequest request,
//                       HttpServletResponse response)
//                 throws IOException, ServletException {
//         // Производим запись лога с уровнем INFO
//         // При каждом GET запросе на стартовую страницу, в файл будет записываться лог
//         LOGGER.log(Level.INFO, "Страница загружена");

//         PrintWriter out = response.getWriter();
//         out.println("Hello, Hexlet!");
//     }
// }
