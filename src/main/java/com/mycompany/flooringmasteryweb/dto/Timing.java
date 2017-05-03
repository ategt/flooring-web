/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

import java.util.Objects;

/**
 *
 * @author apprentice
 */
public class Timing implements Identifiable {

    private long startTime;
    private long stopTime;
    private long differenceTime;
    private int modifiers;
    private String invokingClassName;
    private String invokingMethodName;
    private int id;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Timing) {
            Timing otherTiming = (Timing) object;

            return getId() == otherTiming.getId()
                    && getStartTime() == otherTiming.getStartTime()
                    && getStopTime() == otherTiming.getStopTime()
                    && getDifferenceTime() == otherTiming.getDifferenceTime()
                    && getModifiers() == otherTiming.getModifiers()
                    && Objects.equals(getInvokingClassName(), otherTiming.getInvokingClassName())
                    && Objects.equals(getInvokingMethodName(), otherTiming.getInvokingMethodName());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return getId();
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the stopTime
     */
    public long getStopTime() {
        return stopTime;
    }

    /**
     * @param stopTime the stopTime to set
     */
    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * @return the differenceTime
     */
    public long getDifferenceTime() {
        return differenceTime;
    }

    /**
     * @param differenceTime the differenceTime to set
     */
    public void setDifferenceTime(long differenceTime) {
        this.differenceTime = differenceTime;
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

    /**
     * @return the modifiers
     */
    public int getModifiers() {
        return modifiers;
    }

    /**
     * @param modifiers the modifiers to set
     */
    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    /**
     * @return the invokingClassName
     */
    public String getInvokingClassName() {
        return invokingClassName;
    }

    /**
     * @param invokingClassName the invokingClassName to set
     */
    public void setInvokingClassName(String invokingClassName) {
        this.invokingClassName = invokingClassName;
    }

    /**
     * @return the invokingMethodName
     */
    public String getInvokingMethodName() {
        return invokingMethodName;
    }

    /**
     * @param invokingMethodName the invokingMethodName to set
     */
    public void setInvokingMethodName(String invokingMethodName) {
        this.invokingMethodName = invokingMethodName;
    }
}
