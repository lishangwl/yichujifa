package tiiehenry.code;
import java.io.*;

public class CharSeqReader extends Reader {
	int offset = 0;
	CharSequence src;

	public CharSeqReader(CharSequence src) {
		this.src = src;
	}

	@Override
	public void close() {
		src = null;
		offset = 0;
	}

	@Override
	public int read(char[] chars, int i, int i1) throws IOException {
		int len = Math.min(src.length() - offset, i1);
		for (int n = 0; n < len; n++) {
			try {
				char c = src.charAt(offset++);
				chars[i++] = c;
			} catch (Exception e) {

			}
		}
		if (len <= 0)
			return  -1;
		return len;
	}
}
