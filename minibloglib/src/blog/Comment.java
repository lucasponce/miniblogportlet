package blog;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Comment implements Serializable {
	
	private static final long serialVersionUID = 4404828262483790668L;

	UUID uuid = UUID.randomUUID();	
	Date published;
	String author;
	String content;
	
	// Link
	String entryTitle;

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getEntryTitle() {
		return entryTitle;
	}

	public void setEntryTitle(String entryTitle) {
		this.entryTitle = entryTitle;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public String toString() {
		return "Comment [uuid=" + uuid + ", published=" + published
				+ ", author=" + author + ", content=" + content
				+ ", entryTitle=" + entryTitle + "]";
	}
	
}
