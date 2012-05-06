package blog;

import java.io.Serializable;
import java.util.ArrayList;

public class Blog implements Serializable {

	private static final long serialVersionUID = 2615566221688566372L;

	String name;
	String description;

	String bio;
	ArrayList<String> tags;
	
	ArrayList<String> users = new ArrayList<String>();

	String css;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}
	
	public ArrayList<String> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
	
	public String getCss() {
		return css;
	}

	public void setCss(String css) {
		this.css = css;
	}

	public String toString() {
		return "Blog [name=" + name + ", description=" + description + ", bio="
				+ bio + ", tags=" + tags + ", users=" + users + ", css=" + css
				+ "]";
	}

}
