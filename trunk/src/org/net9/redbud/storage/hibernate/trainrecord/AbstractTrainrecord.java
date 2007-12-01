package org.net9.redbud.storage.hibernate.trainrecord;



/**
 * AbstractTrainrecord generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractTrainrecord  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String trainid;
     private String loginid;
     private String score;
     private Integer isattend;


    // Constructors

    /** default constructor */
    public AbstractTrainrecord() {
    }

	/** minimal constructor */
    public AbstractTrainrecord(Integer id, String trainid, String loginid, Integer isattend) {
        this.id = id;
        this.trainid = trainid;
        this.loginid = loginid;
        this.isattend = isattend;
    }
    
    /** full constructor */
    public AbstractTrainrecord(Integer id, String trainid, String loginid, String score, Integer isattend) {
        this.id = id;
        this.trainid = trainid;
        this.loginid = loginid;
        this.score = score;
        this.isattend = isattend;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrainid() {
        return this.trainid;
    }
    
    public void setTrainid(String trainid) {
        this.trainid = trainid;
    }

    public String getLoginid() {
        return this.loginid;
    }
    
    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getScore() {
        return this.score;
    }
    
    public void setScore(String score) {
        this.score = score;
    }

    public Integer getIsattend() {
        return this.isattend;
    }
    
    public void setIsattend(Integer isattend) {
        this.isattend = isattend;
    }
   








}