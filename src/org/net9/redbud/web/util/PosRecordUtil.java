package org.net9.redbud.web.util;

import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.posrecord.Posrecord;
import org.net9.redbud.storage.hibernate.posrecord.PosrecordDAO;

public class PosRecordUtil {

	public static void addPosRecord(Login login, String posName, String ability1,String ability2,
						String ability3, String ability4,String ability5,String ability6,
						String remark,String startTime,String endTime,String posCode,String stuNum)
	{
		PosrecordDAO posrecordDAO=new PosrecordDAO();
		Posrecord posre=new Posrecord();
		posre.setLogin(login);
		posre.setPosname(posName);
		posre.setAbility1(ability1);
		posre.setAbility2(ability2);
		posre.setAbility3(ability3);
		posre.setAbility4(ability4);
		posre.setAbility5(ability5);
		posre.setAbility6(ability6);
		posre.setRemark(remark);
		posre.setStarttime(startTime);
		posre.setEndtime(endTime);
		posre.setPoscode(posCode);
		posre.setStunum(stuNum);
		posrecordDAO.save(posre);
	}
			
}
