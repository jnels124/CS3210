import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by jessenelson on 4/17/15.
 */
public class TokenUtil {
	private static final int QUOTE = '"';
	private static final int PLUS = '+';
	private static String currentToken;
	private static String currentTokenValue;
	private static final Map<String, Integer> reservedTokens = new HashMap<String, Integer>();
	private static final Map<String, Integer> operatorSeperatorTokens = new HashMap<String, Integer>();
    public static final Map<String, Integer> tokenIDMap = new HashMap<String, Integer>();
	private static PushbackReader in;

	private static final String operatorsSeperatorsRegEx = "([-+*/()<>=;\n.\"]|:=|[0-9]| )";
	// Inititalize token map
	static {
		// Reserved words
		reservedTokens.put("IF", 1);
		reservedTokens.put("THEN", 2);
		reservedTokens.put("ELSE", 3);
		reservedTokens.put("FI", 4);
		reservedTokens.put("LOOP", 5);
		reservedTokens.put("BREAK", 6);
		reservedTokens.put("READ", 7);
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

        tokenIDMap.put("PERIOD", 0);
        tokenIDMap.put("SEMICOLON", 1);
        tokenIDMap.put("IF", 2);
        tokenIDMap.put("THEN", 3);
        tokenIDMap.put("ELSE", 4);
        tokenIDMap.put("FI", 5);
        tokenIDMap.put("LOOP", 6);
        tokenIDMap.put("ID", 7);
        tokenIDMap.put("COLON", 8);
        tokenIDMap.put("REPEAT", 9);
        tokenIDMap.put("BREAK", 10);
        tokenIDMap.put("BECOMES", 11);
        tokenIDMap.put("PRINT", 12);
        tokenIDMap.put("READ", 13);
        tokenIDMap.put(",", 14);
        tokenIDMap.put("OR", 15);
        tokenIDMap.put("AND", 16);
        tokenIDMap.put("<", 17);
        tokenIDMap.put("<=", 18);
        tokenIDMap.put("=", 19);
        tokenIDMap.put(">=", 20);
        tokenIDMap.put(">", 21);
        tokenIDMap.put("<>", 22);
        tokenIDMap.put("+", 23);
        tokenIDMap.put("-", 24);
        tokenIDMap.put("*", 25);
        tokenIDMap.put("/", 26);
        tokenIDMap.put("(", 27);
        tokenIDMap.put(")", 28);
        tokenIDMap.put("CONST", 29);
	}

	TokenUtil (String fileName) {
		String pathToFile = System.getProperty("user.dir").replace("\\", "/");
		pathToFile += "/" + fileName;
		try {
			System.setIn(new FileInputStream(new File(pathToFile)));
		} catch (IOException e) {
			print("Error setting system in to " + pathToFile + " " + e.getMessage());
		}
		in = new PushbackReader(new InputStreamReader(System.in), 2);
	}

