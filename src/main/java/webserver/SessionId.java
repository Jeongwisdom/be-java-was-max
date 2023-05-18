package webserver;

import java.util.UUID;

public class SessionId {
	private String sid;

	public void SetSid() {
		this.sid = UUID.randomUUID().toString();
	}

	public String getSid() {
		return sid;
	}
}
