/*
 * Levels Beyond CONFIDENTIAL
 *
 * Copyright 2003 - 2015 Levels Beyond Incorporated
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Levels Beyond Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Levels Beyond Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is unlawful and strictly forbidden unless prior written permission is obtained
 * from Levels Beyond Incorporated.
 */

import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Set;

/**
 * Created by jessenelson on 3/13/15.
 */
public class Main {
    private static final int QUOTE = '"';
    private static final int PLUS = '+';
    private static final Map<String, Integer> reservedTokens = new HashMap<String, Integer>();
    private static final Map<String, Integer> operatorSeperatorTokens = new HashMap<String, Integer>();

    private static  String operatorsSeperatorsRegEx = "([-+*/()<>=;\n.]|:=|[0-9]| )";
    // Inititalize token map
    static {
        // Reserved words
        reservedTokens.put("IF", 1);
        reservedTokens.put("THEN", 2);
        reservedTokens.put("ELSE", 3);
        reservedTokens.put("FI", 4);
        reservedTokens.put("LOOP", 5);
        reservedTokens.put("BREAK", 6);
        reservedTokens.put ("READ", 7);
        reservedTokens.put("REPEAT", 8);
        reservedTokens.put("PRINT", 9);
        reservedTokens.put("AND", 10);
        reservedTokens.put("OR", 11);

        // Operators and Separators
        operatorSeperatorTokens.put(")", 12);
        operatorSeperatorTokens.put("(", 13);
        operatorSeperatorTokens.put("/", 14);
        operatorSeperatorTokens.put("*", 15);
        operatorSeperatorTokens.put("-", 16);
        operatorSeperatorTokens.put("+", 17);
        operatorSeperatorTokens.put("<>", 18);
        operatorSeperatorTokens.put(">", 19);
        operatorSeperatorTokens.put(">=", 20);
        operatorSeperatorTokens.put("=", 21);
        operatorSeperatorTokens.put("<=", 22);
        operatorSeperatorTokens.put("<", 23);
        operatorSeperatorTokens.put(":=", 24);
        operatorSeperatorTokens.put(";", 25);
        operatorSeperatorTokens.put(" ", 26);
        operatorSeperatorTokens.put("\n", 27);
        operatorSeperatorTokens.put(".", 26);
    }

    private static PushbackReader in;

    public static void main (String args []) throws Exception{

        if (args.length == 1) {
            // Handle Windows file system
            String pathToFile = System.getProperty("user.dir").replace("\\", "/");
            pathToFile += "/" + args[0];
            try {
                System.setIn(new FileInputStream(new File(pathToFile)));
            } catch (IOException e) {
                System.out.println("Error setting system in to " + pathToFile);
            }
        }
        in = new PushbackReader(new InputStreamReader(System.in));
        char currChar;
        // '\uFFFF' is returned when end of file is read
        while ((currChar = readIn()) != '\uFFFF') {
            getToken(currChar);
        }
    }

    public static boolean isOperatorSeperator (String key) {

        return operatorSeperatorTokens.containsKey(key);
    }

    private static String transformKey (String key) {
        if (key.equals("\n")) {
            return "EOLN";
        }
        else if (key.equals(" ")) {
            return "SPACE";
        }

        else {
            return key;
        }
    }

    public static void getToken(char currChar) {
        char nextChar = readIn();
        String key;
        if (currChar == '.') {
            if (isNumber(nextChar)) {
                print(handleNumber(readIn(), "" + currChar + nextChar, true, true));
            }
            else {
                unreadIn(nextChar);
                print("Token: " + '.' + " Value: " + 31);
            }
        }
        else if (isNumber(currChar)) {
            print(handleNumber(nextChar, "" + currChar, false, true));
        }
        else if (currChar == '"') {
            print(handleString(nextChar, "" + currChar));
        }
        else if (isOperatorSeperator(key = "" + currChar + nextChar)) {
            print("Token: " + key + " Value: " + operatorSeperatorTokens.get(key));
        }
        else if (isOperatorSeperator(key = "" + currChar)) {
            unreadIn(nextChar);
            print("Token: " + transformKey(key) + " Value: " + operatorSeperatorTokens.get(key));
        }
        else {
            print(handleReserveredOrIdentifier("" + currChar + nextChar));
        }
    }

    private static String handleReserveredOrIdentifier(String string) {
        if (string.matches(".*" + operatorsSeperatorsRegEx + ".*")) {
            //Split the string so that the first item in the array will contain the reserved word or identifier
            String result = string.split(operatorsSeperatorsRegEx)[0];
            //Put separating character(s) back on input stack
            unreadIn(string.substring(result.length(), string.length()).toCharArray()); // Really only pushing back one character but reliant on regex match so ...
            if (reservedTokens.containsKey(result.toUpperCase())) {
                return "Token: " + "RESERVED--> " + result + " Value: " +  reservedTokens.get(result);
            }
            else {
                return "Token: " + "IDENTIFIER--> " + result + " Value: " + 28;
            }
        }

        return handleReserveredOrIdentifier(string + readIn());
    }

    private static String handleNumber (char currChar, String number, boolean isFloatingPoint, boolean validNumber) throws InputMismatchException {
        if (currChar == '.') {
            if (isFloatingPoint) {
                throw new InputMismatchException("White space is required between PERIOD and NUMBER");
            }
            else {
                // Set validNumber to false as a digit must follow the decimal to still be a number
                return handleNumber(readIn(), number + currChar, true, false);
            }
        }
        if (!(isNumber(currChar))) {
            if (!validNumber) {
                throw new InputMismatchException("White space is required between PERIOD and NUMBER. No digits following the decimal place");
            }
            unreadIn(currChar);
            return "Token: " + "NUMBER--> " + number + " Value: " +  29;
        }
        // Set validNumber to true as we have encountered at least one digit after the decimal place 
        return handleNumber(readIn(), number + currChar, isFloatingPoint, true);
    }

    private static String handleString (char currChar, String string) {
        if (currChar == '"') {
            return "Token: " + "STRING--> " + string + currChar + " Value: " +  30;
        }
        return handleString(readIn(), string + currChar);
    }

    private static void print (String msg) {
        System.out.println(msg);
    }

    private static char readIn() {
        char input = '\uFFFF';
        try {
            input=(char)in.read();
        } catch (IOException e) {
            print("Error reading from standard in " + e.getMessage());
            System.exit(-1);
        }
        return input;
    }

    private static void unreadIn(char [] buff) {
        try {
            in.unread(buff);
        } catch (IOException e) {
            print("Error pushing characters back to standard in " + e.getMessage());
            System.exit(-1);
        }
    }

    private static void unreadIn(char c) {
        try {
            in.unread(c);
        } catch (IOException e) {
            print("Error pushing characters back to standard in " + e.getMessage());
            System.exit(-1);
        }
    }

    private static boolean isNumber (int c) {
        return c >= '0' && c <= '9';
    }
}
