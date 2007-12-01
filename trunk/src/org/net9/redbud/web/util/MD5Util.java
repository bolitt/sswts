package org.net9.redbud.web.util;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.net9.redbud.storage.hibernate.HibernateSessionFactory;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;

//by Ryan 2007.5.23

public class MD5Util {
	public final static String MD5(String s) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			return null;
		}
	}
	//for test
	public static void main(String[] args) {
		// MD5_Test aa = new MD5_Test();

		//System.out.print(MD5Util.MD5("qian"));
		LoginDAO ld = new LoginDAO();
		Login l = new Login();
		List result = ld.findByExample(l);
		Iterator iter=result.iterator();
		System.out.println("num: "+result.size());
		Login sample;
		String studentnum;
		while (iter.hasNext())
		{
			sample=(Login)iter.next();
			studentnum = sample.getStudentnum();
			System.out.println(sample.getStudentnum());
			String code = MD5Util.MD5(studentnum);
			sample.setPassword(code);
			Session session = HibernateSessionFactory.getSession();
			Transaction tx = session.beginTransaction();
			session.update(sample);
			tx.commit();
			session.close();
			System.out.println("--------");
		}
		System.out.println("done!!");
		return;
	}
}
