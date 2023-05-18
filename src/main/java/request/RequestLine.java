package request;

public class RequestLine {
	private static final int METHOD_INDEX = 0;
	private static final int URL_INDEX = 1;

	private String requestLine;
	private String httpMethod;
	private String url;
	public RequestLine(String requestLine) {
		this.requestLine = requestLine;
		String[] requestLines = requestLine.split(" ");
		httpMethod = requestLines[METHOD_INDEX];
		url = requestLines[URL_INDEX];
	}

	public String getRequestLine() {
		return requestLine;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public String getUrl() {
		return url;
	}
}
