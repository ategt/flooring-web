/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author apprentice
 */
public class DateUtilities {

    public static boolean closeDate(Date date1, Date date2) {

        if (date1 == null && date2 == null) {
            return true;
        }

        if (date1 == null || date2 == null) {
            return false;
        }

        long mills1 = date1.getTime();
        long mills2 = date2.getTime();

        return Math.abs(mills1 - mills2) < 2000;
    }

//
//    public String getPrettyDateFromStringUsingBrute(String possibleDate) throws DateUnSimplifiable, ParseException {
//
//        String tempDateString = possibleDate
//                    .replaceAll("\\.", "");
//
////                .replaceAll("/", "-")
////                .replaceAll(",", "-")
////                .replaceAll("\\.", "-")
////                .replaceAll(" ", "-");
//
//        String formatPredicted = DateUtilities.determineDateFormat(tempDateString);
//        SimpleDateFormat dateFormat = new SimpleDateFormat(formatPredicted);
//
//        SimpleDateFormat betterDateFormat = new SimpleDateFormat("MM-dd-yyyy");
//
//        Date givenDate = dateFormat.parse(tempDateString);
//        String resultDateString = betterDateFormat.format(givenDate);
//
//        return resultDateString;
//
//    }
//
////    @Deprecated
////    private void oldWay(){
////        
////        String possibleDate = "";
////        int month = 0;
////        int day = 0;
////        int year = 0;
////
////        String[] monthsOfTheYear = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
////
////        String tempDateString = possibleDate.replaceAll(Pattern.quote("\\"), "-")
////                .replaceAll("/", "-")
////                .replaceAll(",", "-")
////                .replaceAll("\\.", "-")
////                .replaceAll(" ", "-");
////
////        String[] tempDateStringArray = new String[3];
////
////        // Check to see if there are any letters in the string.
////        //if (tempDateString.matches("[a-zA-Z]")) {
////        //if (tempDateString.matches("[a-zA-Z]")) {
////        if (tempDateString.matches("[a-zA-Z]+\\.?")) {
////            for (int x = 0; x < monthsOfTheYear.length; x++) {
////                if (monthsOfTheYear[x].toLowerCase().contains(tempDateString.toLowerCase())) {
////                    month = x + 1;
////                    tempDateStringArray = new String[2];
////                    tempDateString = tempDateString.replaceAll("[a-zA-Z]+\\.?", "");
////                    break;
////                }
////            }
////        }
////
////        // It might be dd-mm-yyyy.
////        if (tempDateString.contains("-")) {
////            tempDateStringArray = tempDateString.split("-");
////
////            // It might be ddmmyy or ddmmyyyy.    
////        } else if (tempDateString.matches("[0-9]")) {
////            if (tempDateString.length() == 6) {
////
////                tempDateStringArray[0] = tempDateString.substring(0, 2);
////                tempDateStringArray[1] = tempDateString.substring(2, 4);
////                tempDateStringArray[2] = tempDateString.substring(4, 6);
////
////            } else if (tempDateString.length() == 8) {
////
////                tempDateStringArray[0] = tempDateString.substring(0, 2);
////                tempDateStringArray[1] = tempDateString.substring(2, 4);
////                tempDateStringArray[2] = tempDateString.substring(4, 8);
////
////            } else if (tempDateString.length() == 4) {
////
////                tempDateStringArray[0] = tempDateString.substring(0, 1);
////                tempDateStringArray[1] = tempDateString.substring(1, 2);
////                tempDateStringArray[2] = tempDateString.substring(2, 4);
////
////            } else {
////                throw new DateUnSimplifiable(" The Utility was unable to Convert The Input Supplied: " + possibleDate + " into Simple Date Format.");
////            }
////
////            //tempDateStringArray = tempDateString.split();
////            //} else 
////        }
////
////        if (tempDateStringArray.length == 3) {
////            for (String dateStringPart : tempDateStringArray) {
////
////                //if (dateStringPart.matches("[^a-zA-Z]")) {
////                if (true) {
////                    int tempNumber = Integer.parseInt(dateStringPart);
////                    if (tempNumber > 31) //Assume This is the Year.
////                    {
////                        year = tempNumber;
////                    } else if (tempNumber > 12) {
////                        //Assume This is the day.
////                        day = tempNumber;
////                    } else if (tempNumber > 0 && month == 0) {
////                        month = tempNumber;
////                    } else if (dateStringPart.equals(tempDateStringArray[2])) {
////                        year = tempNumber;
////                    }
////
////                    //|| dateStringPart.equals(tempDateStringArray[3]) 
////                }
////
////            }
////
////        } else {
////            throw new DateUnSimplifiable(" The Utility was unable to Determine The Delimiter Used For The Input Supplied: " + possibleDate + "");
////        }
////
////        String returnString = Integer.toString(month) + "-" + Integer.toString(day) + "-" + Integer.toString(year);
////
////        return returnString;
////
////    }
    /**
     * I got this from StackOverflow, it seems to be better thought out than
     * mine.
     *
     * @return
     */
    private static final java.util.Map<String, String> loadMap() {
        //private static final Map<String, String> DATE_FORMAT_REGEXPS = new HashMap<String, String>() {{
        final java.util.Map<String, String> DATE_FORMAT_REGEXPS = new java.util.HashMap<>();

        DATE_FORMAT_REGEXPS.put("^\\d{8}$", "yyyyMMdd");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}$", "dd-MM-yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}$", "yyyy-MM-dd");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}$", "MM/dd/yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}$", "yyyy/MM/dd");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}$", "dd MMM yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]+\\.?{3}\\s\\d{4}$", "dd MMM yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}$", "dd MMMM yyyy");
        DATE_FORMAT_REGEXPS.put("^\\s[a-z]{4,}\\d{1,2},\\s\\d{4}$", "MMMM dd, yyyy");
        DATE_FORMAT_REGEXPS.put("^\\d{12}$", "yyyyMMddHHmm");
        DATE_FORMAT_REGEXPS.put("^\\d{8}\\s\\d{4}$", "yyyyMMdd HHmm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}$", "dd-MM-yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy-MM-dd HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}$", "MM/dd/yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}$", "yyyy/MM/dd HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMM yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}$", "dd MMMM yyyy HH:mm");
        DATE_FORMAT_REGEXPS.put("^\\d{14}$", "yyyyMMddHHmmss");
        DATE_FORMAT_REGEXPS.put("^\\d{8}\\s\\d{6}$", "yyyyMMdd HHmmss");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}-\\d{1,2}-\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd-MM-yyyy HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy-MM-dd HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}/\\d{1,2}/\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "MM/dd/yyyy HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$", "yyyy/MM/dd HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{3}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMM yyyy HH:mm:ss");
        DATE_FORMAT_REGEXPS.put("^\\d{1,2}\\s[a-z]{4,}\\s\\d{4}\\s\\d{1,2}:\\d{2}:\\d{2}$", "dd MMMM yyyy HH:mm:ss");

        return DATE_FORMAT_REGEXPS;

    }

    /**
     * Determine SimpleDateFormat pattern matching with the given date string.
     * Returns null if format is unknown. You can simply extend DateUtil with
     * more formats if needed.
     *
     * @param dateString The date string to determine the SimpleDateFormat
     * pattern for.
     * @return The matching SimpleDateFormat pattern, or null if format is
     * unknown.
     * @see SimpleDateFormat
     */
    public static String determineDateFormat(String dateString) {
        final java.util.Map<String, String> DATE_FORMAT_REGEXPS = DateUtilities.loadMap();

        for (String regexp : DATE_FORMAT_REGEXPS.keySet()) {
            if (dateString.toLowerCase().matches(regexp)) {
                return DATE_FORMAT_REGEXPS.get(regexp);
            }
        }
        return null; // Unknown format.
    }

}
