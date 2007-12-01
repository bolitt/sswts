package org.net9.redbud.storage.hibernate.test;

import java.util.Iterator;

import org.net9.redbud.storage.hibernate.internalinfo.Internalinfo;
import org.net9.redbud.storage.hibernate.internalinfo.InternalinfoDAO;
import org.net9.redbud.storage.hibernate.internalinfologin.Internalinfologin;
import org.net9.redbud.storage.hibernate.internalinfologin.InternalinfologinDAO;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestInternalInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"src/servlet.cfg.xml");
		LoginDAO loginDAO = (LoginDAO) ctx.getBean("LoginDAO");
		InternalinfoDAO internalinfoDAO = (InternalinfoDAO) ctx
				.getBean("InternalinfoDAO");
		InternalinfologinDAO internalinfologinDAO = (InternalinfologinDAO) ctx
				.getBean("InternalinfologinDAO");
		// /read///

		Login l = loginDAO.findById(2);
		Iterator It = l.getInternalinfologins().iterator();
		//Iterator It = internalinfologinDAO.findByLoginAndStatus("2", "false").iterator();
		while (It.hasNext()) {
			Internalinfologin temp = (Internalinfologin) It.next();
			Internalinfo internalinfo = temp.getInternalinfo();
			int id = internalinfo.getId();
			System.out.println(id);
			
			Internalinfo inter = internalinfoDAO.findById(id);
			
			System.out.println(inter.getInfotitle());
			System.out.println(inter.getInfocontent());
		}

		// /save///
//		 Login l = loginDAO.findById(2);
//				
//		 Internalinfo p = new Internalinfo();
//				
//		 p.setInfocontent("internal content ssss");
//		 p.setInfotitle("internal title发的发的发 ");
//		 p.setOwnerid(l.getId());
//		 p.setOwnername(l.getUserinfo().getName());
//		 p.setOwnerpos("test");
//		 p.setPublishtime(System.currentTimeMillis());
//				
//		 internalinfoDAO.save(p);
//			
//		
//		 l = loginDAO.findById(2);
////		
////				
//		 Internalinfologin pd = new Internalinfologin();
////				
//		 pd.setLogin(l);
//		 pd.setInternalinfo(p);
//		 pd.setStatus("true");
////				
//		 internalinfologinDAO.save(pd);
//		 //////////////////

	}

}
