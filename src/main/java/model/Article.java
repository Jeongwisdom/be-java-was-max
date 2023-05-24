package model;

public class Article {
	private Long articleIndex;
	private String title;
	private String author;
	private String contents;
	private String writeDate;
	private long hits;

	public Article(Long articleIndex, String author, String title, String contents, String writeDate, long hits) {
		this.articleIndex = articleIndex;
		this.title = title;
		this.author = author;
		this.contents = contents;
		this.writeDate = writeDate;
		this.hits = hits;
	}

	public Long getArticleIndex() {
		return articleIndex;
	}

	public String getTitle() {
		return title;
	}

	public String getAuthor() {
		return author;
	}

	public String getContents() {
		return contents;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public long getHits() {
		return hits;
	}
}
