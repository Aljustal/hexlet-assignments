# Деплой

В этом домашнем задании мы научимся обрабатывать строку запроса и выполнять деплой приложения. Деплой — процесс выкладки приложения на сервер (или сервера). В результате к приложению можно будет доступ через браузер. Для выполнения домашнего задания понадобятся утилиты *git* и *heroku* и зарегистрированный аккаунт на [heroku](https://www.heroku.com/). Либо *railway*, если вы используете сервис [Railway](https://railway.app/).

## Ссылки

* [Урок Передача данных query string](https://ru.hexlet.io/courses/http_protocol/lessons/query_string/theory_unit)
* [Установка утилиты Heroku](https://devcenter.heroku.com/articles/heroku-cli#download-and-install)
* [Сайт heroku](https://www.heroku.com/)
* [Деплой на heroku](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat#deploy-to-heroku)
* [Railway Getting Started](https://docs.railway.app/getting-started)

## servlet/CompaniesServlet.java

## Задачи

* Реализуйте логику метода `doGet()`, который должен отображать список компаний в зависимости от параметров запроса. Список всех доступных компаний генерируется методом `getCompanies()` (уже импортирован в модуль). При выводе списка компаний, сортировать их дополнительно не требуется.

  * Если строка запроса содержит параметр `search`, то должен выводиться список только тех компаний, которые содержат в имени переданное значение.

    ```text
    http://localhost:5000/companies?search=ov

    Cartwright-Glover and Sons
    Hermann, Macejkovic and Brekke Group
    ```

  * Если значение параметра `search` отсутствует в именах компаний, то должна выводиться строка `Companies not found`.

    ```text
    http://localhost:5000/companies?search=tl

    Companies not found
    ```

  * В случае отсутствия в строке запроса параметра `search` или если значением является пустая строка — должны выводиться все компании. Подробнее смотрите примеры.

    ```text
    http://localhost:5000/companies?search=
    http://localhost:5000/companies

    <список полностью всех компаний>
    ```

## Подсказки

* Для работы со строкой запроса вам понадобится использовать методы [getParameter()](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/ServletRequest.html#getParameter-java.lang.String-) и [getQueryString()](https://javaee.github.io/javaee-spec/javadocs/javax/servlet/http/HttpServletRequest.html#getQueryString--).

## App.java

## Задачи

* Зарегистрируйте в контейнере tomcat сервлет `CompaniesServlet`, так чтобы он обрабатывал запросы по пути `/companies`. Для проверки работы приложения запустите сервер, используя команду `make start`. Приложение в браузере будет доступно по адресу *http://localhost:5000*

## build.gradle

## Задачи

* В процессе деплоя heroku с помощью gradle будет запускать задачу *stage*, которую необходимо создать. В простейшем случае задача *stage* должна выполнять установку приложения (задачу *install*), но без запуска тестов — это можно сделать вызвав задачу *installDist*. Добавим задачу *stage* с нужной логикой:

  ```java
  task stage(dependsOn: ['clean', 'installDist'])
  installDist.mustRunAfter clean
  ```

## Procfile

## Задачи

* После успешной компиляции и сборки проекта на heroku, приложение нужно запустить. Это можно сделать если указать heroku команду которую нужно выполнить. Добавьте команду запуска исполняемого файла:

  ```bash
  web: sh build/install/deploy/bin/deploy
  ```

## Gradle Wrapper

## Задачи

* В процессе деплоя heroku автоматически определит, что используется проект gradle. Но для корректной работы, heroku необходимо использовать нужные версии gradle и java. Иначе будут использоваться версии по умолчанию, которые морально устарели, и с большой долей вероятности деплой завершится с ошибкой.

    Зафиксировать нужную версию gradle можно с помощью, так называемого Wrapper (локальной для проекта копии Gradle). Для этого выполните в директории домашнего задания команду:

    ```bash
    gradle wrapper --gradle-version 7.4
    ``` 

    Указать целевую версию java можно с помощью конфигурации в файле *system.properties*:

    ```text
    java.runtime.version=17
    ```

## Деплой

## Задачи

* Зарегистрируйте бесплатный аккаунт на [heroku](https://www.heroku.com/) или [Railway](https://railway.app/). Если вы из РФ, вам необходимо подключить [VPN](https://github.com/Hexlet/hexlet-unblock) и указать другую страну.
* В директории с домашним заданием выполните последовательно команды, как показано в [руководстве по деплою](https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat#deploy-to-heroku)
* После открытия задеплоенного приложения в браузере убедитесь, что оно работает.
* При сдаче домашнего задания укажите ссылку на задеплоенное приложение.
