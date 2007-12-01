package org.net9.redbud.storage.hibernate.tokencontainer;



/**
 * AbstractTokencontainer generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractTokencontainer  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String password;
     private Integer objectid;


    // Constructors

    /** default constructor */
    public AbstractTokencontainer() {
    }

    
    /** full constructor */
    public AbstractTokencontainer(Integer id, String password, Integer objectid) {
        this.id = id;
        this.password = password;
        this.objectid = objectid;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    

    public Integer getObjectid() {
        return this.objectid;
    }
    
    public void setObjectid(Integer objectid) {
        this.objectid = objectid;
    }
   








}