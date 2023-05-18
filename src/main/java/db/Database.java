package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<String, User> session = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static void addSession(String sessionId, String userId) {
        session.put(sessionId, findUserById(userId));
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
