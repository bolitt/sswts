package org.net9.redbud.storage.hibernate.posrecord;

import org.net9.redbud.storage.hibernate.login.Login;
// Generated by MyEclipse - Hibernate Tools



/**
 * Posrecord generated by MyEclipse - Hibernate Tools
 */
public class Posrecord extends AbstractPosrecord implements java.io.Serializable {

    // Constructors

    /**
	 * 
	 */
	private static final long serialVersionUID = -5304610951948550188L;

	/** default constructor */
    public Posrecord() {
    }

	/** minimal constructor */
    public Posrecord(Integer id, Login login, String posname, String stunum) {
        super(id, login, posname, stunum);        
    }
    
    /** full constructor */
    public Posrecord(Integer id, Login login, String posname, String ability1, String ability2, String ability3, String ability4, String ability5, String ability6, String remark, String starttime, String endtime, String poscode, String stunum) {
        super(id, login, posname, ability1, ability2, ability3, ability4, ability5, ability6, remark, starttime, endtime, poscode, stunum);        
    }
   
}
