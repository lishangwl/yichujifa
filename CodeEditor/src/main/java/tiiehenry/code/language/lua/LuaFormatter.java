package tiiehenry.code.language.lua;

import java.io.*;

import tiiehenry.code.language.*;

public class LuaFormatter extends DefFormatter {
	private static LuaFormatter _theOne = null;

	public static LuaFormatter getInstance() {
		if (_theOne == null) {
			_theOne = new LuaFormatter();
		}
		return _theOne;
	}


	@Override
	public int createAutoIndent(CharSequence text) {
		LuaLexer lexer = new LuaLexer(text);
		int idt = 0;
		try {
			while (true) {
				LuaType type = lexer.advance();
				if (type == null)
					break;
				idt += indentAuto(type);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return idt;
	}

	private int indentAuto(LuaType t) {
		switch (t) {
			case DO:
			case FUNCTION:
			case THEN:
			case REPEAT:
			case LCURLY:
			case ELSE:
			case ELSEIF:
				return 1;
			case UNTIL:
			case RETURN:
				//case RCURLY:
				return -1;
			default:
				return 0;
		}
	}


	private int indent(LuaType t) {
		switch (t) {
			case DO:
			case FUNCTION:
			case THEN:
			case REPEAT:
			case LCURLY:
			case ELSE:
				//case ELSEIF:
				return 1;
			case UNTIL:
				//case ELSEIF:
			case END:
			case RCURLY:
				return -1;
			default:
				return 0;
		}
	}

	@Override
	public CharSequence format(CharSequence text, int width) {
		StringBuilder builder = new StringBuilder();
		boolean isNewLine = true;
		LuaLexer lexer = new LuaLexer(text);
		try {
			int idt = 0;

			while (true) {
				LuaType type = lexer.advance();
				if (type == null)
					break;

				if (type == LuaType.NEW_LINE) {
					if (builder.length() > 0 && builder.charAt(builder.length() - 1) == ' ')
						builder.deleteCharAt(builder.length() - 1);
					isNewLine = true;
					builder.append('\n');
					idt = Math.max(0, idt);
				} else if (isNewLine) {
					switch (type) {
						case WHITE_SPACE:
							break;
						case ELSE:
						case ELSEIF:
						case CASE:
						case DEFAULT:
							//idt--;
							builder.append(createIndent(idt * width - width / 2));
							builder.append(lexer.yytext());
							//idt++;
							isNewLine = false;
							break;
						case DOUBLE_COLON:
						case AT:
							builder.append(lexer.yytext());
							isNewLine = false;
							break;
						case END:
						case UNTIL:
						case RCURLY:
							idt--;
							builder.append(createIndent(idt * width));
							builder.append(lexer.yytext());
							isNewLine = false;
							break;
						default:
							builder.append(createIndent(idt * width));
							builder.append(lexer.yytext());
							idt += indent(type);
							isNewLine = false;
					}
				} else if (type == LuaType.WHITE_SPACE) {
					builder.append(' ');
				} else {
					builder.append(lexer.yytext());
					idt += indent(type);
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder;
	}

	private static char[] createIndent(int n) {
		if (n < 0)
			return new char[0];
		char[] idts = new char[n];
		for (int i = 0; i < n; i++)
			idts[i] = indentChar;
		return idts;
	}


}
