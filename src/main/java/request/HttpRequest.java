package request;

import java.io.BufferedReader;
import java.io.IOException;

import org.slf4j.Logger;

public class HttpRequest {
	private static final int CONTENT_LENGTH_INDEX = 1;

	private RequestLine requestLine;
	private int contentLength;
	public HttpRequest(BufferedReader br) throws IOException {
		requestLine = new RequestLine(br.readLine());
	}

	public void debug(BufferedReader br, Logger logger) throws IOException {
		logger.debug("request line: {}", requestLine.getRequestLine());
		String requestHeader;
		while (!(requestHeader = br.readLine()).equals("")) {
			logger.debug("{}", requestHeader);
			if(requestHeader.contains("Content-Length")) {
				contentLength = Integer.parseInt(requestHeader.split(" ")[CONTENT_LENGTH_INDEX]);
			}
		}
	}

	public void processRequestBody(BufferedReader br) throws IOException {
		if(contentLength > 0) {
			RequestBody.process(br, requestLine.getUrl(),  contentLength);
		}
	}

	public RequestLine getRequestLine() {
		return requestLine;
	}
}
