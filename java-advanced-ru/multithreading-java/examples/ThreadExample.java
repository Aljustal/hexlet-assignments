// Чтобы создать поток, нужно создать класс и унаследовать его от класса Thread
public class ThreadExample extends Thread {

    // В классе нужно переопределить метод run()
    // В методе содержится логика, которую поток будет выполнять
    @Override
    public void run() {
        // Метод getName() выводит имя потока
        System.out.println("Thread " + getName());
    }
}

public class Example {

    public static void main(String[] args) {

        // Создадим 5 потоков в цикле
        for (int i = 0; i < 5; i++) {

            // Создаём экземпляр потока
            ThreadExample thread = new ThreadExample();
            // Запускаем поток при помощи метода start()
            // После этого начинает выполняться метод run() потока
            thread.start();
        }
    }
}

// Вывод

// Thread Thread-2
// Thread Thread-1
// Thread Thread-0
// Thread Thread-3
// Thread Thread-4

// Метод thread.join()

// Метод thread.join() останавливает выполнение текущего потока до тех пор,
// пока указанный поток не прекратит работу (или не будет прерван)


public class Example1 {

    public static void main(String[] args) {

        // Создаём поток
        // Допустим, поток выполняет какие-то сложные вычисления
        MyThread thread = new MyThread();
        // Запускаем поток
        thread.start();

        // Прежде, чем выводить результаты вычислений
        // нужно дождаться окончания выполнения потока thread
        // Метод join() приостановит выполнение потока main до тех пор,
        // пока поток thread не завершит работу

        try {
            thread.join();
        } catch (InterruptedException e) {
            System.out.println("Поток был прерван");
        }

        // После окончания работы потока выводим результат вычислений
        // Допустим, для получения результатов вычисления в классе MyThread
        // мы определили метод getResults()
        System.out.println(thread.getResults())
    }
}
