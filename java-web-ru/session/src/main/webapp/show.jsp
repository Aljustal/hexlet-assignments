<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<tag:application>
    <h1>Данные пользователя</h1>
    <div class="card">
        <ul class="list-group list-group-flush">
            <li class="list-group-item">id: ${user.get("id")}</li>
            <li class="list-group-item">Полное имя: ${user.get("firstName")} ${user.get("lastName")}</li>
            <li class="list-group-item">Email: ${user.get("email")}</li>
        </ul>
        <div class="card-footer">
            <a class="btn btn-primary" href='/users/edit?id=${user.get("id")}'>Редактировать</a>
            <a class="btn btn-primary" href='/users/delete?id=${user.get("id")}'>Удалить</a>
        </div>
    </div>
</tag:application>
