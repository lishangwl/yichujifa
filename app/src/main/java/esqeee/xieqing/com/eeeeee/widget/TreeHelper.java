package esqeee.xieqing.com.eeeeee.widget;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import esqeee.xieqing.com.eeeeee.R;

/**
 * 树结构帮助类
 * 
 * @Project App_View
 * @Package com.android.view.tree
 * @author chenlin
 * @version 1.0
 * @Date 2014年6月4日
 * @Note TODO
 */
public class TreeHelper {

    private static final String TAG = "ture";

    /**
     * 得到排好序的节点
     * 
     * @param datas
     * @param defaultExpandLevel
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static <T> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) throws IllegalArgumentException,
            IllegalAccessException {
        List<Node> result = new ArrayList<Node>();
        // 1.将用户数据转化为List<Node>以及设置Node间关系
        List<Node> nodes = convetData2Node(datas);
        // 2.拿到跟节点
        List<Node> rootNodes = getRootNodes(nodes);
        // 3.依次展开排序把字节点添加到根节点
        for (Node node : rootNodes) {
            addNode(result, node, defaultExpandLevel, 1);
        }
        return result;

    }

    /**
     * 把数据转化为节点数据
     * 
     * @param datas
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected static <T> List<Node> convetData2Node(List<T> datas) throws IllegalArgumentException, IllegalAccessException {
        List<Node> nodes = new ArrayList<Node>();

        for (T t : datas) {
            int id = -1;
            int pId = -1;
            String lable = null;
            // 使用反射的方法获得类的名称
            Class<? extends Object> clazz = t.getClass();
            // 根据类得到声明的字段
            Field[] fields = clazz.getDeclaredFields();
            // 遍历所有的字段
            for (Field field : fields) {
                // 如果字段里有注解，说明得到的字段就存在
                if (field.getAnnotation(TreeNodeId.class) != null) {
                    field.setAccessible(true);
                    id = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodePid.class) != null) {
                    field.setAccessible(true);
                    pId = field.getInt(t);
                }
                if (field.getAnnotation(TreeNodeLabel.class) != null) {
                    field.setAccessible(true);
                    lable = (String) field.get(t);
                }
                //如果都遍历了，就不需要再次遍历了
                if (id != -1 && pId != -1 && lable != null) {
                    break;
                }
            }
            Node node = new Node(id, pId, lable);
            nodes.add(node);
        }

        /**
         * 使用选着排序，比较两个节点的关系
         */
        for (int i = 0; i < nodes.size(); i++) {
            Node node1 = nodes.get(i);
            // 从i+1处开始比较，使用了选择排序法
            for (int j = i + 1; j < nodes.size(); j++) {
                Node node2 = nodes.get(j);
                // 说明，node2是node1的父类
                if (node1.getpId() == node2.getId()) {
                    node2.getChildren().add(node1);
                    node1.setParent(node2);
                    // node1是node2的父类
                } else if (node2.getpId() == node1.getId()) {
                    node1.getChildren().add(node2);
                    node2.setParent(node1);
                }
            }
        }


        /**
         * 设置图片
         */
        for (Node node : nodes) {
            setIcon(node);
        }

        return nodes;
    }

    /**
     * 最主要根据节点是否有字节点和是否展开来判断显示什么样的图标 如果子节点是展开的，用- 否则有+
     * 
     * @param node
     */
    private static void setIcon(Node node) {;
        if (node.getChildren().size() > 0 && node.isExpend()) {
            node.setIcon(R.mipmap.ic_ex_s);
        } else if (node.getChildren().size() > 0 && !node.isExpend()) {
            node.setIcon(R.mipmap.ic_ex_m);
        } else {
            // 设置为-1时会在适配器里判断，如果为-1就隐藏
            node.setIcon(-1);
        }
    }

    /**
     * 把一个节点上的所有的内容都挂上去
     * 
     * @param nodes
     * @param node
     * @param defaultExpandLevel
     * @param currentLevel
     */
    protected static void addNode(List<Node> nodes, Node node, int defaultExpandLevel, int currentLevel) {
        //添加到集合里
        nodes.add(node);
        // 如果传进来的<currentLevel，说明在下一级，展开
        if (defaultExpandLevel >= currentLevel) {
            node.setExpend(true);
        }
        if (node.isLeaf()) {
            return;
        }
        // 使用递归，展开所有的子node
        for (int i = 0; i < node.getChildren().size(); i++) {
            addNode(nodes, node.getChildren().get(i), defaultExpandLevel, currentLevel + 1);
        }

    }

    /**
     * 判断是否是根节点,只要判断是否是isRoot();
     * 
     * @param nodes
     * @return
     */
    protected static List<Node> getRootNodes(List<Node> nodes) {
        List<Node> result = new ArrayList<Node>();
        for (Node node : nodes) {
            if (node.isRoot()) {
                result.add(node);
            }
        }
        return result;
    }

    /**
     * 过滤出所有可见的Node
     * 
     * @param mAllNodes
     * @return
     */
    public static List<Node> filterVisibleNode(List<Node> mAllNodes) {
        List<Node> result = new ArrayList<Node>();
        for (Node node : mAllNodes) {
            // 根节点是必须可见的，如果父亲节点是展开的话，这个节点当然是展开的
            if (node.isRoot() || node.isParentExptend()) {      
                setIcon(node);
                result.add(node);
            }
        }
        return result;
    }

}