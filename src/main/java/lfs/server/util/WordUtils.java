package lfs.server.util;

public class WordUtils {

	private WordUtils() {}

	public static String humenize(String name) {
		if(WordUtils.isEmpty(name)) {
			return name;
		}
		StringBuilder newName = new StringBuilder();
		int length = name.length();
		for (int i = 0; i < name.length(); i++) {
			char ch = name.charAt(i);
			if(i == 0){
				ch = Character.toUpperCase(ch);
			}
			if(Character.isUpperCase(ch) && i != 0){
				if(!((i+1) < length && Character.isUpperCase(name.charAt(i+1)))) {
					newName.append(" ");
				}
			}
			newName.append(ch);
		}
		return newName.toString();
	}

	public static String toTitleCase(String str) {
		if(isEmpty(str)) {
			return str;
		}
		final char[] buffer = str.toCharArray();
		buffer[0] = Character.toUpperCase(buffer[0]);
		for(int i = 1; i < buffer.length; i++) {
			if(isTitleBeginner(buffer[i-1])) {
				buffer[i] = Character.toUpperCase(buffer[i]);
			}
			else {
				buffer[i] = Character.toLowerCase(buffer[i]);
			}
		}
		return new String(buffer);
	}

	private static boolean isTitleBeginner(char ch) {
		return Character.isWhitespace(ch) 
				|| ch == '.' 
				|| ch == '?'
				|| ch == '!'
				|| ch == '(';
	}

	public static String classToVariable(String className) {
		if(WordUtils.isEmpty(className)) {
			return className;
		}
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < className.length(); i++) {
			char ch = className.charAt(i);
			if(i == 0){
				ch = Character.toLowerCase(ch);
			}
			newName.append(ch);
		}
		return newName.toString();
	}

	public static String toResourceName(Class<?> type) {
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < type.getSimpleName().length(); i++) {
			char ch = type.getSimpleName().charAt(i);
			if(i == 0){
				ch = Character.toLowerCase(ch);
			}
			if(Character.isUpperCase(ch) && i != 0){
				newName.append("-");
				ch = Character.toLowerCase(ch);
			}
			newName.append(ch);
		}
		return newName.toString();
	}

	public static String toFileName(Class<?> type) {
		StringBuilder newName = new StringBuilder();
		for (int i = 0; i < type.getSimpleName().length(); i++) {
			char ch = type.getSimpleName().charAt(i);
			if(i == 0){
				ch = Character.toLowerCase(ch);
			}
			if(Character.isUpperCase(ch) && i != 0){
				newName.append("_");
				ch = Character.toLowerCase(ch);
			}
			newName.append(ch);
		}
		return newName.toString();
	}

	public static String toResourceName(Object obj) {
		return toResourceName(obj.getClass());
	}

	public static boolean isEmpty(final String cs) {
		return cs == null || cs.trim().length() == 0;
	}
}
