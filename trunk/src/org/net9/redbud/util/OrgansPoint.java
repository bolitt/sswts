package org.net9.redbud.util;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.net9.redbud.storage.hibernate.organs.Organs;

public class OrgansPoint {

	private Organs organs;
	private Vector<DeptsPoint> deptsList;
	private int tempDepts;
	
	public OrgansPoint()
	{
		organs=null;
		deptsList=new Vector<DeptsPoint>();
		tempDepts=-1;
		
	}
	public Organs getOrgans()
	{
		return organs;
		
	}
	public void setOrgans(Organs or)
	{
		this.organs=or;
	}
	public  DeptsPoint getTempDepts()
	{
		return deptsList.get(tempDepts);
	}
	public DeptsPoint findByDeptsCode(String code){
		
		if (deptsList.isEmpty()) return null;
		Iterator<DeptsPoint> iter=deptsList.iterator();
		int order=0;
		DeptsPoint deptsP=iter.next();
		while ((iter.hasNext())&&(deptsP.getDepts().getCode().equals(code)==false))
		{
			deptsP=iter.next();
			order++;
		}
		if (iter.hasNext()==true)
		{
			tempDepts=order;
			return deptsP;
		}
		else
		{
			if (deptsP.getDepts().getCode().equals(code)==true)
				return deptsP;
			
		}
		return null;
	}
	
	public void addDeptsPoint(DeptsPoint deptP)
	{
		deptsList.add(deptP);
		tempDepts=deptsList.size()-1;
		
	}
	public List<DeptsPoint> getDeptsList()
	{
		return deptsList;
	}
}
