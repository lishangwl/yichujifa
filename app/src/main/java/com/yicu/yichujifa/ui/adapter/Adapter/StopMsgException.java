package com.yicu.yichujifa.ui.adapter.Adapter;

/**
 * @author: WelliJohn
 * @time: 2018/8/6-17:12
 * @email: wellijohn1991@gmail.com
 * @desc: 递归跳出的异常
 */
public class StopMsgException extends RuntimeException {
    private Tree tree;

    public <T extends Tree> T getTree() {
        return (T) tree;
    }

    public StopMsgException setTree(Tree tree) {
        this.tree = tree;
        return this;
    }

    public StopMsgException(String message) {
        super(message);
    }


}