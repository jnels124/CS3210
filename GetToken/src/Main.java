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

    private static  String operatorsSeperatorsRegEx = "([-+*/()<>=;\n]|:=| )";
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

//        tokenMap.put("SPACE", 26);
//        tokenMap.put("EOLN", 12);
//        tokenMap.put("IDENTIFIER", 12);
//        tokenMap.put("number", 29);
//        tokenMap.put("string", 30);
//        tokenMap.put("PERIOD", 31);
    }
    static PushbackReader in;

    public static void main (String args []) {

        if (args.length == 1) {
            String pathToFile = System.getProperty("user.dir").replace("\\", "/");
            pathToFile += "/" + args[0];
            try {
                System.setIn(new FileInputStream(new File(pathToFile)));
            } catch (IOException e) {
                System.out.println("Error setting system in to " + pathToFile);
            }
        }
        in = new PushbackReader(new InputStreamReader(System.in));
        getToken();
    }

    public static void getToken() {
        try {
            char currChar = (char)in.read();
            char nextChar;
            switch (currChar) {
                case ')':
                    System.out.println("Token: " + ')' + " Value: " +  12);
                    getToken();
                    break;
                case '(':
                    System.out.println("Token: " + '(' + " Value: " +  13);
                    getToken();
                    break;
                case '/':
                    System.out.println("Token: " + '/' + " Value: " +  14);
                    getToken();
                    break;
                case '*':
                    System.out.println("Token: " + '*' + " Value: " +  15);
                    getToken();
                    break;
                case '-':
                    System.out.println("Token: " + ')' + " Value: " +  16);
                    getToken();
                    break;
                case '+':
                    System.out.println("Token: " + '+' + " Value: " +  17);
                    getToken();
                    break;
                case '=':
                    System.out.println("Token: " + '=' + " Value: " +  21);
                    getToken();
                    break;
                case '>':
                    nextChar = (char)in.read();
                    if (nextChar == '=') {
                        System.out.println("Token: " + ">=" + " Value: " +  20);
                    }
                    else {
                        System.out.println("Token: " + ">" + " Value: " +  19);
                        in.unread(nextChar);
                    }
                    getToken();
                    break;
                case '<':
                    nextChar = (char)in.read();
                    if (nextChar == '>') {
                        System.out.println("Token: " + "<>" + " Value: " +  18);
                    }
                    else if (nextChar == '='){
                        System.out.println("Token: " + "<=" + " Value: " +  22);
                    }
                    else {
                        System.out.println("Token: " + "<" + " Value: " +  23);
                        in.unread(nextChar);
                    }
                    getToken();
                    break;
                case ';':
                    System.out.println("Token: " + ';' + " Value: " +  25);
                    getToken();
                    break;
                case ' ':
                    System.out.println("Token: " + "SPACE" + " Value: " +  26);
                    getToken();
                    break;
                case '\n':
                    System.out.println("Token: " + "EOLN" + " Value: " +  27);
                    getToken();
                    break;
                case QUOTE:
                    String strInput ="\"";
                    char c;
                    do {
                        c = (char)in.read();
                        strInput += c;
                    } while (c != QUOTE);
                    System.out.println("Token: " + "STRING--> " + strInput + " Value: " +  30);
                    getToken();
                    break;
                case '.':
                    nextChar = (char)in.read();
                    if (isNumber(nextChar)) {
                        throw new InputMismatchException("White space is required between PERIOD and NUMBER");
                    }
                    else {
                        in.unread(nextChar);
                        System.out.println("Token: " + "PERIOD" + " Value: " +  31);
                        getToken();
                        break;
                    }
                case ':':
                    nextChar = (char)in.read();
                    if (nextChar == '=') {
                        System.out.println("Token: " + ":=" + " Value: " +  24);
                        getToken();
                        break;
                    }
                    else {
                        in.unread(nextChar);
                        // Now fall through to handle as part of an identifier
                    }
                default:
                    if (isNumber(currChar)) {
                        String number = "" + currChar;
                        nextChar = (char)in.read();
                        while (nextChar >= '0' && nextChar <= '9') {
                            number += nextChar;
                            nextChar = (char)in.read();
                        }
                        if (nextChar == '.') {
                            throw new InputMismatchException("White space is required between PERIOD and NUMBER");
                        }
                        else {
                            System.out.println( "Token: " + "NUMBER--> " + number + " Value: " +  29);
                            in.unread(nextChar);
                        }
                    }
                    else {
                        String identOrReserved = "" + currChar;
                        do {
                            nextChar = (char)in.read();
                            identOrReserved += nextChar;

                        } while (nextChar != ' ' && nextChar != '\n' && !identOrReserved.matches(".*" + operatorsSeperatorsRegEx + ".*"));
                        String result = identOrReserved.split(operatorsSeperatorsRegEx)[0];

                        if (reservedTokens.containsKey(result.toUpperCase())){
                            System.out.println( "Token: " + "RESERVED--> " + result + " Value: " +  reservedTokens.get(result));
                        }
                        else {
                            System.out.println( "Token: " + "IDENTIFIER--> " + result + " Value: " + 28);
                        }
                        in.unread(identOrReserved.substring(result.length(), identOrReserved.length()).toCharArray());
                    }
                    getToken();
            }
        } catch (IOException e) {
            System.out.println("Error reading from standard in. Exiting ... " + e.getMessage());
        }

    }
    
    public static boolean isNumber (int c) {
        return c >= '0' && c <= '9';
    }
}
