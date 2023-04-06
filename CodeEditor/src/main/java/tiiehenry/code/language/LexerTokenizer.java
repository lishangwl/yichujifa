package tiiehenry.code.language;
import java.util.*;
import tiiehenry.code.*;
import tiiehenry.code.language.*;

import static tiiehenry.code.Lexer.*;

public class LexerTokenizer {
	private static LexerTokenizer _theOne = null;
	//private LexerTokenizer() {}
	public static LexerTokenizer getInstance() {
		if (_theOne == null) {
			_theOne = new LexerTokenizer();
		}
		return _theOne;
	}

	public ArrayList<Pair> tokenize(DocumentProvider hDoc, Flag _abort) {
		Language language = Lexer.getLanguage();
		ArrayList<Pair> tokens = new ArrayList<Pair>();

		if (!language.isProgLang()) {
			tokens.add(new Pair(0, NORMAL));
			return tokens;
		}

		char[] candidateWord = new char[MAX_KEYWORD_LENGTH];
		int currentCharInWord = 0;

		int spanStartPosition = 0;
		int workingPosition = 0;
		int state = UNKNOWN;
		char prevChar = 0;

		hDoc.seekChar(0);
		while (hDoc.hasNext() && !_abort.isSet()) {
			char currentChar = hDoc.next();
			switch (state) {
				case UNKNOWN: //fall-through
				case NORMAL: //fall-through
				case KEYWORD: //fall-through
				case NAME: //fall-through
				case SINGLE_SYMBOL_WORD:
					int pendingState = state;
					boolean stateChanged = false;
					if (language.isLineStart(prevChar, currentChar)) {
						pendingState = DOUBLE_SYMBOL_LINE;
						stateChanged = true;
					} else if (language.isMultilineStartDelimiter(prevChar, currentChar)) {
						pendingState = DOUBLE_SYMBOL_DELIMITED_MULTILINE;
						stateChanged = true;
					} else if (language.isDelimiterA(currentChar)) {
						pendingState = SINGLE_SYMBOL_DELIMITED_A;
						stateChanged = true;
					} else if (language.isDelimiterB(currentChar)) {
						pendingState = SINGLE_SYMBOL_DELIMITED_B;
						stateChanged = true;
					} else if (language.isLineAStart(currentChar)) {
						pendingState = SINGLE_SYMBOL_LINE_A;
						stateChanged = true;
					} else if (language.isLineBStart(currentChar)) {
						pendingState = SINGLE_SYMBOL_LINE_B;
						stateChanged = true;
					}


					if (stateChanged) {
						if (pendingState == DOUBLE_SYMBOL_LINE ||
							pendingState == DOUBLE_SYMBOL_DELIMITED_MULTILINE) {
							// account for previous char
							spanStartPosition = workingPosition - 1;
							if (tokens.get(tokens.size() - 1).first == spanStartPosition) {
								tokens.remove(tokens.size() - 1);
							}
						} else {
							spanStartPosition = workingPosition;
						}

						// If a span appears mid-word, mark the chars preceding
						// it as NORMAL, if the previous span isn't already NORMAL
						if (currentCharInWord > 0 && state != NORMAL) {
							tokens.add(new Pair(workingPosition - currentCharInWord, NORMAL));
						}

						state = pendingState;
						tokens.add(new Pair(spanStartPosition, state));
						currentCharInWord = 0;
					} else if (language.isWhitespace(currentChar) || language.isOperator(currentChar)) {
						if (currentCharInWord > 0) {
							// full word obtained; mark the beginning of the word accordingly
							if (language.isWordStart(candidateWord[0])) {
								spanStartPosition = workingPosition - currentCharInWord;
								state = SINGLE_SYMBOL_WORD;
								tokens.add(new Pair(spanStartPosition, state));
							} else if (language.isKeyword(new String(candidateWord, 0, currentCharInWord))) {
								spanStartPosition = workingPosition - currentCharInWord;
								state = KEYWORD;
								tokens.add(new Pair(spanStartPosition, state));
							} else if (language.isName(new String(candidateWord, 0, currentCharInWord))) {
								spanStartPosition = workingPosition - currentCharInWord;
								state = NAME;
								tokens.add(new Pair(spanStartPosition, state));
							} else if (state != NORMAL) {
								spanStartPosition = workingPosition - currentCharInWord;
								state = NORMAL;
								tokens.add(new Pair(spanStartPosition, state));
							}
							currentCharInWord = 0;
						}

						// mark operators as normal
						if (state != NORMAL && language.isOperator(currentChar)) {
							state = NORMAL;
							tokens.add(new Pair(workingPosition, state));
						}
					} else if (currentCharInWord < MAX_KEYWORD_LENGTH) {
						// collect non-whitespace chars up to MAX_KEYWORD_LENGTH
						candidateWord[currentCharInWord] = currentChar;
						currentCharInWord++;
					}
					break;


				case DOUBLE_SYMBOL_LINE: // fall-through
				case SINGLE_SYMBOL_LINE_A: // fall-through
				case SINGLE_SYMBOL_LINE_B:
					if (language.isMultilineStartDelimiter(prevChar, currentChar)) {
						state = DOUBLE_SYMBOL_DELIMITED_MULTILINE;
					} else if (currentChar == '\n') {
						state = UNKNOWN;
					}
					break;


				case SINGLE_SYMBOL_DELIMITED_A:
					if ((language.isDelimiterA(currentChar) || currentChar == '\n')
						&& !language.isEscapeChar(prevChar)) {
						state = UNKNOWN;
					} else if (language.isEscapeChar(currentChar) && language.isEscapeChar(prevChar)) {
						currentChar = ' ';
					}
					break;


				case SINGLE_SYMBOL_DELIMITED_B:
					if ((language.isDelimiterB(currentChar) || currentChar == '\n')
						&& !language.isEscapeChar(prevChar)) {
						state = UNKNOWN;
					} else if (language.isEscapeChar(currentChar)
							   && language.isEscapeChar(prevChar)) {
						currentChar = ' ';
					}
					break;

				case DOUBLE_SYMBOL_DELIMITED_MULTILINE:
					if (language.isMultilineEndDelimiter(prevChar, currentChar)) {
						state = UNKNOWN;
					}
					break;

				default:
					TextWarriorException.fail("Invalid state in TokenScanner");
					break;
			}
			++workingPosition;
			prevChar = currentChar;
		}
		// end state machine


		if (tokens.isEmpty()) {
			// return value cannot be empty
			tokens.add(new Pair(0, NORMAL));
		}

		return tokens;
	}
}
