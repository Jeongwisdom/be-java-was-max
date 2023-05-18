package response;

import static response.ResponseBody.*;

public enum Status {
	OK(200, " OK"),
	FOUND(302, " FOUND"),
	BAD_REQUEST(400, " BAD REQUEST"),
	UNAUTHORIZED(401, " UNAUTHORIZED");

	private int statusCode;
	private String statusText;

	Status(int statusCode, String statusText) {
		this.statusCode = statusCode;
		this.statusText = statusText;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getStatusText() {
		return statusText;
	}
}
