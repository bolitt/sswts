package org.net9.redbud.storage.hibernate.category;

import java.util.HashSet;
import java.util.Set;


/**
 * AbstractCategory generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractCategory  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String code;
     private String name;
     private String description;
     private Set organses = new HashSet(0);


    // Constructors

    /** default constructor */
    public AbstractCategory() {
    }

	/** minimal constructor */
    public AbstractCategory(Integer id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }
    
    /** full constructor */
    public AbstractCategory(Integer id, String code, String name, String description, Set organses) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.organses = organses;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public Set getOrganses() {
        return this.organses;
    }
    
    public void setOrganses(Set organses) {
        this.organses = organses;
    }
   








}