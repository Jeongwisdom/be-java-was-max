package response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResponseBody {
	private static final String HTML_PATH = "src/main/resources/templates";
	private static final String STATIC_PATH = "src/main/resources/static";
	static final String HOME_PATH = "/index.html";
	static final String LOGIN_FAIL_PATH = "/user/login_failed.html";

	private byte[] body;

	public ResponseBody(String url) throws IOException {
		setBody(url);
	}

	public void setBody(String url) throws IOException {
		if(url.contains("html")) {
			body = Files.readAllBytes(new File(HTML_PATH + url).toPath());
		} else if(url.equals("/")){
			body = Files.readAllBytes(new File(HTML_PATH + HOME_PATH).toPath());
		} else if(url.contains(".")){
			body = Files.readAllBytes(new File(STATIC_PATH + url).toPath());
		}
	}

	public byte[] getBody() {
		return body;
	}
}
