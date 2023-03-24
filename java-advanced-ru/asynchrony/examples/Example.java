class Example {
    public static void main(String[] args) {

        // Если нам нужно просто асинхронно выполнить задачу и не нужно ничего возвращать

        // В виде лямбды передаём объект Runnable
        CompletableFuture<Void> future1 = CompletableFuture.runAsync(() -> {
            // Задержка имитирует длительно выполняющуюся задачу
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            System.out.println("Run in separate thread");
        });

        // Ожидаем окончания работы задачи
        future1.get();


        // Асинхронный запуск задачи и возврат результата работы

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // Задержка имитирует длительно выполняющуюся задачу
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
            return "Result of async computation";
        });

        // Ожидаем окончания работы задачи и получаем результат
        String result = future2.get(); // "Result of async computation"

        // Комбинирование двух CompletableFuture

        // Если одна задача не зависит от другой
        // Нужно выполнить две задачи независимо одна от другой и выполнить третью,
        // когда предыдущее будут завершены


        // Задачи выполняются независимо друг от друга
        System.out.println("Retrieving weight");
        CompletableFuture<Integer> futureWeight = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            return 100;
        });

        System.out.println("Retrieving volume");
        CompletableFuture<Integer> futureVolume = CompletableFuture.supplyAsync(() -> {

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
            return 2;
        });

        // выполняется после завершения первых двух
        System.out.println("Calculate density");
        CompletableFuture<Integer> futureDensity = futureWeight.thenCombine(futureVolume, (weight, volume) -> {
            Integer density = weight / volume;
            return density;

        // Обработка исключений
        // Если при работе задач возникли исключения
        // их можно обработать в методе exceptionally
        }).exceptionally(ex -> {
            System.out.println("Oops! We have an exception - " + ex.getMessage());
            return null;
        });
    }
}
