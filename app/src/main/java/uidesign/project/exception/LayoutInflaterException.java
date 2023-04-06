package uidesign.project.exception;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class LayoutInflaterException extends RuntimeException {
    private String message = "";
    public LayoutInflaterException(String message){
        super(message);
        this.message = message;
    }

    public LayoutInflaterException(XmlPullParserException e, XmlPullParser pullParser){
        super(e.getMessage());
        this.message = e.getMessage() +" on line :"+ pullParser.getLineNumber()+" colum:"+pullParser.getColumnNumber();
    }

    @Override
    public String toString() {
        return "LayoutInflaterException:"+message;
    }
}
