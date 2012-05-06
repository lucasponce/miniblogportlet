package blog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Entry implements Serializable {

	private static final long serialVersionUID = 7401227903821218593L;

	Date published;
	String author;
	ArrayList<String> tags = new ArrayList<String>();

	String title;
	String content;

	String summary;
	
	// Link
	String blogName;

	ArrayList<Comment> comments = new ArrayList<Comment>();
	
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

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getBlogName() {
		return blogName;
	}

	public void setBlogName(String blogName) {
		this.blogName = blogName;
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	
	public String getSummary() {
		if (content == null) return "";
		return cleanHtml(content);
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	private String cleanHtml(String input) {
		
		if (input.indexOf('<') == -1) return input;
		if (input.indexOf('>') == -1) return input;
		
		boolean cleaned = false;
		
		while (!cleaned) {		
			String s1 = input.substring(0, input.indexOf('<'));
			String s2 = input.substring(input.indexOf('>')+1, input.length());			
			input = s1+s2;
			
			if (input.indexOf('<') == -1) cleaned = true;
			if (input.indexOf('>') == -1) cleaned = true;
		}
		if (input.indexOf('<') != -1) {
			input = input.substring(0, input.indexOf('<'));
		}
		return input;	
		
	}
	
	public String toString() {
		return "Entry [published=" + published + ", author=" + author
				+ ", tags=" + tags + ", title=" + title + ", content="
				+ content + ", blogName=" + blogName + ", comments=" + comments.size() + "]";
	}

}
