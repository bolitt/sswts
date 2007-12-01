package org.net9.redbud.storage.hibernate.test;

import java.util.Iterator;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.publicinfo.Publicinfo;
import org.net9.redbud.storage.hibernate.publicinfo.PublicinfoDAO;
import org.net9.redbud.storage.hibernate.publicinfologin.Publicinfologin;
import org.net9.redbud.storage.hibernate.publicinfologin.PublicinfologinDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestPublicInfo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx = new FileSystemXmlApplicationContext(
				"src/servlet.cfg.xml");
		LoginDAO loginDAO = (LoginDAO) ctx.getBean("LoginDAO");
		PublicinfoDAO publicinfoDAO = (PublicinfoDAO) ctx
				.getBean("PublicinfoDAO");
		PublicinfologinDAO publicinfologinDAO = (PublicinfologinDAO) ctx
				.getBean("PublicinfologinDAO");
		// /read///

		Login l = loginDAO.findById(1);
		Iterator It = l.getPublicinfologins().iterator();
		while (It.hasNext()) {
			Publicinfologin temp = (Publicinfologin) It.next();
			Publicinfo publicinfo = temp.getPublicinfo();
			int id = publicinfo.getId();
			System.out.println(id);
			
			Publicinfo pub = publicinfoDAO.findById(id);
			
			System.out.println(pub.getInfotitle());
			System.out.println(pub.getInfocontent());
		}

		// /save///
		// l = loginDAO.findById(1);
		//		
		// Publicinfo p = new Publicinfo();
		//		
		// p.setApprovestatus(1);
		// p.setApprovetime(System.currentTimeMillis());
		// p.setInfocontent("快让开看而据威尔");
		// p.setInfotitle("呕吐偶igkrie ！");
		// p.setOwenercatename("婆婆iytrrfh ");
		// p.setOwnerid("1");
		// p.setPublishtime(System.currentTimeMillis());
		//		
		// publicinfoDAO.save(p);
		//	
		//
		// l = loginDAO.findById(1);
		//
		// p = publicinfoDAO.findById(1);
		//		
		// Publicinfologin pd = new Publicinfologin();
		//		
		// pd.setLogin(l);
		// pd.setPublicinfo(p);
		// pd.setStatus("true");
		//		
		// publicinfologinDAO.save(pd);
		// //////////////////

	}

}
