package esqeee.xieqing.com.eeeeee.doAction;

import esqeee.xieqing.com.eeeeee.widget.TreeNodeId;
import esqeee.xieqing.com.eeeeee.widget.TreeNodeLabel;
import esqeee.xieqing.com.eeeeee.widget.TreeNodePid;

public class NodeBean {
    @TreeNodeId
    private int nodeId;

    @TreeNodePid
    private int nodePid;

    @TreeNodeLabel
    private String nodeLable;

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public void setNodeLable(String nodeLable) {
        this.nodeLable = nodeLable;
    }

    public void setNodePid(int nodePid) {
        this.nodePid = nodePid;
    }

    public int getNodeId() {
        return nodeId;
    }

    public String getNodeLable() {
        return nodeLable;
    }

    public int getNodePid() {
        return nodePid;
    }
}
