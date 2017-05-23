package com.jian.androidtree.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jian on 2017/5/22.
 */

public class Node {
    private int id;
    private int pId = 0;
    private String name;
    private int level;
    private boolean isExpand = false;
    private int icon;
    private List<Node> children = new ArrayList();
    private Node parent;
    private int resId;

    public Node() {
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return this.pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return this.isExpand;
    }

    public List<Node> getChildren() {
        return this.children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return this.parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getResId() {
        return this.resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean isParentExpand() {
        return this.parent == null?false:this.parent.isExpand();
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public int getLevel() {
        return this.parent == null?0:this.parent.getLevel() + 1;
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }
}
