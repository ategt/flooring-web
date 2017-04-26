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
        private long differenctTime;
        private int id;

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
     * @return the differenctTime
     */
    public long getDifferenctTime() {
        return differenctTime;
    }

    /**
     * @param differenctTime the differenctTime to set
     */
    public void setDifferenctTime(long differenctTime) {
        this.differenctTime = differenctTime;
    }

    /**
     * @return the id
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    @Override
    public void setId(int id) {
        this.id = id;
    }
        }
