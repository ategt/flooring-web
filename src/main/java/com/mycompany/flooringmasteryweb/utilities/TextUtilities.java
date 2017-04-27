/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

/**
 *
 * @author apprentice
 */
public class TextUtilities {

    
    /** Many thanks to whomever came up with this on stackoverflow.com.
     * 
     * @param givenString
     * @return 
     */
    public static String toTitleCase(String givenString) {
        if (givenString == null)
            return null;

        if (givenString.trim().length() < 1)
            return givenString;
        
        String[] arr = givenString.toLowerCase().split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }
}
