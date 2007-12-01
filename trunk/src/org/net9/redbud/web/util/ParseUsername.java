package org.net9.redbud.web.util;

public class ParseUsername {
	public static boolean parse(String username) {

		String regex = "^(root)|([0-9]{3})|([0-9]{10})$";

		return java.util.regex.Pattern.matches(regex, username);
	}
}
