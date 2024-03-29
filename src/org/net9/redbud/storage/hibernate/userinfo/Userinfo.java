package org.net9.redbud.storage.hibernate.userinfo;
// Generated by MyEclipse - Hibernate Tools



/**
 * Userinfo generated by MyEclipse - Hibernate Tools
 */
public class Userinfo extends AbstractUserinfo implements java.io.Serializable {

    // Constructors

    /**
	 * 
	 */
	private static final long serialVersionUID = 4463411321729730596L;

	/** default constructor */
    public Userinfo() {
    }

	/** minimal constructor */
    public Userinfo(Integer id, String name, Short grade, String gender, String nation, Short department, String classnum, String political, String tel, String mobile, String email, String address, Short ability1, Short ability2, Short ability3, Short ability4, Short ability5, Short ability6, String specialty) {
        super(id, name, grade, gender, nation, department, classnum, political, tel, mobile, email, address, ability1, ability2, ability3, ability4, ability5, ability6, specialty);        
    }
    
    /** full constructor */
    public Userinfo(Integer id, String name, Short grade, String gender, String nation, Short department, String classnum, String political, String tel, String mobile, String email, String address, Short ability1, Short ability2, Short ability3, Short ability4, Short ability5, Short ability6, String postRec, String trainingRec, String activityRec, String specialty) {
        super(id, name, grade, gender, nation, department, classnum, political, tel, mobile, email, address, ability1, ability2, ability3, ability4, ability5, ability6, postRec, trainingRec, activityRec, specialty);        
    }
   
}
