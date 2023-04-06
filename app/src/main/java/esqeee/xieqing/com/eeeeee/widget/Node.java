package esqeee.xieqing.com.eeeeee.widget;

import java.util.ArrayList;
import java.util.List;

/**
 * 节点
 * @Project    App_View
 * @Package    com.android.view.tree
 * @author     chenlin
 * @version    1.0
 * @Date       2013年6月4日
 * @Note       TODO
 */
public class Node {
    private int id;
    private int pId = 0;//父节点，根节点是0
    private int level;//级别
    private String name;//节点名称
    private int icon;//小图标
    private Node parent;//父节点
    //子节点
    private List<Node> children = new ArrayList<Node>();
    private boolean isExpend = false;//是否展开

    public Node() {
    }

    public Node(int id, int pId, String name) {
        this.id = id;
        this.pId = pId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }


    public void setLevel(int level) {
        this.level = level;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }


    public void setIcon(int icon) {
        this.icon = icon;
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }


    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }


    public boolean isExpend() {
        return isExpend;
    }

    /**
     * 设置展开时不但要展开自己，也要展开所有的子节点
     * @param isExpend
     */
    public void setExpend(boolean isExpend) {
        this.isExpend = isExpend;
        if (!isExpend) {
            for(Node node : children){
                node.setExpend(isExpend);
            }
        }
    }

    /**
     * 获得级别
     * @return
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * 判断是否是叶子节点
     * @return
     */
    public boolean isLeaf(){
        return children.size() == 0;
    }

    /**
     * 判断是否是根节点
     * @return
     */
    public boolean isRoot(){
        return parent == null;
    }

    /**
     * 判断父节点是否打开
     * @return
     */
    public boolean isParentExptend(){
        if (parent == null) {
            return false;
        }
        return parent.isExpend();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((children == null) ? 0 : children.hashCode());
        result = prime * result + icon;
        result = prime * result + id;
        result = prime * result + (isExpend ? 1231 : 1237);
        result = prime * result + level;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + pId;
        result = prime * result + ((parent == null) ? 0 : parent.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Node other = (Node) obj;
        if (children == null) {
            if (other.children != null)
                return false;
        } else if (!children.equals(other.children))
            return false;
        if (icon != other.icon)
            return false;
        if (id != other.id)
            return false;
        if (isExpend != other.isExpend)
            return false;
        if (level != other.level)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (pId != other.pId)
            return false;
        if (parent == null) {
            if (other.parent != null)
                return false;
        } else if (!parent.equals(other.parent))
            return false;
        return true;
    }

}