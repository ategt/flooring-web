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
public class State {

    private String state;
    private double stateTax;
    private Integer id;

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object object) {

        if (object == null) {
            return false;
        }

        if (!(object instanceof State)) {
            return false;
        }

        State otherState = (State) object;

        return Objects.equals(getId(), otherState.getId())
                && Objects.equals(getState(), otherState.getState())
                && Objects.equals(getStateName(), otherState.getStateName())
                && Objects.equals(getStateTax(), otherState.getStateTax());
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        if (state != null) {
            state = state.toUpperCase();
        }
        this.state = state;
    }

    /**
     * @return the state
     */
    public String getStateName() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setStateName(String state) {
        this.state = state;
    }

    /**
     * @return the stateTax
     */
    public double getStateTax() {
        return stateTax;
    }

    /**
     * @param stateTax the stateTax to set
     */
    public void setStateTax(double stateTax) {
        this.stateTax = stateTax;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

}
