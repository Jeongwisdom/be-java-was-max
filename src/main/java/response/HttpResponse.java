package response;

import static response.ResponseBody.*;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;

import db.Database;
import request.RequestLine;
import webserver.SessionId;

public class HttpResponse {
	private String requestTarget;
	private String contentType;
	private Status status;
	private ResponseBody responseBody;
	private Long index;

	public HttpResponse(RequestLine requestLine, Status status, SessionId sessionId) {
		this.requestTarget = requestLine.getRequestTarget();
		this.contentType = ContentType.findContentType(requestTarget);
		this.status = status;
		this.responseBody = new ResponseBody();
	}

	public void response(OutputStream out, Logger logger, SessionId sessionId) throws IOException {
		DataOutputStream dos = new DataOutputStream(out);
		responseHeader(dos, logger, sessionId);
		if(requestTarget.contains("show.html")) {
			String[] urls = requestTarget.split("/");
			index = Long.valueOf(urls[2]);
			requestTarget = "/" + urls[1] + "/" + urls[3];
		}
		this.responseBody.setBody(requestTarget, sessionId, index);
		responseBody(dos, logger);
	}

	private void responseHeader(DataOutputStream dos, Logger logger, SessionId sessionId) {
		try {
			dos.writeBytes("HTTP/1.1 " + status.getStatusCode() + status.getStatusText() + "\r\n");
			redirectLocation(dos);
			dos.writeBytes("Content-Type: " + contentType + "\r\n");
			putContentLength(dos);
			setCookie(dos, requestTarget, sessionId);
			deleteCookie(dos, requestTarget, sessionId);
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void deleteCookie(DataOutputStream dos, String requestTarget, SessionId sessionId) throws IOException {
		if(requestTarget.contains("/logout")) {
			dos.writeBytes("Set-Cookie: sid=" + sessionId.getSid() + "; Max-Age=0; Path=/");
			Database.deleteSid(sessionId.getSid());
			sessionId.logout();
		}
	}

	private void setCookie(DataOutputStream dos, String requestTarget, SessionId sessionId) throws IOException {
		if(requestTarget.equals("/user/login") && status.getStatusCode() == 302) {
			dos.writeBytes("Set-Cookie: sid=" + sessionId.getSid() + "; Path=/");
		}
	}

	private void putContentLength(DataOutputStream dos) throws IOException {
		if(responseBody.getBody() != null) {
			int lengthOfBodyContent = responseBody.getBody().length;
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
		}
	}

	private void redirectLocation(DataOutputStream dos) throws IOException {
		if(status.getStatusCode() == 302) {
			if(status.equals(Status.FOUND_LOGIN_FAIL)) {
				dos.writeBytes("Location: " + LOGIN_FAIL_PATH + "\r\n");
			} else {
				dos.writeBytes("Location: " + HOME_PATH + "\r\n");
			}
		}
	}

	private void responseBody(DataOutputStream dos, Logger logger) {
		try {
			dos.write(responseBody.getBody(), 0, responseBody.getBody().length);
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
