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
    
    private int id;
    private Date date;
    private int orderid;
    private String actionPerformed;
    private Date logDate;
    private String orderName;
    private double orderTotal;
    
    @Override
    public int hashCode(){
        return id;
    }
    
    @Override
    public boolean equals(Object object){
                        
        if (object == null)
            return false;
        
        if (!(object instanceof Audit))
            return false;
        
        Audit otherAudit = (Audit)object;               
        
        return Objects.equals(id, otherAudit.getId()) &&
                DateUtilities.closeDate(date, otherAudit.getDate()) &&
                Objects.equals(orderid, otherAudit.getOrderid()) && 
                Objects.equals(actionPerformed, otherAudit.getActionPerformed()) &&
                DateUtilities.closeDate(logDate, otherAudit.getLogDate()) &&
                Objects.equals(orderName, orderName) &&
                Objects.equals(orderTotal, otherAudit.getOrderTotal());                
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
     * @return the orderid
     */
    public int getOrderid() {
        return orderid;
    }

    /**
     * @param orderid the orderid to set
     */
    public void setOrderid(int orderid) {
        this.orderid = orderid;
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

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
}
