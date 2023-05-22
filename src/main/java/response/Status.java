package response;

import static response.ResponseBody.*;

public enum Status {
	OK(200, " OK"),
	FOUND_SUCCESS(302, " FOUND"),
	FOUND_LOGIN_FAIL(302, " FOUND"),
	BAD_REQUEST(400, " BAD REQUEST");

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
