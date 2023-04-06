package tiiehenry.code.language.xml;

import tiiehenry.code.language.*;

/**
 * Singleton class containing the symbols and operators of the Javascript language
 */
public class XMLLanguage extends Language {

	private static XMLLanguage _theOne;

	public static Language getInstance() {
		if (_theOne == null) {
			_theOne = new XMLLanguage();
		}
		return _theOne;
	}

	private XMLLanguage() {
	}

	/**
	 * Whether the word after c is a token
	 */
	public boolean isWordStart2(char c) {
		return (c == '.');
	}

	public boolean isLineAStart(char c) {
		return false;
	}

	/**
	 * Whether c0c1 signifies the start of a multi-line token
	 */
	public boolean isMultilineStartDelimiter(char c0, char c1, char c2, char c3) {
		return (c0 == '<' && c1 == '!' && c2 == '-' && c3 == '-');
	}

	/**
	 * Whether c0c1 signifies the end of a multi-line token
	 */
	public boolean isMultilineEndDelimiter(char c0, char c1, char c2) {
		return (c0 == '-' && c1 == '-' && c2 == '>');
	}
	public boolean isMultilineStartDelimiter(char c0, char c1) {
		return (c0 == '<' && c1 == '!');
	}

	/**
	 * Whether c0c1 signifies the end of a multi-line token
	 */
	public boolean isMultilineEndDelimiter(char c0, char c1) {
		return (c0 == '-' && c1 == '>');
	}
}

