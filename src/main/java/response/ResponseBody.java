package response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import db.Database;
import model.Article;
import model.User;
import webserver.SessionId;

public class ResponseBody {
	private static final String HTML_PATH = "src/main/resources/templates";
	private static final String STATIC_PATH = "src/main/resources/static";
	static final String HOME_PATH = "/index.html";
	static final String LOGIN_FAIL_PATH = "/user/login_failed.html";
	static final String LOGIN_PATH = "/user/login.html";
	static final int SID_LENGTH = 8;
	static final int NAME_LENGTH = 8;
	static final int LIST_LENGTH = 9;
	static final int ARTICLE_LENGTH = 12;

	private byte[] body;

	public ResponseBody() {}

	public void setBody(String url, SessionId sessionId, Long index) throws IOException {
		if(url.contains("html")) {
			StringBuffer stringBuffer = new StringBuffer(Files.readString(new File(HTML_PATH + url).toPath()));
			stringBuffer = checkSid(stringBuffer, sessionId.getSid(), url);
			stringBuffer = showArticle(stringBuffer, url, index);
			body = String.valueOf(stringBuffer).getBytes();
		} else if(url.equals("/") || url.equals("/user/logout")){
			StringBuffer stringBuffer = new StringBuffer(Files.readString(new File(HTML_PATH + HOME_PATH).toPath()));
			stringBuffer = checkSid(stringBuffer, sessionId.getSid(), url);
			body = String.valueOf(stringBuffer).getBytes();
		} else if(url.contains(".")){
			body = Files.readAllBytes(new File(STATIC_PATH + url).toPath());
		}
		if(body == null) {
			body = new byte[0];
		}
	}

	private StringBuffer showArticle(StringBuffer stringBuffer, String url, Long articleIndex) {
		if(url.equals("/qna/show.html")) {
			Article article = Database.findArticleByIndex(articleIndex);
			int articleStart = stringBuffer.indexOf("{{#article}}");
			int articleEnd = stringBuffer.indexOf("{{/article}}", articleStart);
			String repeat = stringBuffer.substring(articleStart + ARTICLE_LENGTH, articleEnd);
			StringBuilder list = new StringBuilder();
			String articles = repeat;
			articles = articles.replace("{{title}}", article.getTitle());
			articles = articles.replace("{{author}}", article.getAuthor());
			articles = articles.replace("{{contents}}", article.getContents());
			articles = articles.replace("{{writeDate}}", article.getWriteDate());
			articles = articles.replace("{{hits}}", String.valueOf(article.getHits()));
			list.append(articles);
			stringBuffer.replace(articleStart, articleEnd + ARTICLE_LENGTH, list.toString());
		}
		return stringBuffer;
	}

	private StringBuffer checkSid(StringBuffer stringBuffer, String sid, String url) throws IOException {
		if(sid == null) {
			if(url.equals("/user/list.html") || url.equals("/qna/form.html")) {
				stringBuffer = new StringBuffer(Files.readString(new File(HTML_PATH + LOGIN_PATH).toPath()));
			}
			int startIndex = stringBuffer.indexOf("{{#sid}}");
			int endIndex = stringBuffer.indexOf("{{/sid}}", startIndex);
			deleteStringBuffer(stringBuffer, startIndex, endIndex);

			int start = stringBuffer.indexOf("{{^sid}}");
			deleteStringBuffer(stringBuffer, start, start);
			int end = stringBuffer.indexOf("{{/sid}}", start);
			deleteStringBuffer(stringBuffer, end, end);
		} else {
			if(url.equals("/user/list.html")) {
				List<User> userList = new ArrayList<>(Database.findAllUsers());
				int listStart = stringBuffer.indexOf("{{#list}}");
				int listEnd = stringBuffer.indexOf("{{/list}}", listStart);
				String repeat = stringBuffer.substring(listStart + LIST_LENGTH, listEnd);
				StringBuilder list = new StringBuilder();
				for (int i = userList.size() - 1; i >= 0; i--) {
					String users = repeat;
					users = users.replace("{{index}}", String.valueOf(i));
					users = users.replace("{{userId}}", userList.get(i).getUserId());
					users = users.replace("{{name}}", userList.get(i).getName());
					users = users.replace("{{email}}", userList.get(i).getEmail());
					list.append(users);
				}
				stringBuffer.replace(listStart, listEnd + LIST_LENGTH, list.toString());
			}
			int startIndex = stringBuffer.indexOf("{{^sid}}");
			int endIndex = stringBuffer.indexOf("{{/sid}}", startIndex);
			deleteStringBuffer(stringBuffer, startIndex, endIndex);

			int start = stringBuffer.indexOf("{{#sid}}");
			deleteStringBuffer(stringBuffer, start, start);
			int end = stringBuffer.indexOf("{{/sid}}", start);
			deleteStringBuffer(stringBuffer, end, end);

			int name = stringBuffer.indexOf("{{name}}");
			stringBuffer.replace(name, name + NAME_LENGTH, Database.findUserBySid(sid).getName());
		}
		stringBuffer = showArticles(stringBuffer, url);
		return stringBuffer;
	}

	private StringBuffer showArticles(StringBuffer stringBuffer, String url) {
		if(url.equals("/index.html")) {
			List<Article> articleList = new ArrayList<>(Database.findAllArticles());
			int listStart = stringBuffer.indexOf("{{#list}}");
			int listEnd = stringBuffer.indexOf("{{/list}}", listStart);
			String repeat = stringBuffer.substring(listStart + LIST_LENGTH, listEnd);
			StringBuilder list = new StringBuilder();
			for (int i = articleList.size() - 1; i >= 0; i--) {
				String articles = repeat;
				articles = articles.replace("{{articleIndex}}", String.valueOf(articleList.get(i).getArticleIndex()));
				articles = articles.replace("{{title}}", articleList.get(i).getTitle());
				articles = articles.replace("{{author}}", articleList.get(i).getAuthor());
				articles = articles.replace("{{writeDate}}", articleList.get(i).getWriteDate().substring(0, 8));
				articles = articles.replace("{{hits}}", String.valueOf(articleList.get(i).getHits()));
				list.append(articles);
			}
			stringBuffer.replace(listStart, listEnd + LIST_LENGTH, list.toString());
		}
		return stringBuffer;
	}

	private void deleteStringBuffer(StringBuffer stringBuffer, int startIndex, int endIndex) {
		if(startIndex != -1 && endIndex != -1) {
			stringBuffer.delete(startIndex, endIndex + SID_LENGTH);
		}
	}

	public byte[] getBody() {
		return body;
	}
}
