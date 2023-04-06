package tiiehenry.code.view;

public class ColorSchemeLight extends ColorScheme {

	public ColorSchemeLight(){
		//文字
		setColor(Colorable.FOREGROUND, OFF_BLACK);
		//背景
		setColor(Colorable.BACKGROUND, OFF_WHITE);
		//选取文字
		setColor(Colorable.SELECTION_FOREGROUND, OFF_WHITE);
		//选取背景
		setColor(Colorable.SELECTION_BACKGROUND, 0xFF999999);
		//关键字
		setColor(Colorable.KEYWORD, BLUE_DARK);
		//函数名
		setColor(Colorable.LITERAL, BLUE_LIGHT);
		//字符串、数字
		setColor(Colorable.STRING,0xFFAA2200);
		//次关键字
		setColor(Colorable.NAME,0xFF2A40FF);
		//符号
		setColor(Colorable.SECONDARY,BLUE_DARK);
		//光标
		setColor(Colorable.CARET_DISABLED,GREEN_DARK);
		//yoyo？
		setColor(Colorable.CARET_FOREGROUND, OFF_WHITE);
		//yoyo背景
		setColor(Colorable.CARET_BACKGROUND,0xFF29B6F6);
		//当前行
		setColor(Colorable.LINE_HIGHLIGHT, 0x1E888888);

		//注释
		setColor(Colorable.COMMENT, GREEN_LIGHT);

	}

	private static final int OFF_WHITE = 0xFFFFFFFF;
	private static final int OFF_BLACK = 0xFF333333;

	private static final int GREEN_LIGHT = 0xFF009B00;
	private static final int GREEN_DARK = 0xFF3F7F5F;
	private static final int BLUE_LIGHT = 0xFF0F9CFF;
	private static final int BLUE_DARK = 0xFF2C82C8;

}
