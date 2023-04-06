package esqeee.xieqing.com.eeeeee.bean;

public enum VariableType {
    STRING("文本型",0),
    INT("整数型",1),
    BYTES("字节集",2),
    BOOL("布尔型",3),
    STRING_ARRAY("文本型数组",4),
    NODE("控件",5),
    NODE_ARRAY("控件数组",6),
    RECT("矩型",7);


    private int type = 0 ;
    private String typeName = "";
    VariableType(String typeName, int i) {
        this.typeName = typeName;
        this.type = i;
    }

    public String getTypeName() {
        return typeName;
    }

    public static VariableType from(int i){
        if (i >= values().length){
            return STRING;
        }
        return values()[i];
    }
}
