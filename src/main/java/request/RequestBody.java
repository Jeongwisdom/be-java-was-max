package request;

import java.io.BufferedReader;
import java.io.IOException;

import db.Database;
import model.User;
import response.Status;
import webserver.SessionId;

public class RequestBody {
	private static final int QUERY_INDEX = 1;
	private static final int USER_ID = 0;
	private static final int PASSWORD = 1;
	private static final int NICKNAME = 2;
	private static final int EMAIL = 3;
	private static final int AUTHOR = 0;
	private static final int TITLE = 1;
	private static final int CONTENTS = 2;

	public static Status process(BufferedReader br, String requestTarget, SessionId sessionId, int contentLength) throws IOException {
		char[] reader = new char[contentLength];
		Status status = Status.BAD_REQUEST;
		br.read(reader);
		String[] queries = String.valueOf(reader).split("&");
		if(requestTarget.equals("/user/create")) {
			status = createUser(queries);
		}
		if(requestTarget.equals("/user/login")) {
			status = userLogin(queries, sessionId);
		}
		if(requestTarget.equals("/qna/articles")) {
			status = writeArticle(queries, sessionId);
		}
		return status;
	}

	private static Status writeArticle(String[] queries, SessionId sessionId) {
		if(sessionId != null) {
			Database.addArticle(splitQuery(queries[AUTHOR]), splitQuery(queries[TITLE]), splitQuery(queries[CONTENTS]));
			return Status.FOUND_SUCCESS;
		}
		return Status.FOUND_LOGIN_FAIL;
	}

	private static Status userLogin(String[] queries, SessionId sessionId) {
		User user = Database.findUserById(splitQuery(queries[USER_ID]));
		if(user == null || !(user.getPassword().equals(splitQuery(queries[PASSWORD])))) {
			return Status.FOUND_LOGIN_FAIL;
		}
		sessionId.SetSid();
		Database.addSession(sessionId.getSid(), user.getUserId());
		return Status.FOUND_SUCCESS;
	}

	private static Status createUser(String[] queries) {
		Database.addUser(new User(splitQuery(queries[USER_ID]), splitQuery(queries[PASSWORD]), splitQuery(queries[NICKNAME]), splitQuery(queries[EMAIL])));
		return Status.FOUND_SUCCESS;
	}

	private static String splitQuery(String query) {
		return query.split("=")[QUERY_INDEX];
	}
}
