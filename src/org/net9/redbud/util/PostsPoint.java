package org.net9.redbud.util;

import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.posts.Posts;

public class PostsPoint {

	private Posts post;
	private Login login;
	
	public PostsPoint()
	{
		post=null;
		login=null;
	}
	public Posts getPost() {
		return post;
	}
	public void setPost(Posts post) {
		this.post = post;
	}
	public Login getLogin() {
		return login;
	}
	public void setLogin(Login login) {
		this.login = login;
	}

	
	
	
	
}
