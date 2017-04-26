/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author apprentice
 */
public class StateUtilities {

    public static final Map<String, String> ABBR_TO_STATE;

    static {
        ABBR_TO_STATE = new HashMap<String, String>();
        ABBR_TO_STATE.put("AA", "Armed Forces Americas");
        ABBR_TO_STATE.put("AL", "Alabama");
        ABBR_TO_STATE.put("AK", "Alaska");
        ABBR_TO_STATE.put("AB", "Alberta");
        ABBR_TO_STATE.put("AZ", "Arizona");
        ABBR_TO_STATE.put("AR", "Arkansas");
        ABBR_TO_STATE.put("BC", "British Columbia");
        ABBR_TO_STATE.put("CA", "California");
        ABBR_TO_STATE.put("CO", "Colorado");
        ABBR_TO_STATE.put("CT", "Connecticut");
        ABBR_TO_STATE.put("DE", "Delaware");
        ABBR_TO_STATE.put("DC", "District Of Columbia");
        ABBR_TO_STATE.put("FL", "Florida");
        ABBR_TO_STATE.put("GA", "Georgia");
        ABBR_TO_STATE.put("GU", "Guam");
        ABBR_TO_STATE.put("HI", "Hawaii");
        ABBR_TO_STATE.put("ID", "Idaho");
        ABBR_TO_STATE.put("IL", "Illinois");
        ABBR_TO_STATE.put("IN", "Indiana");
        ABBR_TO_STATE.put("IA", "Iowa");
        ABBR_TO_STATE.put("KS", "Kansas");
        ABBR_TO_STATE.put("KY", "Kentucky");
        ABBR_TO_STATE.put("LA", "Louisiana");
        ABBR_TO_STATE.put("ME", "Maine");
        ABBR_TO_STATE.put("MB", "Manitoba");
        ABBR_TO_STATE.put("MD", "Maryland");
        ABBR_TO_STATE.put("MA", "Massachusetts");
        ABBR_TO_STATE.put("MI", "Michigan");
        ABBR_TO_STATE.put("MN", "Minnesota");
        ABBR_TO_STATE.put("MS", "Mississippi");
        ABBR_TO_STATE.put("MO", "Missouri");
        ABBR_TO_STATE.put("MT", "Montana");
        ABBR_TO_STATE.put("NE", "Nebraska");
        ABBR_TO_STATE.put("NV", "Nevada");
        ABBR_TO_STATE.put("NB", "New Brunswick");
        ABBR_TO_STATE.put("NH", "New Hampshire");
        ABBR_TO_STATE.put("NJ", "New Jersey");
        ABBR_TO_STATE.put("NM", "New Mexico");
        ABBR_TO_STATE.put("NY", "New York");
        ABBR_TO_STATE.put("NF", "Newfoundland");
        ABBR_TO_STATE.put("NC", "North Carolina");
        ABBR_TO_STATE.put("ND", "North Dakota");
        ABBR_TO_STATE.put("NT", "Northwest Territories");
        ABBR_TO_STATE.put("NS", "Nova Scotia");
        ABBR_TO_STATE.put("NU", "Nunavut");
        ABBR_TO_STATE.put("OH", "Ohio");
        ABBR_TO_STATE.put("OK", "Oklahoma");
        ABBR_TO_STATE.put("ON", "Ontario");
        ABBR_TO_STATE.put("OR", "Oregon");
        ABBR_TO_STATE.put("PA", "Pennsylvania");
        ABBR_TO_STATE.put("PE", "Prince Edward Island");
        ABBR_TO_STATE.put("PR", "Puerto Rico");
        ABBR_TO_STATE.put("QC", "Quebec");
        ABBR_TO_STATE.put("RI", "Rhode Island");
        ABBR_TO_STATE.put("SK", "Saskatchewan");
        ABBR_TO_STATE.put("SC", "South Carolina");
        ABBR_TO_STATE.put("SD", "South Dakota");
        ABBR_TO_STATE.put("TN", "Tennessee");
        ABBR_TO_STATE.put("TX", "Texas");
        ABBR_TO_STATE.put("UT", "Utah");
        ABBR_TO_STATE.put("VT", "Vermont");
        ABBR_TO_STATE.put("VI", "Virgin Islands");
        ABBR_TO_STATE.put("VA", "Virginia");
        ABBR_TO_STATE.put("WA", "Washington");
        ABBR_TO_STATE.put("WV", "West Virginia");
        ABBR_TO_STATE.put("WI", "Wisconsin");
        ABBR_TO_STATE.put("WY", "Wyoming");
        ABBR_TO_STATE.put("YT", "Yukon Territory");
    }

