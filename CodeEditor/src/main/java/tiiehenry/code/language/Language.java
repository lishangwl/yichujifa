package tiiehenry.code.language;

import java.util.*;
import tiiehenry.code.*;
/**
 * 高级语言类
 * 包含符号
 */
public abstract class Language {
	public final static char EOF = '\uFFFF';
	public final static char NULL_CHAR = '\u0000';
	public final static char NEWLINE = '\n';
	public final static char BACKSPACE = '\b';
	public final static char TAB = '\t';
	public final static String GLYPH_NEWLINE = "\u21b5";
	public final static String GLYPH_SPACE = "\u00b7";
	public final static String GLYPH_TAB = "\u00bb";

	private final static char[] BASIC_C_OPERATORS = {
		'(', ')', '{', '}', '.', ',', ';', '=', '+', '-',
		'/', '*', '&', '!', '|', ':', '[', ']', '<', '>',
		'?', '~', '%', '^'};


	//关键字
	protected HashMap<String, Integer> _keywords = new HashMap<String, Integer>(0);
	private String[] _keyword;
	public void setKeywords(String[] keywords) {
		_keyword = keywords;
		_keywords = new HashMap<String, Integer>(keywords.length);
		for (int i = 0; i < keywords.length; ++i) {
			_keywords.put(keywords[i], Lexer.KEYWORD);
		}
	}
	public String[] getKeywords() {
		return _keyword;
	}
	public final boolean isKeyword(String s) {
		return _keywords.containsKey(s);
	}

	//子类需要实现该方法
	public LexerTokenizer getTokenizer() {
		return LexerTokenizer.getInstance();
	}
	//子类需要实现该方法
	public DefFormatter getFormatter() {
		return DefFormatter.getInstance();
	}

	//自带函数方法
	protected HashMap<String, Integer> _names = new HashMap<String, Integer>(0);
	private String[] _name;
	public void setNames(String[] names) {
		_name = names;
		ArrayList<String> buf=new ArrayList<String>();
		_names = new HashMap<String, Integer>(names.length);
		for (int i = 0; i < names.length; ++i) {
			if (!buf.contains(names[i]))
				buf.add(names[i]);
			_names.put(names[i], Lexer.NAME);
		}
		_name = new String[buf.size()];
		buf.toArray(_name);
	}
	public String[] getNames() {
		return _name;
	}
	public final boolean isName(String s) {
		return _names.containsKey(s);
	}


	//包(os,table)，类(File)
	protected HashMap<String, String[]> _bases = new HashMap<String, String[]>(0);
	public void addBasePackage(String name, String[] names) {
		_bases.put(name, names);
	}
	public String[] getBasePackage(String name) {
		return _bases.get(name);
	}
	public final boolean isBasePackage(String s) {
		return _bases.containsKey(s);
	}
	public final boolean isBaseWord(String p, String s) {
		String[] pkg= _bases.get(p);
		for (String n:pkg) {
			if (n.equals(s))
				return true;
		}
		return false;
	}


	//用户函数，方法
	protected HashMap<String, Integer> _users = new HashMap<String, Integer>(0);
	private String[] _userWords=new String[0];
	private ArrayList<String> _ueserCache = new ArrayList<String>();
	public void addUserWord(String name) {
		if (!_ueserCache.contains(name) && !_names.containsKey(name))
			_ueserCache.add(name);
		_users.put(name, Lexer.NAME);	
	}
	public String[] getUserWord() {
		return  _userWords;
	}
	public void clearUserWord() {
		_ueserCache.clear();
		_users.clear();
	}
	public void updateUserWord() {
		String[] uw = new String[_ueserCache.size()];
		_userWords = _ueserCache.toArray(uw);
	}
	public final boolean isUserWord(String s) {
		return _users.containsKey(s);
	}


	//符号
	protected HashMap<Character, Integer> _operators = generateOperators(BASIC_C_OPERATORS);
	protected void setOperators(char[] operators) {
		_operators = generateOperators(operators);
	}
	private HashMap<Character, Integer> generateOperators(char[] operators) {
		HashMap<Character, Integer> operatorsMap = new HashMap<Character, Integer>(operators.length);
		for (int i = 0; i < operators.length; ++i) {
			operatorsMap.put(operators[i], Lexer.OPERATOR);
		}
		return operatorsMap;
	}
	public final boolean isOperator(char c) {
		return _operators.containsKey(Character.valueOf(c));
	}



	public boolean isWhitespace(char c) {
		return (c == ' ' || c == '\n' || c == '\t' ||
			c == '\r' || c == '\f' || c == EOF);
	}

	public boolean isSentenceTerminator(char c) {
		return (c == '.');
	}

	public boolean isEscapeChar(char c) {
		return (c == '\\');
	}

	//是否为高级语言
	public boolean isProgLang() {
		return true;
	}

	/**
	 * Whether the word after c is a token
	 */
	public boolean isWordStart(char c) {
		return false;
	}

	/**
	 * Whether cSc is a token, where S is a sequence of characters that are on the same line
	 */
	public boolean isDelimiterA(char c) {
		return (c == '"');
	}

	/**
	 * Same concept as isDelimiterA(char), but Language and its subclasses can
	 * specify a second type of symbol to use here
	 */
	public boolean isDelimiterB(char c) {
		return (c == '\'');
	}

	/**
	 * Whether cL is a token, where L is a sequence of characters until the end of the line
	 */
	public boolean isLineAStart(char c) {
		return (c == '#');
	}

	/**
	 * Same concept as isLineAStart(char), but Language and its subclasses can
	 * specify a second type of symbol to use here
	 */
	public boolean isLineBStart(char c) {
		return false;
	}

	/**
	 * Whether c0c1L is a token, where L is a sequence of characters until the end of the line
	 */
	public boolean isLineStart(char c0, char c1) {
		return (c0 == '/' && c1 == '/');
	}

	/**
	 * Whether c0c1 signifies the start of a multi-line token
	 */
	public boolean isMultilineStartDelimiter(char c0, char c1) {
		return (c0 == '/' && c1 == '*');
	}

	/**
	 * Whether c0c1 signifies the end of a multi-line token
	 */
	public boolean isMultilineEndDelimiter(char c0, char c1) {
		return (c0 == '*' && c1 == '/');
	}
}

