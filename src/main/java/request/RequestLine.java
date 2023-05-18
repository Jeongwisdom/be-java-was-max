package request;

public class RequestLine {
	private static final int METHOD_INDEX = 0;
	private static final int REQUEST_TARGET_INDEX = 1;

	private String requestLine;
	private String httpMethod;
	private String requestTarget;

	public RequestLine(String requestLine) {
		this.requestLine = requestLine;
		String[] requestLines = requestLine.split(" ");
		httpMethod = requestLines[METHOD_INDEX];
		requestTarget = requestLines[REQUEST_TARGET_INDEX];
	}

	public String getRequestLine() {
		return requestLine;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getRequestTarget() {
		return requestTarget;
	}
}
