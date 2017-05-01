/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.dto;

/**
 *
 * @author apprentice
 */
public class Timing implements Identifiable {

    private long startTime;
    private long stopTime;
    private long differenceTime;
    private int id;

    @Override
    public boolean equals(Object object) {
        if (object instanceof Timing) {
            Timing otherTiming = (Timing) object;

            return id == otherTiming.getId()
                    && startTime == otherTiming.getStartTime()
                    && stopTime == otherTiming.getStopTime()
                    && differenceTime == otherTiming.getDifferenceTime();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return id;
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
}
