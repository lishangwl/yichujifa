package tiiehenry.code.language.lua;

import android.graphics.Rect;

import java.util.ArrayList;

import tiiehenry.code.DocumentProvider;
import tiiehenry.code.Flag;
import tiiehenry.code.Lexer;
import tiiehenry.code.Pair;
import tiiehenry.code.TextWarriorException;
import tiiehenry.code.language.Language;
import tiiehenry.code.language.LexerTokenizer;

import static tiiehenry.code.Lexer.DOUBLE_SYMBOL_LINE;
import static tiiehenry.code.Lexer.KEYWORD;
import static tiiehenry.code.Lexer.LITERAL;
import static tiiehenry.code.Lexer.NAME;
import static tiiehenry.code.Lexer.NORMAL;
import static tiiehenry.code.Lexer.OPERATOR;
import static tiiehenry.code.Lexer.SINGLE_SYMBOL_DELIMITED_A;

public class LuaTokenizer extends LexerTokenizer{
	private static LuaTokenizer _theOne = null;
	//private LuaTokenize() {}
	public static LuaTokenizer getInstance() {
		if (_theOne == null) {
			_theOne = new LuaTokenizer();
		}
		return _theOne;
	}
	@Override
	public ArrayList<Pair> tokenize(DocumentProvider hDoc, Flag _abort) {
		int rowCount = hDoc.getRowCount();
		int maxRow = 9999;
		ArrayList<Pair> tokens = new ArrayList<Pair>(8196);
		ArrayList<Rect> lines = new ArrayList<>(8196);
		ArrayList<Rect> lineStacks = new ArrayList<>(8196);
		ArrayList<Rect> lineStacks2 = new ArrayList<>(8196);

		LuaLexer lexer = new LuaLexer(hDoc);
		Language language = Lexer.getLanguage();
		language.clearUserWord();
		try {
			int idx = 0;

			LuaType lastType = null;
			LuaType lastType2 = null;

			String lastName = "";
			Pair lastPair = null;
			int lastLen = 0;
			StringBuilder bul = new StringBuilder();
			boolean isModule = false;
			boolean hasDo = true;
			int lastNameIdx = -1;
			while (!_abort.isSet()) {
				Pair pair = null;
				LuaType type = lexer.advance();
				if (type == null)
					break;
				int len = lexer.yylength();

				if (isModule && lastType == LuaType.STRING && type != LuaType.STRING) {
					String mod = bul.toString();
					if (bul.length() > 2)
						language.addUserWord(mod.substring(1, mod.length() - 1));
					bul = new StringBuilder();
					isModule = false;
				}

                    /*if (lastType2 == type && lastPair != null) {
                        lastPair.setFirst(lastLen += len);
                        continue;
                    }*/
				lastLen = len;
				switch (type) {
					case DO:
						if (hasDo) {
							lineStacks.add(new Rect(lexer.yychar(), lexer.yyline(), 0, lexer.yyline()));
						}
						hasDo = true;
						//关键字
						tokens.add(new Pair(len, KEYWORD));
						break;
					case WHILE:
					case FOR:
						hasDo = false;
						lineStacks.add(new Rect(lexer.yychar(), lexer.yyline(), 0, lexer.yyline()));
						//关键字
						tokens.add(new Pair(len, KEYWORD));
						break;
					case FUNCTION:
					case IF:
					case SWITCH:
						lineStacks.add(new Rect(lexer.yychar(), lexer.yyline(), 0, lexer.yyline()));
						//关键字
						tokens.add(new Pair(len, KEYWORD));
						break;
					case END:
						int size = lineStacks.size();
						if (size > 0) {
							Rect rect = lineStacks.remove(size - 1);
							rect.bottom = lexer.yyline();
							rect.right = lexer.yychar();
							if (rect.bottom - rect.top>1)
								lines.add(rect);
						}
						//关键字
						tokens.add(new Pair(len, KEYWORD));
						hasDo = true;
						break;
					case TRUE:
					case FALSE:
					case NOT:
					case AND:
					case OR:
					case THEN:
					case ELSEIF:
					case ELSE:
					case IN:
					case RETURN:
					case BREAK:
					case LOCAL:
					case REPEAT:
					case UNTIL:
					case NIL:

					case CASE:
					case DEFAULT:
					case CONTINUE:
					case GOTO:
						//关键字
						tokens.add(new Pair(len, KEYWORD));
						break;
					case LCURLY:
						lineStacks2.add(new Rect(lexer.yychar(), lexer.yyline(), 0, lexer.yyline()));
						//符号
						tokens.add(pair = new Pair(len, OPERATOR));
						break;
					case RCURLY:
						int size2 = lineStacks2.size();
						if (size2 > 0) {
							Rect rect = lineStacks2.remove(size2 - 1);
							rect.bottom = lexer.yyline();
							rect.right = lexer.yychar();
							if (rect.bottom - rect.top>1)
								lines.add(rect);
						}
						//符号
						tokens.add(pair = new Pair(len, OPERATOR));
						break;
					case LPAREN:
					case RPAREN:
					case LBRACK:
					case RBRACK:
					case COMMA:
					case DOT:
						//符号
						tokens.add(pair = new Pair(len, OPERATOR));
						break;
					case STRING:
					case LONG_STRING:
						//字符串
						tokens.add(pair = new Pair(len, SINGLE_SYMBOL_DELIMITED_A));
						if (rowCount > maxRow)
							break;

						if (lastName.equals("require"))
							isModule = true;

						if (isModule)
							bul.append(lexer.yytext());
						break;
					case NAME:
						if (rowCount > maxRow) {
							tokens.add(new Pair(len, NORMAL));
							break;
						}
						if (lastType2 == LuaType.NUMBER) {
							Pair p = tokens.get(tokens.size()-1);
							p.setSecond(NORMAL);
							p.setFirst(p.first+len);
						}
						String name = lexer.yytext();
						if (lastType == LuaType.FUNCTION) {
							//函数名
							tokens.add(new Pair(len, LITERAL));
							language.addUserWord(name);
						} else if (language.isUserWord(name)) {
							tokens.add(new Pair(len, LITERAL));
						} else if (lastType == LuaType.GOTO || lastType == LuaType.AT) {
							tokens.add(new Pair(len, LITERAL));
						} else if (language.isBasePackage(name)) {
							tokens.add(new Pair(len, NAME));
						} else if (lastType == LuaType.DOT && language.isBasePackage(lastName) && language.isBaseWord(lastName, name)) {
							//标准库函数
							tokens.add(new Pair(len, NAME));
						} else if (language.isName(name)) {
							tokens.add(new Pair(len, NAME));
						} else {
							tokens.add(new Pair(len, NORMAL));
						}

						if (lastType == LuaType.ASSIGN && name.equals("require")) {
							language.addUserWord(lastName);
							if (lastNameIdx>=0) {
								Pair p = tokens.get(lastNameIdx-1);
								p.setSecond(LITERAL);
								lastNameIdx=-1;
							}
						}
						lastNameIdx=tokens.size();
						lastName = name;
						break;
					case SHORT_COMMENT:
					case BLOCK_COMMENT:
					case DOC_COMMENT:
						//注释
						tokens.add(pair = new Pair(len, DOUBLE_SYMBOL_LINE));
						break;
					case NUMBER:
						//数字
						tokens.add(new Pair(len, LITERAL));
						break;
					default:
						tokens.add(pair = new Pair(len, NORMAL));
				}

				if (type != LuaType.WHITE_SPACE
					//&& type != LuaTokenTypes.NEWLINE && type != LuaTokenTypes.NL_BEFORE_LONGSTRING
						) {
					lastType = type;
				}
				lastType2 = type;
				if (pair != null)
					lastPair = pair;
				idx += len;
			}
		} catch (Exception e) {
			e.printStackTrace();
			TextWarriorException.fail(e.getMessage());
		}
		if (tokens.isEmpty()) {
			// return value cannot be empty
			tokens.add(new Pair(0, NORMAL));
		}
		language.updateUserWord();
		Lexer.mLines = lines;
		return tokens;
	}
}
