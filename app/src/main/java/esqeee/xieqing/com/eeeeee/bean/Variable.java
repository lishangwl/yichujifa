package esqeee.xieqing.com.eeeeee.bean;

public class Variable extends JSONBean {
    private String name;
    private boolean isFinal = false;
    private VariableType type;

    public Variable(String name,VariableType type){
        super("{\"name\":\""+name+"\",\"type\":"+type.ordinal()+"}");
        this.name = name;
        this.type = type;
    }
    public Variable(String name,VariableType type,Object finalValue){
        this(name,type);
        this.isFinal = true;
        put("value",String.valueOf(finalValue));
    }
    public String getValue(){
        return getString("value");
    }
    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public VariableType getType() {
        return type;
    }
}
