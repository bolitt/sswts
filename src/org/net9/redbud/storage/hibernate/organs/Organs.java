package org.net9.redbud.storage.hibernate.organs;
// Generated by MyEclipse - Hibernate Tools

import java.util.Set;

import org.net9.redbud.storage.hibernate.category.Category;


/**
 * Organs generated by MyEclipse - Hibernate Tools
 */
public class Organs extends AbstractOrgans implements java.io.Serializable {

    // Constructors

    /**
	 * 
	 */
	private static final long serialVersionUID = -2052841609863591604L;

	/** default constructor */
    public Organs() {
    }

	/** minimal constructor */
    public Organs(Integer id, Category category, String code, String name, String description) {
        super(id, category, code, name, description);        
    }
    
    /** full constructor */
    public Organs(Integer id, Category category, String code, String name, String description, Set deptses) {
        super(id, category, code, name, description, deptses);        
    }
   
}
