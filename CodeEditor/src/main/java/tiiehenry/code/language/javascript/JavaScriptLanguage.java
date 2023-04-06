package tiiehenry.code.language.javascript;

import tiiehenry.code.language.*;

public class JavaScriptLanguage extends Language {
	private static JavaScriptLanguage _theOne = null;

	private final static String[] keywords = {
		"abstract", "boolean", "break", "byte", "case", "catch", "char",
		"class", "const", "continue", "debugger", "default", "delete", "do",
		"double", "else", "enum", "export", "extends", "false", "final",
		"finally", "float", "for", "function", "goto", "if", "implements",
		"import", "in", "instanceof", "int", "interface", "long", "native",
		"new", "null", "package", "private", "protected", "public", "return",
		"short", "static", "super", "switch", "synchronized", "this", "throw",
		"throws", "transient", "true", "try", "typeof", "var", "void",
		"volatile", "while", "with"
	};

	public static JavaScriptLanguage getInstance(){
		if(_theOne == null){
			_theOne = new JavaScriptLanguage();
		}
		return _theOne;
	}

	private JavaScriptLanguage(){
		super.setKeywords(keywords);
	}

	public boolean isLineAStart(char c){
		return false;
	}
}
