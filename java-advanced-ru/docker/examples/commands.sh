# Используется для сборки образа
# Флаг -t позволяет указать имя образа
docker build . -t my_account_name/awesome-image
# Показывает все собранные образы
docker images
# Запускает контейнер
# Флаг -p используется для проброса портов: <порт внутри контейнера>:<порт доступный снаружи>
docker run -it -p 80:8080 my_account_name/awesome-image
# Отправляет образ на dockerhub
docker push my_account_name/awesome-image
# А так мы можем запустить образ и подключиться к нему с помощью bash
docker run -it -p 80:8080 my_account_name/awesome-image bash
