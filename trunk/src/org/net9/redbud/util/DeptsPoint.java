package org.net9.redbud.util;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.net9.redbud.storage.hibernate.depts.Depts;

public class DeptsPoint {

	private Depts depts;
	private Vector<PostsPoint> postList;
	private int tempPosts;

	public DeptsPoint()
	{
		depts=null;
		postList=new Vector<PostsPoint>();
		tempPosts=-1;
	}
	public Depts getDepts(){
		return depts;
	}
	
	public void setDepts(Depts depts)
	{
		this.depts=depts;
	}
	
	public List<PostsPoint> getPostsList()
	{
		return postList;
	}
	public PostsPoint getTempPostsP()
	{
		return postList.get(tempPosts);
	}
	public void addPostsPoint(PostsPoint posP)
	{
		postList.add(posP);
		tempPosts=postList.size()-1;
	}
	public PostsPoint findByPostsCode(String code)
	{
		if (postList.isEmpty()) return null;
		Iterator<PostsPoint> iter=postList.iterator();
		int order=0;
		PostsPoint postsP=iter.next();
		while ((iter.hasNext())&&(postsP.getPost().getCode().equals(code)==false))
		{
			postsP=iter.next();
			order++;
		}
		if (iter.hasNext()==true)
		{
			tempPosts=order;
			return postsP;
		}
		else
		{
			if (postsP.getPost().getCode().equals(code)==true)
				return postsP;
			
		}
		return null;
	}
}