    public static final Map<String, String> STATE_TO_ABBR;

    static {
        STATE_TO_ABBR = new HashMap<String, String>();
        STATE_TO_ABBR.put("Alabama", "AL");
        STATE_TO_ABBR.put("Alaska", "AK");
        STATE_TO_ABBR.put("Alberta", "AB");
        STATE_TO_ABBR.put("American Samoa", "AS");
        STATE_TO_ABBR.put("Arizona", "AZ");
        STATE_TO_ABBR.put("Arkansas", "AR");
        STATE_TO_ABBR.put("Armed Forces (AE)", "AE");
        STATE_TO_ABBR.put("Armed Forces Americas", "AA");
        STATE_TO_ABBR.put("Armed Forces Pacific", "AP");
        STATE_TO_ABBR.put("British Columbia", "BC");
        STATE_TO_ABBR.put("California", "CA");
        STATE_TO_ABBR.put("Colorado", "CO");
        STATE_TO_ABBR.put("Connecticut", "CT");
        STATE_TO_ABBR.put("Delaware", "DE");
        STATE_TO_ABBR.put("District Of Columbia", "DC");
        STATE_TO_ABBR.put("Florida", "FL");
        STATE_TO_ABBR.put("Georgia", "GA");
        STATE_TO_ABBR.put("Guam", "GU");
        STATE_TO_ABBR.put("Hawaii", "HI");
        STATE_TO_ABBR.put("Idaho", "ID");
        STATE_TO_ABBR.put("Illinois", "IL");
        STATE_TO_ABBR.put("Indiana", "IN");
        STATE_TO_ABBR.put("Iowa", "IA");
        STATE_TO_ABBR.put("Kansas", "KS");
        STATE_TO_ABBR.put("Kentucky", "KY");
        STATE_TO_ABBR.put("Louisiana", "LA");
        STATE_TO_ABBR.put("Maine", "ME");
        STATE_TO_ABBR.put("Manitoba", "MB");
        STATE_TO_ABBR.put("Maryland", "MD");
        STATE_TO_ABBR.put("Massachusetts", "MA");
        STATE_TO_ABBR.put("Michigan", "MI");
        STATE_TO_ABBR.put("Minnesota", "MN");
        STATE_TO_ABBR.put("Mississippi", "MS");
        STATE_TO_ABBR.put("Missouri", "MO");
        STATE_TO_ABBR.put("Montana", "MT");
        STATE_TO_ABBR.put("Nebraska", "NE");
        STATE_TO_ABBR.put("Nevada", "NV");
        STATE_TO_ABBR.put("New Brunswick", "NB");
        STATE_TO_ABBR.put("New Hampshire", "NH");
        STATE_TO_ABBR.put("New Jersey", "NJ");
        STATE_TO_ABBR.put("New Mexico", "NM");
        STATE_TO_ABBR.put("New York", "NY");
        STATE_TO_ABBR.put("Newfoundland", "NF");
        STATE_TO_ABBR.put("North Carolina", "NC");
        STATE_TO_ABBR.put("North Dakota", "ND");
        STATE_TO_ABBR.put("Northwest Territories", "NT");
        STATE_TO_ABBR.put("Nova Scotia", "NS");
        STATE_TO_ABBR.put("Nunavut", "NU");
        STATE_TO_ABBR.put("Ohio", "OH");
        STATE_TO_ABBR.put("Oklahoma", "OK");
        STATE_TO_ABBR.put("Ontario", "ON");
        STATE_TO_ABBR.put("Oregon", "OR");
        STATE_TO_ABBR.put("Pennsylvania", "PA");
        STATE_TO_ABBR.put("Prince Edward Island", "PE");
        STATE_TO_ABBR.put("Puerto Rico", "PR");
        STATE_TO_ABBR.put("Quebec", "QC");
        STATE_TO_ABBR.put("Rhode Island", "RI");
        STATE_TO_ABBR.put("Saskatchewan", "SK");
        STATE_TO_ABBR.put("South Carolina", "SC");
        STATE_TO_ABBR.put("South Dakota", "SD");
        STATE_TO_ABBR.put("Tennessee", "TN");
        STATE_TO_ABBR.put("Texas", "TX");
        STATE_TO_ABBR.put("Utah", "UT");
        STATE_TO_ABBR.put("Vermont", "VT");
        STATE_TO_ABBR.put("Virgin Islands", "VI");
        STATE_TO_ABBR.put("Virginia", "VA");
        STATE_TO_ABBR.put("Washington", "WA");
        STATE_TO_ABBR.put("West Virginia", "WV");
        STATE_TO_ABBR.put("Wisconsin", "WI");
        STATE_TO_ABBR.put("Wyoming", "WY");
        STATE_TO_ABBR.put("Yukon Territory", "YT");
    }

