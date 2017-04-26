/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Date;

/**
 *
 * @author apprentice
 */
public class Audit {
    
    private Date date;
    private int orderid;
    private String actionPerformed;
    private Date logDate;

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
    
    
}
