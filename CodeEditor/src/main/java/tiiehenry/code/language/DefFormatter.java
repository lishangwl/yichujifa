package tiiehenry.code.language;

public class DefFormatter implements Formatter
{
	public static char indentChar='\t';
	
	private static DefFormatter _theOne = null;
	public static DefFormatter getInstance() {
		if (_theOne == null) {
			_theOne = new DefFormatter();
		}
		return _theOne;
	}
	
	public int createAutoIndent(CharSequence text){
		return 1;
	}
	public CharSequence format(CharSequence text, int width){
		return text;
	}
	
}
