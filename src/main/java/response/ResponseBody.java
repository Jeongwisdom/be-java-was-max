package response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import db.Database;
import webserver.SessionId;

public class ResponseBody {
	private static final String HTML_PATH = "src/main/resources/templates";
	private static final String STATIC_PATH = "src/main/resources/static";
	static final String HOME_PATH = "/index.html";
	static final String LOGIN_FAIL_PATH = "/user/login_failed.html";
	static final int SID_LENGTH = 8;
	static final int NAME_LENGTH = 8;

	private byte[] body;

	public ResponseBody() {}

	public void setBody(String url, SessionId sessionId) throws IOException {
		if(url.contains("html")) {
			StringBuffer stringBuffer = new StringBuffer(Files.readString(new File(HTML_PATH + url).toPath()));
			checkSid(stringBuffer, sessionId.getSid());
			body = String.valueOf(stringBuffer).getBytes();
		} else if(url.equals("/")){
			StringBuffer stringBuffer = new StringBuffer(Files.readString(new File(HTML_PATH + HOME_PATH).toPath()));
			checkSid(stringBuffer, sessionId.getSid());
			body = String.valueOf(stringBuffer).getBytes();
		} else if(url.contains(".")){
			body = Files.readAllBytes(new File(STATIC_PATH + url).toPath());
		}
	}

	private void checkSid(StringBuffer stringBuffer, String sid) {
		if(sid == null) {
			int startIndex = stringBuffer.indexOf("{{#sid}}");
			int endIndex = stringBuffer.indexOf("{{/sid}}", startIndex);
			deleteStringBuffer(stringBuffer, startIndex, endIndex);

			int start = stringBuffer.indexOf("{{^sid}}");
			deleteStringBuffer(stringBuffer, start, start);
			int end = stringBuffer.indexOf("{{/sid}}", start);
			deleteStringBuffer(stringBuffer, end, end);
		} else {
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
