# HTTP

Для работы над данным домашним заданием понадобится утилита *telnet*. В Linux её можно установить с помощью менеджера пакетов. Например, в Ubuntu это можно сделать с помощью команды `sudo apt-get install telnet`. В Windows для этого потребуется перейти в панель управления, а далее в разделе "установка приложений" выбрать "установка и удаление компонентов", где нужно будет отметить в списке пункт "Telnet Client".

## Ссылки

* [курс Протокол HTTP](https://ru.hexlet.io/courses/http_protocol)
* [урок Тело HTTP-запроса](https://ru.hexlet.io/courses/http_protocol/lessons/body/theory_unit)
* [Заголовок Content-Type](https://developer.mozilla.org/ru/docs/Web/HTTP/Headers/Content-Type)
* [Заголовок Content-Length](https://developer.mozilla.org/ru/docs/Web/HTTP/Headers/Content-Length)
* [Заголовок Connection](https://developer.mozilla.org/ru/docs/Web/HTTP/Headers/Connection)

## solution

## Задачи

* В упражнении уже реализован сервер, к которому можно подключиться и выполнять запросы. Выполните запуск сервера в терминале с помощью команды `make start`.

* Откройте второе окно/вкладку терминала и используя *telnet* подключитесь к *localhost* на порт *5000*. Выполните запрос к хосту *hexlet.local*. Параметры запроса: глагол *POST*, страница */upload*, протокол *HTTP 1.1*, тело: `my request body`. Не забудьте установить заголовки необходимые для отправки *body* (тела запроса). Если запрос выполнен правильно, сервер вернёт ответ с телом `data uploaded`.

* Запишите ваш HTTP-запрос в файл *solution*. Выполните запуск тестов и убедитесь, что они проходят.
  
 