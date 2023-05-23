package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Map.of(
        "wis", new User("wis","a", "wis", "wis@abc.de")
    );
    private static Map<String, User> session = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static void addSession(String sessionId, String userId) {
        User user = findUserById(userId);
        session.put(sessionId, user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static User findUserBySid(String sid) {
        return session.get(sid);
    }

    public static User deleteSid(String sid) {
        return session.remove(sid);
    }
}
