<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="tag" tagdir="/WEB-INF/tags" %>

<tag:application>
    <h1>Регистрация</h1>
    <form action="/users/new" method="post">
        <div class="mb-3">
            <label>Имя</label>
            <input class="form-control" type="text" name="firstName" value='${user.getOrDefault("firstName", "")}'>
        </div>
        <div class="mb-3">
            <label>Фамилия</label>
            <input class="form-control" type="text" name="lastName" value='${user.getOrDefault("lastName", "")}'>
        </div>
        <div class="mb-3">
            <label>Email</label>
            <input class="form-control" type="email" name="email">
        </div>
        <button class="btn btn-primary" type="submit">Создать</button>
    </form>
</tag:application>
