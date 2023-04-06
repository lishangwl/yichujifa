package tiiehenry.code.language;

/**
 * 无编程语言类
 */
import tiiehenry.code.*;

public class LanguageNonProg extends Language {
	private static LanguageNonProg _theOne = null;

	private final static String[] keywords = {};

	private final static char[] operators = {};


	public static LanguageNonProg getInstance() {
		if (_theOne == null) {
			_theOne = new LanguageNonProg();
		}
		return _theOne;
	}

	private LanguageNonProg() {
		super.setKeywords(keywords);
		super.setOperators(operators);
	}

	@Override
	public boolean isProgLang() {
		return false;
	}

	@Override
	public boolean isEscapeChar(char c) {
		return false;
	}

	@Override
	public boolean isDelimiterA(char c) {
		return false;
	}

	@Override
	public boolean isDelimiterB(char c) {
		return false;
	}

	@Override
	public boolean isLineAStart(char c) {
		return false;
	}

	@Override
	public boolean isLineStart(char c0, char c1) {
		return false;
	}

	@Override
	public boolean isMultilineStartDelimiter(char c0, char c1) {
		return false;
	}

	@Override
	public boolean isMultilineEndDelimiter(char c0, char c1) {
		return false;
	}
}
