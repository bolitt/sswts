package org.net9.redbud.storage.hibernate.trainrecord;
// Generated by MyEclipse - Hibernate Tools



/**
 * Trainrecord generated by MyEclipse - Hibernate Tools
 */
public class Trainrecord extends AbstractTrainrecord implements java.io.Serializable {

    // Constructors

    /**
	 * 
	 */
	private static final long serialVersionUID = -2738248536611781991L;

	/** default constructor */
    public Trainrecord() {
    }

	/** minimal constructor */
    public Trainrecord(Integer id, String trainid, String loginid, Integer isattend) {
        super(id, trainid, loginid, isattend);        
    }
    
    /** full constructor */
    public Trainrecord(Integer id, String trainid, String loginid, String score, Integer isattend) {
        super(id, trainid, loginid, score, isattend);        
    }
   
}