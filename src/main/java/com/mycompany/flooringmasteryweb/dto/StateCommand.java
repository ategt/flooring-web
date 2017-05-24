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
public class StateCommand {
    private String stateName;
    private String stateAbbreviation;
    private double stateTax;

    @Override
    public int hashCode(){
        return getStateAbbreviation().hashCode();
    }
    
    @Override
    public boolean equals(Object object){
                        
        if (object == null)
            return false;
        
        if (!(object instanceof StateCommand))
            return false;
        
        StateCommand otherStateCommand = (StateCommand)object;  
        
        return Objects.equals(stateName, otherStateCommand.getStateName()) &&
                Objects.equals(stateAbbreviation, otherStateCommand.getStateAbbreviation()) && 
                Objects.equals(stateTax, otherStateCommand.getStateTax());                
    }
    
    /**
     * @return the stateName
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * @param stateName the stateName to set
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * @return the stateAbbreviation
     */
    public String getStateAbbreviation() {
        return stateAbbreviation;
    }

    /**
     * @param stateAbbreviation the stateAbbreviation to set
     */
    public void setStateAbbreviation(String stateAbbreviation) {
        this.stateAbbreviation = stateAbbreviation;
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
    
}
