package org.net9.redbud.storage.hibernate.posrecord;

import org.net9.redbud.storage.hibernate.login.Login;



/**
 * AbstractPosrecord generated by MyEclipse - Hibernate Tools
 */

public abstract class AbstractPosrecord  implements java.io.Serializable {


    // Fields    

     private Integer id;
     private Login login;
     private String posname;
     private String ability1;
     private String ability2;
     private String ability3;
     private String ability4;
     private String ability5;
     private String ability6;
     private String remark;
     private String starttime;
     private String endtime;
     private String poscode;
     private String stunum;


    // Constructors

    /** default constructor */
    public AbstractPosrecord() {
    }

	/** minimal constructor */
    public AbstractPosrecord(Integer id, Login login, String posname, String stunum) {
        this.id = id;
        this.login = login;
        this.posname = posname;
        this.stunum = stunum;
    }
    
    /** full constructor */
    public AbstractPosrecord(Integer id, Login login, String posname, String ability1, String ability2, String ability3, String ability4, String ability5, String ability6, String remark, String starttime, String endtime, String poscode, String stunum) {
        this.id = id;
        this.login = login;
        this.posname = posname;
        this.ability1 = ability1;
        this.ability2 = ability2;
        this.ability3 = ability3;
        this.ability4 = ability4;
        this.ability5 = ability5;
        this.ability6 = ability6;
        this.remark = remark;
        this.starttime = starttime;
        this.endtime = endtime;
        this.poscode = poscode;
        this.stunum = stunum;
    }

   
    // Property accessors

    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Login getLogin() {
        return this.login;
    }
    
    public void setLogin(Login login) {
        this.login = login;
    }

    public String getPosname() {
        return this.posname;
    }
    
    public void setPosname(String posname) {
        this.posname = posname;
    }

    public String getAbility1() {
        return this.ability1;
    }
    
    public void setAbility1(String ability1) {
        this.ability1 = ability1;
    }

    public String getAbility2() {
        return this.ability2;
    }
    
    public void setAbility2(String ability2) {
        this.ability2 = ability2;
    }

    public String getAbility3() {
        return this.ability3;
    }
    
    public void setAbility3(String ability3) {
        this.ability3 = ability3;
    }

    public String getAbility4() {
        return this.ability4;
    }
    
    public void setAbility4(String ability4) {
        this.ability4 = ability4;
    }

    public String getAbility5() {
        return this.ability5;
    }
    
    public void setAbility5(String ability5) {
        this.ability5 = ability5;
    }

    public String getAbility6() {
        return this.ability6;
    }
    
    public void setAbility6(String ability6) {
        this.ability6 = ability6;
    }

    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStarttime() {
        return this.starttime;
    }
    
    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return this.endtime;
    }
    
    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getPoscode() {
        return this.poscode;
    }
    
    public void setPoscode(String poscode) {
        this.poscode = poscode;
    }

    public String getStunum() {
        return this.stunum;
    }
    
    public void setStunum(String stunum) {
        this.stunum = stunum;
    }
   








}