package db;

import com.google.common.collect.Maps;

import model.Article;
import model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static Map<String, User> users = new HashMap<>(){{
        put("wis", new User("wis","a", "wis", "wis@abc.de"));
    }};
    private static Map<Long, Article> articles = new HashMap<>(){{
        put(0L, new Article(0L, "wis", "HELLO", "안녕하세용", "2023-05-24 12:34:56", 0));
    }};
    private static Map<String, User> session = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static void addArticle(String author, String title, String contents) {
        Article article = new Article((long)articles.size(), author, title, contents, writeDate(), 0);
        articles.put(article.getArticleIndex(), article);
    }

    private static String writeDate() {
        LocalDateTime now = LocalDateTime.now();
        return now.format(DATE_FORMATTER);
    }

    public static void addSession(String sessionId, String userId) {
        User user = findUserById(userId);
        session.put(sessionId, user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAllUsers() {
        return users.values();
    }

    public static Collection<Article> findAllArticles() {
        return articles.values();
    }

    public static User findUserBySid(String sid) {
        return session.get(sid);
    }

    public static User deleteSid(String sid) {
        return session.remove(sid);
    }

    public static Article findArticleByIndex(Long articleIndex) {
        return articles.get(articleIndex);
    }
}
