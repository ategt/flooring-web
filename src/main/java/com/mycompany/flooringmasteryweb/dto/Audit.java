/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import com.mycompany.flooringmasteryweb.utilities.DateUtilities;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author apprentice
 */
public class Audit {
    
    private Integer id;
    private Date date;
    private Integer orderid;
    private String actionPerformed;
    private Date logDate;
    private String orderName;
    private double orderTotal;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    @Override
    public int hashCode(){
        return id == null ? 0 : id;
    }
    
    @Override
    public boolean equals(Object object){
                        
        if (object == null)
            return false;
        
        if (!(object instanceof Audit))
            return false;
        
        Audit otherAudit = (Audit)object;               
        
        return Objects.equals(getId(), otherAudit.getId()) &&
                DateUtilities.closeDate(getDate(), otherAudit.getDate()) &&
                Objects.equals(getOrderid(), otherAudit.getOrderid()) && 
                Objects.equals(getActionPerformed(), otherAudit.getActionPerformed()) &&
                DateUtilities.closeDate(getLogDate(), otherAudit.getLogDate()) &&
                Objects.equals(getOrderName(), otherAudit.getOrderName()) &&
                Objects.equals(getOrderTotal(), otherAudit.getOrderTotal());                
    }
    
    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * @return the actionPerformed
     */
    public String getActionPerformed() {
        return actionPerformed;
    }

    /**
     * @param actionPerformed the actionPerformed to set
     */
    public void setActionPerformed(String actionPerformed) {
        this.actionPerformed = actionPerformed;
    }

    /**
     * @return the orderName
     */
    public String getOrderName() {
        return orderName;
    }

    /**
     * @param orderName the orderName to set
     */
    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    /**
     * @return the orderTotal
     */
    public double getOrderTotal() {
        return orderTotal;
    }

    /**
     * @param orderTotal the orderTotal to set
     */
    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }


}
