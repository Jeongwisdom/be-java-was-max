package response;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.slf4j.Logger;

import request.RequestLine;

public class HttpResponse {
	private String contentType;
	private String statusCode;
	private ResponseBody responseBody;

	public HttpResponse(RequestLine requestLine) throws IOException {
		this.contentType = ContentType.findContentType(requestLine.getUrl());
		this.statusCode = StatusCode.getStatusCode(requestLine.getHttpMethod());
		this.responseBody = new ResponseBody(requestLine.getUrl());
	}

	public void response(OutputStream out, Logger logger) {
		DataOutputStream dos = new DataOutputStream(out);
		if(responseBody.getBody() != null) {
			responseHeader(dos, logger, responseBody.getBody().length);
			responseBody(dos, logger, responseBody.getBody());
		} else {
			responseHeader(dos, logger, 0);
		}
		logger.debug("----------------------------");
	}

	private void responseHeader(DataOutputStream dos, Logger logger, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 " + statusCode);
			dos.writeBytes("Content-Type: " + contentType + "\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, Logger logger, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}
}
