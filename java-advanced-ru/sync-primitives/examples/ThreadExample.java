// Разделяемый ресурс
public class CommonResource {
    private int counter = 0;

    // Метод, у котрого нужно ограничить выполнение
    // Одновременно только один поток может выполнять метод increaseCounter()
    public synchronized void increaseCounter() {
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}

// Создаё класс потока
public class ThreadExample extends Thread {

    // Разделяемый ресурс
    CommonResource resource;

    ThreadExample(CommonResource resource) {
        this.resource = resource;
    }

    // Метод увеличивает счетчик на 1
    @Override
    public void run() {
        resource.increaseCounter();
    }
}

public class Example {

    public static void main(String[] args) {

        CommonResource resource = new CommonResource();

        // Создадим два потока, которые будут менять разделяемый ресурс
        ThreadExample thread1 = new ThreadExample(resource);
        ThreadExample thread2 = new ThreadExample(resource);

        // Запускаем потоки
        thread1.start();
        thread2.start();

        // Дожидаемся окончания выполнения потоков
        try {

            thread1.join();
            thread2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Проверяем результат
        System.out.println("Size: " + resource.getCounter());
    }
}

// Вывод в консоль

// => "Size: 2"
