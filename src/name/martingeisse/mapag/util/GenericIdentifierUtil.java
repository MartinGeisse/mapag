package name.martingeisse.mapag.util;

/**
 *
 */
public final class GenericIdentifierUtil {

	// prevent instantiation
	private GenericIdentifierUtil() {
	}

	public static String toIdentifier(String s, boolean firstCharacterUppercase) {
		StringBuilder builder = new StringBuilder();
		boolean firstValidCharacter = true;
		boolean forceNextCharacterCase = true;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
				if (forceNextCharacterCase) {
					if (firstCharacterUppercase || !firstValidCharacter) {
						c = Character.toUpperCase(c);
					} else {
						c = Character.toLowerCase(c);
					}
				}
				builder.append(c);
				firstValidCharacter = false;
				forceNextCharacterCase = false;
			} else if (c >= '0' && c <= '9') {
				if (firstValidCharacter) {
					builder.append('_');
				}
				builder.append(c);
				firstValidCharacter = false;
				forceNextCharacterCase = false;
			} else if (c == '/') {
				builder.append('_');
				forceNextCharacterCase = true;
			} else {
				forceNextCharacterCase = true;
			}
		}
		return builder.toString();
	}

}
