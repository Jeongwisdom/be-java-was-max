package response;

public enum ContentType {
	HTML("text/html;charset=utf-8", "html"),
	CSS("text/css;charset=utf-8", "css"),
	JS("application/javascript", "js"),
	ICO("image/vnd.microsoft.icon", "ico"),
	PNG("image/png", "png"),
	JPG("image/jpeg", "jpg"),
	WOFF("application/x-font-woff", "woff");

	private final String contentType;
	private final String extension;

	ContentType(String contentType, String extension) {
		this.contentType = contentType;
		this.extension = extension;
	}

	public static String findContentType(String url) {
		String[] urls = url.split("\\.");
		String contentType = urls[urls.length - 1];
		ContentType[] contentTypes = values();
		for (ContentType type : contentTypes) {
			if (type.extension.equals(contentType)) {
				return type.contentType;
			}
		}
		return null;
	}
}
