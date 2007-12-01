package org.net9.redbud.storage.hibernate.training;
// Generated by MyEclipse - Hibernate Tools



/**
 * Training generated by MyEclipse - Hibernate Tools
 */
public class Training extends AbstractTraining implements java.io.Serializable {

    // Constructors

    /**
	 * 
	 */
	private static final long serialVersionUID = 4951620003063313077L;

	/** default constructor */
    public Training() {
    }

	/** minimal constructor */
    public Training(Integer id, String name, String description, String postcode, String starttime, String endtime, Integer ispublic, String posttime) {
        super(id, name, description, postcode, starttime, endtime, ispublic, posttime);        
    }
    
    /** full constructor */
    public Training(Integer id, String name, String description, String postcode, String starttime, String endtime, Integer ispublic, String deadline, String posttime) {
        super(id, name, description, postcode, starttime, endtime, ispublic, deadline, posttime);        
    }
   
}
