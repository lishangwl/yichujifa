package com.yicu.yichujifa.LayoutHierarchy;

import android.view.accessibility.AccessibilityNodeInfo;

import com.yicu.yichujifa.ui.adapter.Adapter.Tree;

import java.util.ArrayList;
import java.util.List;

public  class Node implements Tree<Node> {
        AccessibilityNodeInfo node;
        List<Node> nodes;
        Node parent;
        int index = 0;
        public AccessibilityNodeInfo getNode() {
            return node;
        }

        public Node setParent(Node parent) {
            this.parent = parent;
            return this;
        }

        public int getLevelNumber(){
            if (getParent() != null){
                return getParent().getLevelNumber() + 1;
            }
            return 0;
        }

        private Node getParent() {
            return parent;
        }

        public Node(AccessibilityNodeInfo node){
            this(node,0);
        }

        public Node(AccessibilityNodeInfo node,int index){
            this.node = node;
            this.index = index;
            nodes = new ArrayList<>();
            if (node == null){
                return;
            }
            for (int i = 0 ;i<node.getChildCount();i++){
                nodes.add(new Node(node.getChild(i),i).setParent(this));
            }
        }

        boolean isExpand = false;
        @Override
        public int getLevel() {
            return getChilds().size() == 0?0:1;
        }

        @Override
        public List<Node> getChilds() {
            return nodes;
        }

        public void setExpand(boolean expand) {
            isExpand = expand;
        }

        @Override
        public boolean isExpand() {
            return isExpand;
        }


        public int getIndex() {
            return index;
        }
    }