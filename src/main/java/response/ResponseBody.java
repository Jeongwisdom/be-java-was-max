package response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import db.Database;
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

	private byte[] body;

	public ResponseBody() {}

	public void setBody(String url, SessionId sessionId) throws IOException {
		if(url.contains("html")) {
			StringBuffer stringBuffer = new StringBuffer(Files.readString(new File(HTML_PATH + url).toPath()));
			stringBuffer = checkSid(stringBuffer, sessionId.getSid(), url);
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

	private StringBuffer checkSid(StringBuffer stringBuffer, String sid, String url) throws IOException {
		if(sid == null) {
			if(url.equals("/user/list.html")) {
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
				List<User> userList = new ArrayList<>(Database.findAll());
				int listStart = stringBuffer.indexOf("{{#list}}");
				int listEnd = stringBuffer.indexOf("{{/list}}", listStart);
				String repeat = stringBuffer.substring(listStart + LIST_LENGTH, listEnd);
				StringBuilder list = new StringBuilder();
				for (int i = 0; i < userList.size(); i++) {
					repeat = repeat.replace("{{index}}", String.valueOf(i));
					repeat = repeat.replace("{{userId}}", userList.get(i).getUserId());
					repeat = repeat.replace("{{name}}", userList.get(i).getName());
					repeat = repeat.replace("{{email}}", userList.get(i).getEmail());
					list.append(repeat);
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
