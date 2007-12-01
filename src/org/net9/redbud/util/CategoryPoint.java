package org.net9.redbud.util;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.net9.redbud.storage.hibernate.category.Category;

public class CategoryPoint {

	private Category category;
	private Vector<OrgansPoint> organsList;
	private int organsOrder;
	
	public CategoryPoint()
	{
		category=null;
		//Vector<OrgansPoint> vector=new Vector<OrgansPoint>();
		
		organsList=new Vector<OrgansPoint>();
		organsOrder=-1;
		
	}
	public boolean isEmpty()
	{
		if (category==null)
			return true;
		else return false;
	}
	public void newCategory(Category cate)
	{
		category=cate;
	}
	public Category getCategory()
	{
		return category;
	}
	public OrgansPoint findByOrgansCode(String code)
	{
		if (organsList.isEmpty()==true) return null;
		Iterator<OrgansPoint> iter=organsList.iterator();
		int order=0;
		
		OrgansPoint organP=iter.next();
		while ((iter.hasNext())&&(organP.getOrgans().getCode().equals(code)==false))
		{
			organP=iter.next();
			order++;
		}
		if (iter.hasNext()==true)
		{
			organsOrder=order;
			return organP;
		}
		else
		{
			if (organP.getOrgans().getCode().equals(code)==true)
				return organP;
			
		}
		return null;
	}
	public OrgansPoint getTempOrgans()
	{
		return organsList.get(organsOrder);
	}
	public void addOrgansP(OrgansPoint orP)
	{
		
		
		organsList.add(orP);
		organsOrder=organsList.size()-1;
	}
	public List<OrgansPoint> getOrgansList()
	{
		return this.organsList;
	}
	
}