    public static String stateFromAbbr(String abbr) {
        if (abbr == null) {
            return null;
        }
        return ABBR_TO_STATE.get(abbr.toUpperCase().substring(0, 2));
    }

    public static String abbrFromState(String stateName) {

        String resultStateName = bestGuessStateName(stateName);

        return STATE_TO_ABBR.get(resultStateName);
    }

    public static String bestGuessStateName(String stateName) {

        if (stateName == null) {
            return null;
        }

        if (stateName.length() < 1) {
            return null;
        }

        if (validStateAbbr(stateName)) {
            return stateFromAbbr(stateName);
        }
        
        List<String> resultList = guessStateName(stateName);

        if (resultList.isEmpty()) {
            return null;
        } else {
            return resultList.get(0);
        }

    }

    public static List<String> guessStateName(String stateName) {

        if (stateName == null) {
            return new ArrayList();
        }

        List<String> partialMatches = STATE_TO_ABBR.keySet().stream()
                .filter(a -> a != null)
                .filter(a -> a.equalsIgnoreCase(stateName))
                .collect(Collectors.toList());

        if (partialMatches.isEmpty()) {

            partialMatches = STATE_TO_ABBR.keySet().stream()
                    .filter(a -> a != null)
                    .filter(a -> a.toLowerCase().startsWith(stateName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (partialMatches.isEmpty()) {

            partialMatches = STATE_TO_ABBR.keySet().stream()
                    .filter(a -> a != null)
                    .filter(a -> a.toLowerCase().contains(stateName.toLowerCase()))
                    .collect(Collectors.toList());
        }

        return partialMatches;
    }

    public static boolean validStateAbbr(String stateAbbr) {
        if (stateAbbr == null) {
            return false;
        }
        return ABBR_TO_STATE.keySet().contains(stateAbbr.trim().toUpperCase());
    }

    public static boolean validStateInput(String input) {
        if (input == null) {
            return false;
        }
        return ((bestGuessStateName(input) != null && guessStateName(input).size() < 6) || validStateAbbr(input));
    }

    /**
     * Returns Map< String StateName , String Abbreviation >
     *
     * @return
     */
    public static Map<String, String> unifiedMap() {
        Map<String, String> unityMap = new HashMap();

        unityMap.putAll(STATE_TO_ABBR);

        //unityMap.putAll(unityMap);
        //ABBR_TO_STATE.keySet().stream().forEach(a -> unityMap.put(a.));
        ABBR_TO_STATE.keySet().stream().forEach(a -> unityMap.put(ABBR_TO_STATE.get(a), a));

        return unityMap;
    }

    public static Map<String, String> unifiedMapRev() {

        Map<String, String> unityMapRev = new HashMap();
        Map<String, String> unityMap = unifiedMap();
        Map<String, String> tempUnityMap = unityMap;

        tempUnityMap.keySet().stream()
                .forEach(a -> unityMapRev.put(unityMap.get(a), a));

        return unityMapRev;
    }

    public static Map<String, String> stringMapReverser(Map<String, String> stringMap) {

        Map<String, String> unityMapRev = new HashMap();
        Map<String, String> unityMap = stringMap;
        Map<String, String> tempUnityMap = unityMap;

        tempUnityMap.keySet().stream()
                .forEach(a -> unityMapRev.put(unityMap.get(a), a));

        return unityMapRev;
    }

}
