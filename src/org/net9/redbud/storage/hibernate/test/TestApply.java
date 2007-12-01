package org.net9.redbud.storage.hibernate.test;

import java.util.List;

import org.net9.redbud.storage.hibernate.apply.ApplyDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestApply {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//for(int i=0;i<10;i++)
//		{
//			test save
//			Apply a = new Apply();
//			a.setApplyreason("ooooshit");
//			a.setPoscode("41120110110");
//			a.setPoscategory("411");
//			a.setStudentnum("2003011338");
//			a.setApplytime("20070515");
//			a.setApplystatus("2");
			
			//ApplyDAO ad = new ApplyDAO();
			//Session session = HibernateSessionFactory.getSession();
			//ad.save(a);
			//Transaction tx = session.beginTransaction();
			//tx.commit();
			//session.close();
	        
	        
	        
	        //test read
//			ApplyDAO ad = new ApplyDAO();
//			List result = ad.findByApplyreason("OOOOOOOOOO");
//			Iterator iter=result.iterator();
//			System.out.println("num: "+result.size());
//			Apply apply;
//			while (iter.hasNext())
//			{
//				apply=(Apply)iter.next();
//				System.out.println(apply.getStudentnum());
//				System.out.println(apply.getPoscode());
//				System.out.println(apply.getApplyreason());
//				System.out.println("--------");
				//Session session = HibernateSessionFactory.getSession();
//				apply.setApplyreason("BBB");
//				ad.attachDirty(apply);
				//Transaction tx = session.beginTransaction();
				//tx.commit();
//			}
//		}
		ApplicationContext ctx=new FileSystemXmlApplicationContext("src/servlet.cfg.xml");
		ApplyDAO applyDAO=(ApplyDAO)ctx.getBean("ApplyDAO");
		List list = applyDAO.findByStudentnum("2100000000");
		if (list.isEmpty())
			System.out.println("empty");
		else
			System.out.println("not empty");
		return;
	}
}
