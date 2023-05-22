package request;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;

import response.Status;
import webserver.SessionId;

public class HttpRequest {
	private static final int CONTENT_LENGTH_INDEX = 1;
	private static final int SID_NUMBER = 1;

	private final RequestLine requestLine;
	private int contentLength;
	private Status status;

	public HttpRequest(String requestLine) {
		this.requestLine = new RequestLine(requestLine);
	}

	public void debug(BufferedReader br, SessionId sessionId, Logger logger) throws IOException {
		logger.debug("request line: {}", requestLine.getRequestLine());
		String requestHeader;
		while (!(requestHeader = br.readLine()).equals("")) {
			logger.debug("{}", requestHeader);
			if(requestHeader.contains("Content-Length")) {
				contentLength = Integer.parseInt(requestHeader.split(" ")[CONTENT_LENGTH_INDEX]);
			}
			if(requestHeader.contains("sid=")) {
				String sid = requestHeader.split("sid=")[SID_NUMBER];
				sessionId.setSid(sid);
			}
		}
		logger.debug("-------------------------------");
	}

	public void processRequestBody(BufferedReader br, SessionId sessionId) throws IOException {
		status = Status.OK;
		if(contentLength > 0) {
			status = RequestBody.process(br, requestLine.getRequestTarget(), sessionId, contentLength);
		}
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}

	public Status getStatus() {
		return status;
	}
}