	public static Entry<String, String> getToken(char currChar) {
		String token = "";
        String tokenValue = "";
        char nextChar;
		while (currChar == ' ' || currChar == '\n') {
			currChar = readIn();
			//nextChar = readIn(); // Ignore white space
		}
		nextChar = readIn();
		String key;
		if (currChar == '.') {
			if (isDigit(nextChar)) {
                token = "CONST";
                tokenValue = handleNumber(readIn(), "" + currChar + nextChar, true, true);
			}
			// Make sure there is not a digit following a ..
//			else if (nextChar == '.') {
//				while (nextChar == '.') {
//					print("Token: " + '.' + " Value: " + 31);
//					nextChar = readIn();
//				}
//				if (isDigit(nextChar)) {
//					throw new InputMismatchException("White space is required between PERIOD and NUMBER." +
//							" Numbers are allowed only one decimal point");
//				}
//				else {
//					unreadIn(nextChar);
//				}
//			}
			else {
				unreadIn(nextChar);
                token = ".";
				tokenValue = ".";
			}
		}
		else if (isDigit(currChar)) {
            token = "CONST";
            tokenValue = handleNumber(nextChar, "" + currChar, false, true);
		}
//		else if (currChar == '"') {
//			print(handleString(nextChar, "" + currChar));
//		}
//		else if (currChar == '-') {
//			if(isDigit(nextChar) || nextChar == '.') {
//				token = "CONST";
//				tokenValue = handleNumber(nextChar, "" + currChar, false, true);
//			}
//			else {
//				token = "OPERATOR";
//				tokenValue = "-";
//			}
//		}
		else if (isOperatorSeperator(key = "" + currChar + nextChar)) {
            token = key;
            tokenValue = transformKey(key);
		}
		else if (isOperatorSeperator(key = "" + currChar)) {
            token = key;
			unreadIn(nextChar);
            tokenValue = transformKey(key);
		}
		else {
            tokenValue = handleReserveredOrIdentifier("" + currChar + nextChar);
			token = reservedTokens.containsKey(token) ? "RESERVED" : "ID";
		}
        final String resultToken = token;
        final String resultTokenValue = tokenValue;
		currentToken = token;
		currentTokenValue = tokenValue;
        Entry<String, String> tokenValueEntry = new Entry<String, String>() {
            @Override
            public String getKey() {
                return resultTokenValue;
            }

            @Override
            public String getValue() {
                return resultToken;
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        };

        return tokenValueEntry;
	}


	private static String handleReserveredOrIdentifier(String string) {
		if (string.matches(".*" + operatorsSeperatorsRegEx + ".*")) {
			//Split the string so that the first item in the array will contain the reserved word or identifier
			String result = string.split(operatorsSeperatorsRegEx)[0];
			//Put separating character(s) back on input stack
			unreadIn(string.substring(result.length(), string.length()).toCharArray());
			if (reservedTokens.containsKey(result.toUpperCase())) {
				return result.toUpperCase();
			}
			else {
				return result;
			}
		}

		return handleReserveredOrIdentifier(string + readIn());
	}

	private static String handleNumber(char currChar, String number, boolean isFloatingPoint, boolean validNumber) throws InputMismatchException {
		if (currChar == '.') {
			if (isFloatingPoint) {
				throw new InputMismatchException("White space is required between PERIOD and NUMBER");
			}
			else {
				// Set validNumber to false as a digit must follow the decimal to still be a number
				return handleNumber(readIn(), number + currChar, true, false);
			}
		}

		if (!(isDigit(currChar))) {
			if (!validNumber) {
				throw new InputMismatchException("White space is required between PERIOD and NUMBER. No digits following the decimal point");
			}
			unreadIn(currChar);
			return number;
		}
		// Set validNumber to true as we have encountered at least one digit after the decimal place if one exists
		return handleNumber(readIn(), number + currChar, isFloatingPoint, true);
	}

	private static String handleString(char currChar, String string) {
		if (currChar == '"') {
			return "Token: " + "STRING--> " + string + currChar + " Value: " + 30;
		}
		return handleString(readIn(), string + currChar);
	}

	private static void print(String msg) {
		System.out.println(msg);
	}

	private static char readIn() {
		char input = '\uFFFF';
		try {
			input = (char) in.read();
		}
		catch (IOException e) {
			print("Error reading from standard in " + e.getMessage());
			System.exit(-1);
		}
		return input;
	}

	private static void unreadIn(char c) {
		char vals [] = {c};
		unreadIn(vals);
	}

	private static void unreadIn(char [] buff) {
		try {
			for(int i = buff.length - 1; i >= 0; i--) {
				in.unread(buff[i]);
			}

		}
		catch (IOException e) {
			print("Error pushing characters back to standard in " + e.getMessage());
			System.exit(-1);
		}
	}

	private static boolean isDigit(int c) {
		return c >= '0' && c <= '9';
	}

	private static boolean isOperatorSeperator(String key) {
		return operatorSeperatorTokens.containsKey(key);
	}

	private static String transformKey(String key) {
		if (key.equals("\n")) {
			return "EOLN";
		}
		else if (key.equals(" ")) {
			return "SPACE";
		}
		else if (key.equals(":=")) {
			return "BECOMES";
		}
		else {
			return key;
		}
	}
	public String getCurrentToken () {
		return currentToken;
	}

	public String getCurrentTokenValue () {
		return currentTokenValue;
	}

	public Entry<String, String> parse () {
		return getToken(readIn());
	}
}
