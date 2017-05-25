package com.jian.androidtree.tree;

/**
 * Created by jian on 2017/5/22.
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TreeHelper {
    public TreeHelper() {
    }

    public static <T extends RvTree> List<Node> getSortedNodes(List<T> datas, int defaultExpandLevel) {
        ArrayList result = new ArrayList();
        List nodes = convertData2Node(datas);
        List rootNodes = getRootNodes(nodes);
        Iterator var5 = rootNodes.iterator();

        while(var5.hasNext()) {
            Node node = (Node)var5.next();
            addNode(result, node, defaultExpandLevel, 1);
        }

        return result;
    }

    public static List<Node> filterVisibleNode(List<Node> nodes) {
        ArrayList result = new ArrayList();
        Iterator var2 = nodes.iterator();

        while(true) {
            Node node;
            do {
                if(!var2.hasNext()) {
                    return result;
                }

                node = (Node)var2.next();
            } while(!node.isRoot() && !node.isParentExpand());

            result.add(node);
        }
    }

    private static <T extends RvTree> List<Node> convertData2Node(List<T> datas) {
        ArrayList list = new ArrayList();
        Iterator var2 = datas.iterator();

        while(var2.hasNext()) {
            RvTree item = (RvTree)var2.next();
            Node node = new Node();
            node.setId(item.getId());
            node.setpId(item.getPid());
            node.setLevel(item.getLevel());
            node.setName(item.getTitle());
            node.setTitleColor(item.getTitleColor());
            node.setResId(item.getImageResId());
            list.add(node);
        }

        sortNodeList(list);
        return list;
    }

    private static void sortNodeList(List<Node> list) {
        for(int i = 0; i < list.size(); ++i) {
            Node n = (Node)list.get(i);

            for(int j = i + 1; j < list.size(); ++j) {
                Node m = (Node)list.get(j);
                if(n.getpId() == m.getId()) {
                    m.getChildren().add(n);
                    n.setParent(m);
                } else if(m.getpId() == n.getId()) {
                    n.getChildren().add(m);
                    m.setParent(n);
                }
            }
        }

    }

    private static List<Node> getRootNodes(List<Node> nodes) {
        ArrayList root = new ArrayList();
        Iterator var2 = nodes.iterator();

        while(var2.hasNext()) {
            Node node = (Node)var2.next();
            if(node.isRoot()) {
                root.add(node);
            }
        }

        return root;
    }

    private static void addNode(List<Node> nodes, Node node, int defaultExpandLeval, int currentLevel) {
        nodes.add(node);
        if(defaultExpandLeval == currentLevel) {
            node.setExpand(true);
        }

        if(!node.isLeaf()) {
            for(int i = 0; i < node.getChildren().size(); ++i) {
                addNode(nodes, (Node)node.getChildren().get(i), defaultExpandLeval, currentLevel + 1);
            }

        }
    }
}
