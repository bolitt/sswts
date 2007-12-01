package org.net9.redbud.storage.hibernate.apply;



/**
 * AbstractApply generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractApply  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String studentnum;
     private String poscode;
     private String applyreason;
     private String poscategory;
     private String applytime;
     private String applystatus;


    // Constructors

    /** default constructor */
    public AbstractApply() {
    }

    
    /** full constructor */
    public AbstractApply(Integer id, String studentnum, String poscode, String applyreason, String poscategory, String applytime, String applystatus) {
        this.id = id;
        this.studentnum = studentnum;
        this.poscode = poscode;
        this.applyreason = applyreason;
        this.poscategory = poscategory;
        this.applytime = applytime;
        this.applystatus = applystatus;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentnum() {
        return this.studentnum;
    }
    
    public void setStudentnum(String studentnum) {
        this.studentnum = studentnum;
    }

    public String getPoscode() {
        return this.poscode;
    }
    
    public void setPoscode(String poscode) {
        this.poscode = poscode;
    }

    public String getApplyreason() {
        return this.applyreason;
    }
    
    public void setApplyreason(String applyreason) {
        this.applyreason = applyreason;
    }

    public String getPoscategory() {
        return this.poscategory;
    }
    
    public void setPoscategory(String poscategory) {
        this.poscategory = poscategory;
    }

    public String getApplytime() {
        return this.applytime;
    }
    
    public void setApplytime(String applytime) {
        this.applytime = applytime;
    }

    public String getApplystatus() {
        return this.applystatus;
    }
    
    public void setApplystatus(String applystatus) {
        this.applystatus = applystatus;
    }
   








}