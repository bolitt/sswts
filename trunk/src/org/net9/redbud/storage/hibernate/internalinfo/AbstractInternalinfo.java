package org.net9.redbud.storage.hibernate.internalinfo;

import java.util.HashSet;
import java.util.Set;


/**
 * AbstractInternalinfo generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractInternalinfo  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String infotitle;
     private String infocontent;
     private Integer ownerid;
     private String ownername;
     private String ownerpos;
     private Long publishtime;
     private Set internalinfologins = new HashSet(0);


    // Constructors

    /** default constructor */
    public AbstractInternalinfo() {
    }

	/** minimal constructor */
    public AbstractInternalinfo(Integer id, String infotitle, String infocontent, Integer ownerid, String ownername, String ownerpos, Long publishtime) {
        this.id = id;
        this.infotitle = infotitle;
        this.infocontent = infocontent;
        this.ownerid = ownerid;
        this.ownername = ownername;
        this.ownerpos = ownerpos;
        this.publishtime = publishtime;
    }
    
    /** full constructor */
    public AbstractInternalinfo(Integer id, String infotitle, String infocontent, Integer ownerid, String ownername, String ownerpos, Long publishtime, Set internalinfologins) {
        this.id = id;
        this.infotitle = infotitle;
        this.infocontent = infocontent;
        this.ownerid = ownerid;
        this.ownername = ownername;
        this.ownerpos = ownerpos;
        this.publishtime = publishtime;
        this.internalinfologins = internalinfologins;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getInfotitle() {
        return this.infotitle;
    }
    
    public void setInfotitle(String infotitle) {
        this.infotitle = infotitle;
    }

    public String getInfocontent() {
        return this.infocontent;
    }
    
    public void setInfocontent(String infocontent) {
        this.infocontent = infocontent;
    }

    public Integer getOwnerid() {
        return this.ownerid;
    }
    
    public void setOwnerid(Integer ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnername() {
        return this.ownername;
    }
    
    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getOwnerpos() {
        return this.ownerpos;
    }
    
    public void setOwnerpos(String ownerpos) {
        this.ownerpos = ownerpos;
    }

    public Long getPublishtime() {
        return this.publishtime;
    }
    
    public void setPublishtime(Long publishtime) {
        this.publishtime = publishtime;
    }

    public Set getInternalinfologins() {
        return this.internalinfologins;
    }
    
    public void setInternalinfologins(Set internalinfologins) {
        this.internalinfologins = internalinfologins;
    }
   








}