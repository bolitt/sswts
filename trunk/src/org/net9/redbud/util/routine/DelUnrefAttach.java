package org.net9.redbud.util.routine;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.net9.redbud.storage.hibernate.training.Training;
import org.net9.redbud.storage.hibernate.training.TrainingDAO;
import org.net9.redbud.web.controller.RedbudBaseController;
public class DelUnrefAttach extends RedbudBaseController{

	static String uploadPath="C:\\Program Files\\Apache Software Foundation\\Tomcat 5.5\\webapps\\sswts1\\";
	private static TrainingDAO trainingDAO;
	
	public static TrainingDAO getTrainingDAO() {
		return trainingDAO;
	}
	public static void setTrainingDAO(TrainingDAO trainingDAO) {
		DelUnrefAttach.trainingDAO = trainingDAO;
	}
	/**
	 * 定期删除没用的附件
	 * written by sillywolf@9#,Sep,13,2007
	 * @param path:附件存放的目录
	 */
	public static void delUnrefAttach(String path)
	{
		
		File pat=new File(path+"upload");
		try
		{
			//String m=pat.getAbsolutePath();
			//TrainingDAO trainingDAO=new TrainingDAO();
			
			if (pat.isDirectory())
			{
				HashSet diskFile=new HashSet();
				HashSet dataBaseFile=new HashSet();
				
				String[] fileDi=pat.list();
				for (int i=0;i<fileDi.length;i++)
				{
					diskFile.add("upload"+"/"+fileDi[i]);
				}
				Class.forName("com.mysql.jdbc.Driver");
				
				String url="jdbc:mysql://sswts.net9.org:3306/sswts";
				Connection conn=DriverManager.getConnection(url, "root", "mysql");
				conn.setAutoCommit(false);
				String statString="select * from training";
				Statement state=conn.createStatement();
				ResultSet res=state.executeQuery(statString);
				String fileList="";
				if (res.next()==true)
				{
					fileList=res.getString("filelist");
					while (res.next()==true)
					{
						String[] files=fileList.split(":",-1);
						for (int i=0;i<files.length;i++)
						{
							dataBaseFile.add(files[i]);
						}
						fileList=res.getString("filelist");
					}
					String[] files=fileList.split(":");
					for (int i=0;i<files.length;i++)
					{
						dataBaseFile.add(files[i]);
					}
					
				}
				
				diskFile.removeAll(dataBaseFile);
				Iterator<String> iter=diskFile.iterator();
				if (iter.hasNext())
				{
					String fileName=iter.next();
					while (iter.hasNext())
					{
						File toDel=new File(path+"\\"+fileName);
						if (toDel.delete()==false)
							throw new RuntimeException(path+"\\"+fileName+"deleted failed");
						fileName=iter.next();
					}
					File toDel=new File(path+"\\"+fileName);
					if (toDel.delete()==false)
						throw new RuntimeException(path+"\\"+fileName+"deleted failed");
				}
				
			}
			else
			{
				throw new FileNotFoundException(path+" is not a directory");
			}
			
		}catch (Exception e)
		{
			e.printStackTrace();
			
		}
	}
	public static void main(String args[])
	{
		delUnrefAttach(uploadPath);
	}
}
