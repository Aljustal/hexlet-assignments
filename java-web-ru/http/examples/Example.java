// GET запрос с использованием версии протокола 1.1

// GET / HTTP/1.1
// host: google.com

// GET — метод запроса
// / — путь
// HTTP/1.1 — версия протокола
// host: google.com — обязательный заголовок, определяет, какой домен должен быть возвращен

// Ответ сервера на этот запрос:
// // Cтрока состояния (status line)
// HTTP/1.1 301 Moved Permanently
// // Заголовки
// Location: http://www.google.com/
// Content-Type: text/html; charset=UTF-8
// Date: Fri, 28 Feb 2020 06:06:40 GMT
// Expires: Sun, 29 Mar 2020 06:06:40 GMT
// Cache-Control: public, max-age=2592000
// Server: gws
// Content-Length: 219
// X-XSS-Protection: 0
// X-Frame-Options: SAMEORIGIN

// // Тело
// <HTML><HEAD><meta http-equiv="content-type" content="text/html;charset=utf-8">
// <TITLE>301 Moved</TITLE></HEAD><BODY>
// <H1>301 Moved</H1>
// The document has moved
// <A HREF="http://www.google.com/">here</A>.
// </BODY></HTML>

// Что из себя представляет строка ответа:

// HTTP/1.1 — версия протокола
// 301 — Код ответа
// Moved Permanently — сообщение


// И запрос, и ответ могут содержать тело. Тело отделяется от заголовков двумя переводами строки.

// Пример POST запроса, который содержит тело:

// POST / HTTP/1.1
// Host: hexlet.local
// Content-Type: text/plain
// Content-Length: 28
// Connection: close

// login=user&password=12345678 # отправляем данные

// Ответ сервера

// HTTP/1.1 200 OK
// X-Powered-By: Express
// Content-Type: text/html; charset=utf-8
// Content-Length: 28
// ETag: W/"1c-nN+JIy/AUI6NXluIMlVAfghl9f8"
// Date: Thu, 09 Jul 2020 03:15:54 GMT
// Connection: close

// login=user&password=12345678 # сервер отправляет обратно переданное тело
// Connection closed by foreign host.

// Заголовок Content-Type — указывает тип содержимого
// Заголовок Content-Length — длина тела (количество байт). После того, как передан такой заголовок,
// другая сторона будет ожидать ровно столько байт, сколько в нём указано.
// Как мы помним, для response и request это работает абсолютно одинаково.
// После того как был передан последний символ, соединение закрывается.
// Стоит уточнить, что закрывается именно HTTP-сессия.

