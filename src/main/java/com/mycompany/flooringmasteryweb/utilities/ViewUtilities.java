/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmasteryweb.utilities;

import java.util.Arrays;

/**
 *
 * @author apprentice
 */
public class ViewUtilities {

    public String borderMaker(java.util.List<String> content, int width, char borderChar) {
        return borderMaker(content, width, borderChar, borderChar, borderChar, 0, 0, 0);
    }

    public String borderMaker(java.util.List<String> content, int width, char borderChar, char edgeChar, char cornerChar, int vPadding, int hPadding, int hMargin) {
        return borderMaker(content, width, borderChar, edgeChar, cornerChar, null, vPadding, hPadding, hMargin);
    }

    public String borderMaker(String content, int width, char borderChar, char edgeChar, char cornerChar, String align, int vPadding, int hPadding, int hMargin) {

        return borderMaker(splitStringsByToken(content), width, borderChar, edgeChar, cornerChar, align, vPadding, hPadding, hMargin);
    }

    public String borderMaker(java.util.List<String> content, int width, char borderChar, char edgeChar, char cornerChar, String align, int vPadding, int hPadding, int hMargin) {

//    }
//    return bordermaker(content, width, borderChar, edgeChar, cornerChar, vPadding, hPadding, hMargin);
//
//    public String bordermaker(java.util.List<String> content, int width, char borderChar) {
        int length = content.size();
        
        int widest = content.stream()
                //.mapToInt(a -> a.length())
                .mapToInt(String::length)
                .max()
                .getAsInt();

        if (width < widest) {
            width = widest + 2;
        }

        int widthOfMenu = width - 2;

        java.util.List<String> newContent = new java.util.ArrayList();

        String topLine = makeSpaces(hMargin) + cornerChar + makeChars((width - 2) + (hPadding * 2), borderChar) + cornerChar + makeSpaces(hMargin);
        String bottomLine = topLine;

        newContent.add(topLine);

        for (int i = 0; i < vPadding; i++) {
            newContent.add(makeSpaces(hMargin) + edgeChar + makeSpaces(hPadding) + makeSpaces(width - 2) + makeSpaces(hPadding) + edgeChar + makeSpaces(hMargin));
        }

        for (String string : content) {

            String formatedContent;

            if (align == null) {
                formatedContent = stringToLengthCenter(string, widthOfMenu);
            } else if (align.equalsIgnoreCase("left") || align.equalsIgnoreCase("l")) {
                formatedContent = stringToLengthLeft(string, widthOfMenu);
            } else if (align.equalsIgnoreCase("right") || align.equalsIgnoreCase("r")) {
                formatedContent = stringToLengthRight(string, widthOfMenu);
            } else {
                formatedContent = stringToLengthCenter(string, widthOfMenu);
            }

            newContent.add(makeSpaces(hMargin) + edgeChar + makeSpaces(hPadding) + formatedContent + makeSpaces(hPadding) + edgeChar + makeSpaces(hMargin));
        }

        for (int i = 0; i < vPadding; i++) {
            newContent.add(makeSpaces(hMargin) + edgeChar + makeSpaces(hPadding) + makeSpaces(width - 2) + makeSpaces(hPadding) + edgeChar + makeSpaces(hMargin));
        }

        newContent.add(bottomLine);

//        for (String string : newContent) {
//            string = borderChar + string + borderChar;
//        }
        String result = "";
        for (String string : newContent) {
            result += string + "\n";
        }

        return result;

    }

    public String stringToLengthLeft(String content, int desiredLength) {

        String result = content;
        int sizeRemaining = desiredLength - result.length();

        if (sizeRemaining > 0) {
            return result + makeSpaces(sizeRemaining);
        } else if (sizeRemaining == 0) {
            return result;
        } else {
            return null;
        }
    }

    public String stringToLengthRight(String content, int desiredLength) {

        String result = content;
        int sizeRemaining = desiredLength - result.length();

        if (sizeRemaining > 0) {
            return makeSpaces(sizeRemaining) + result;
        } else if (sizeRemaining == 0) {
            return result;
        } else {
            return null;
        }
    }

    public String stringToLengthCenter(String content, int desiredLength) {

        String result = content;
        int sizeRemaining = desiredLength - result.length();

        int leftRemaining = (sizeRemaining / 2);

        if (sizeRemaining == 0) {
            return result;
        } else if (leftRemaining > 0) {
            result = makeSpaces(leftRemaining) + result;
        } else {
            return null;
        }

        int rightRemaining = desiredLength - result.length();

        if (rightRemaining == 0) {
            return result;
        } else if (rightRemaining > 0) {
            return result + makeSpaces(rightRemaining);
        } else {
            return null;
        }
    }

    public String makeSpaces(int length) {
        char[] charArray = new char[length];
        java.util.Arrays.fill(charArray, ' ');
        String str = new String(charArray);
        return str;
    }

    public String makeChars(int length, char borderItem) {
        char[] charArray = new char[length];
        java.util.Arrays.fill(charArray, borderItem);
        String str = new String(charArray);
        return str;
    }

    public java.util.List<String> splitStringsByToken(String content) {
        return splitStringsByToken(content, "\n");
    }

    public java.util.List<String> splitStringsByToken(String content, final String TOKEN) {

        java.util.List<String> newList = new java.util.ArrayList();
        newList.addAll(Arrays.asList(content.split(TOKEN)));
        return newList;
    }
}
