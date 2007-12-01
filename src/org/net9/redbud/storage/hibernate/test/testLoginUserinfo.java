package org.net9.redbud.storage.hibernate.test;

import java.util.List;

import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
public class testLoginUserinfo {
		/**
		 * @param args
		 */
		public static void main(String[] args) {
			ApplicationContext ctx=new FileSystemXmlApplicationContext("src/servlet.cfg.xml");
			LoginDAO loginDAO=(LoginDAO)ctx.getBean("LoginDAO");
			List list=loginDAO.findByStudentnum("2003011300");
			if (list.isEmpty())
				System.out.println("empty");
			else
				System.out.println("not empty");
			Login login = new Login();
	        login.setStudentnum("2003014444");
	        login.setPassword("99999");
	        
	        Userinfo userinfo = new Userinfo();
	        
	        userinfo.setAbility1((short)1);
	        userinfo.setAbility2((short)2);
	        userinfo.setAbility3((short)3);
	        userinfo.setAbility4((short)4);
	        userinfo.setAbility5((short)5);
	        userinfo.setAbility6((short)6);
	        userinfo.setActivityRec("activityasdfasdf");
	        userinfo.setAddress("zijinggongyuasdfasdf");
	        userinfo.setClassnum("计33");
	        userinfo.setDepartment((short)33);
	        userinfo.setEmail("someemail@xxxXXXX.com");
	        userinfo.setGender("1");
	        userinfo.setGrade(Short.parseShort("2003"));
	        userinfo.setMobile("1380013812");
	        userinfo.setName("qian");
	        userinfo.setNation("han");
	        userinfo.setPolitical("dangyuan");
	        userinfo.setPostRec("postrec");
	        userinfo.setTel("51532600");
	        userinfo.setTrainingRec("train!");
	        userinfo.setSpecialty("吃喝嫖赌");

	        userinfo.setLogin(login);
	        login.setUserinfo(userinfo);
	                
	        //userinfodao.save(userinfo);
	        loginDAO.save(login);
	       
//	        List list=shitD.findByStudentnum("2003011300");
//	        Login ll=(Login)list.get(0);
//	        //Userinfo login2=(Userinfo)(((Login)shitD.findByStudentnum("20030114").get(0)).getUserinfo());
//	        Userinfo login2=ll.getUserinfo();
//	       
//	        
//	        System.out.println(login2.getMobile());
		}

}
