package org.net9.redbud.storage.hibernate.publicinfo;

import java.util.HashSet;
import java.util.Set;


/**
 * AbstractPublicinfo generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractPublicinfo  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private String infotitle;
     private String infocontent;
     private Integer ownerid;
     private String ownercatename;
     private Long publishtime;
     private Long approvetime;
     private Integer approvestatus;
     private Set publicinfologins = new HashSet(0);


    // Constructors

    /** default constructor */
    public AbstractPublicinfo() {
    }

	/** minimal constructor */
    public AbstractPublicinfo(Integer id, String infotitle, String infocontent, Integer ownerid, String ownercatename, Long publishtime, Long approvetime, Integer approvestatus) {
        this.id = id;
        this.infotitle = infotitle;
        this.infocontent = infocontent;
        this.ownerid = ownerid;
        this.ownercatename = ownercatename;
        this.publishtime = publishtime;
        this.approvetime = approvetime;
        this.approvestatus = approvestatus;
    }
    
    /** full constructor */
    public AbstractPublicinfo(Integer id, String infotitle, String infocontent, Integer ownerid, String ownercatename, Long publishtime, Long approvetime, Integer approvestatus, Set publicinfologins) {
        this.id = id;
        this.infotitle = infotitle;
        this.infocontent = infocontent;
        this.ownerid = ownerid;
        this.ownercatename = ownercatename;
        this.publishtime = publishtime;
        this.approvetime = approvetime;
        this.approvestatus = approvestatus;
        this.publicinfologins = publicinfologins;
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

    public String getOwnercatename() {
        return this.ownercatename;
    }
    
    public void setOwnercatename(String ownercatename) {
        this.ownercatename = ownercatename;
    }

    public Long getPublishtime() {
        return this.publishtime;
    }
    
    public void setPublishtime(Long publishtime) {
        this.publishtime = publishtime;
    }

    public Long getApprovetime() {
        return this.approvetime;
    }
    
    public void setApprovetime(Long approvetime) {
        this.approvetime = approvetime;
    }

    public Integer getApprovestatus() {
        return this.approvestatus;
    }
    
    public void setApprovestatus(Integer approvestatus) {
        this.approvestatus = approvestatus;
    }

    public Set getPublicinfologins() {
        return this.publicinfologins;
    }
    
    public void setPublicinfologins(Set publicinfologins) {
        this.publicinfologins = publicinfologins;
    }
   








}