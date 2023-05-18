package request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import db.Database;
import model.User;

public class RequestBody {
	private static final int QUERY_INDEX = 1;
	private static final int USER_ID = 0;
	private static final int PASSWORD = 1;
	private static final int NICKNAME = 2;
	private static final int EMAIL = 3;

	public static void process(BufferedReader br, String url, int contentLength) throws IOException {
		char[] reader = new char[contentLength];
		br.read(reader);
		String[] queries = Arrays.toString(reader).split("&");
		if(url.contains("/user/create")) {
			Database.addUser(new User(splitQuery(queries[USER_ID]), splitQuery(queries[PASSWORD]), splitQuery(queries[NICKNAME]), splitQuery(queries[EMAIL])));
		}
	}

	private static String splitQuery(String query) {
		return query.split("=")[QUERY_INDEX];
	}
}
