package exercise.controllers;

import io.javalin.http.Context;
import io.javalin.apibuilder.CrudHandler;
import io.ebean.DB;
import java.util.List;

import exercise.domain.User;
import exercise.domain.query.QUser;

import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.lang3.StringUtils;

public class UserController implements CrudHandler {

    public void getAll(Context ctx) {
        // BEGIN
        List<User> users = new QUser()
                .orderBy()
                .id.asc()
                .findList();

        String json = DB.json().toJson(users);
        ctx.json(json);
        // END
    };

    public void getOne(Context ctx, String id) {

        // BEGIN
        User user = new QUser()
                .id.equalTo(Integer.parseInt(id))
                .findOne();
        String json = DB.json().toJson(user);

        ctx.json(json);
        // END
    };

    public void create(Context ctx) {

        // BEGIN
        String body = ctx.body();
        User user = DB.json().toBean(User.class, body);

        user.save();
        // END
    };

    public void update(Context ctx, String id) {
        // BEGIN
        String body = ctx.body();
        User user = DB.json().toBean(User.class, body);

        user.setId(id);
        new QUser()
                .id.equalTo(user.getId())
                .asUpdate()
                .set("firstName", user.getFirstName())
                .set("lastName", user.getLastName())
                .set("email", user.getEmail())
                .set("password", user.getPassword())
                .update();
        // END
    };

    public void delete(Context ctx, String id) {
        // BEGIN
        User user = new QUser()
                .id.equalTo(Integer.parseInt(id))
                .findOne();
        assert user != null;
        user.delete();
        // END
    };
}
