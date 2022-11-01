package exercise;

import java.util.Random;
import java.util.Locale;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import java.util.Collections;

public class Users {
    private final int usersCount = 100;
    private int idCounter = usersCount;
    private List<Map<String, String>> users;

    public Users() {
        Random random = new Random(123);
        Faker faker = new Faker(new Locale("en"), random);

        List<String> ids = IntStream
            .range(1, usersCount + 1)
            .mapToObj(i -> Integer.toString(i))
            .collect(Collectors.toList());
        Collections.shuffle(ids, random);

        users = new ArrayList<>();

        for (int i = 0; i < usersCount; i++) {
            Map<String, String> user = new HashMap<>();
            user.put("id", ids.get(i));
            user.put("firstName", faker.address().firstName());
            user.put("lastName", faker.address().lastName());
            user.put("email", faker.internet().emailAddress());
            user.put("password", "password");
            users.add(user);
        }
    }

    private String getNextId() {
        int id = ++idCounter;
        return Integer.toString(id);
    }

    public Map<String, String> build(String firstName, String lastName, String email) {
        Map<String, String> user = new HashMap<>();

        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("email", email);

        return user;
    }

    public Map<String, String> build(String email) {
        Map<String, String> user = new HashMap<>();

        user.put("email", email);

        return user;
    }

    public Map<String, String> build() {
        Map<String, String> user = new HashMap<>();

        return user;
    }

    public void add(Map<String, String> user) {
        user.put("id", getNextId());
        users.add(user);
    }

    public void update(Map<String, String> user,
                       Map<String, String> updatedUserData) {
        updatedUserData.put("id", user.get("id"));
        user.putAll(updatedUserData);
    }

    public void remove(Map<String, String> user) {
        users.remove(user);
    }

    public List<Map<String, String>> getAll() {
        return users;
    }

    public Map<String, String> findById(String id) {
        Map<String, String> user = users
            .stream()
            .filter(u -> u.get("id").equals(id))
            .findAny()
            .orElse(null);

        return user;
    }

    public Map<String, String> findByEmail(String email) {
        Map<String, String> user = users
            .stream()
            .filter(u -> u.get("email").equals(email))
            .findAny()
            .orElse(null);

        return user;
    }
}
