package org.net9.redbud.storage.hibernate.test;

import java.util.Iterator;

import org.net9.redbud.storage.hibernate.internalinfo.Internalinfo;
import org.net9.redbud.storage.hibernate.internaltask.Internaltask;
import org.net9.redbud.storage.hibernate.internaltask.InternaltaskDAO;
import org.net9.redbud.storage.hibernate.internaltasklogin.Internaltasklogin;
import org.net9.redbud.storage.hibernate.internaltasklogin.InternaltaskloginDAO;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestInternalTask {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"src/servlet.cfg.xml");
		LoginDAO loginDAO = (LoginDAO) ctx.getBean("LoginDAO");
		InternaltaskDAO internaltaskDAO= (InternaltaskDAO) ctx
				.getBean("InternaltaskDAO");
		InternaltaskloginDAO internaltaskloginDAO= (InternaltaskloginDAO) ctx
				.getBean("InternaltaskloginDAO");
		// /read///

		Login l = loginDAO.findById(2);
		Iterator It = l.getInternaltasklogins().iterator();
		while (It.hasNext()) {
			Internaltasklogin temp = (Internaltasklogin) It.next();
			Internaltask internaltask = temp.getInternaltask();
			int id = internaltask.getId();
			System.out.println(id);
			
			Internaltask inter = internaltaskDAO.findById(id);
			
			System.out.println(inter.getOwenername());
			System.out.println(inter.getOwnerposname());
			System.out.println(inter.getTasktitle());
			System.out.println(inter.getTaskcontent());
			System.out.println(inter.getPublishtime());
		}

//		// /save///
//		 Login l = loginDAO.findById(2);
//				
//		 Internaltask t = new Internaltask();
//				
//		 t.setOwenername(l.getUserinfo().getName());
//		 t.setOwnerid(l.getId());
//		 t.setOwnerposname("some postname");
//		 t.setPublishtime(System.currentTimeMillis());
//		 t.setTaskcontent("taskcontern:饿虐火车扩大会议我看到是");
//		 t.setTasktitle("tasktitle:爱的擂茶可进口看看v");
//		 internaltaskDAO.save(t);
			
		
//		 l = loginDAO.findById(2);
////		
////				
//		 Internaltasklogin pd = new Internaltasklogin();
////				
//		 pd.setLogin(l);
//		 pd.setInternaltask(t);
//		 pd.setStatus("false");
////				
//		 internaltaskloginDAO.save(pd);
		 //////////////////

	}

}
