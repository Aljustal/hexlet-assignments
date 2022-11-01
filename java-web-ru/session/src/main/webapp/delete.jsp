<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<tag:application>
    <h1>Удаление пользователя</h1>
    <p>Вы уверены, что хотите удалить пользователя ${user.get("firstName")} ${user.get("lastName")}?</p>

    <form action='/users/delete?id=${user.get("id")}' method="post">
        <button type="submit" class="btn btn-danger">Да, удалить</button>
    </form>
</tag:application>
