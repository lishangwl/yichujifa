package tiiehenry.code.language;

public interface Formatter
{
		public int createAutoIndent(CharSequence text);
		
		public CharSequence format(CharSequence text, int width);

}
