package com.jian.androidtree.tree;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jian.androidtree.R;

import java.util.Iterator;
import java.util.List;

/**
 * Created by jian on 2017/5/22.
 */

public class TreeAdapter<T extends RvTree> extends RecyclerView.Adapter<TreeAdapter.TreeViewHolder> {
    private static final int PADDING = 30;
    private Context mContext;
    private List<Node> mNodes;
    private TreeItemClickListener mListener;

    public TreeAdapter(Context context) {
        this.mContext = context;
    }

    public TreeAdapter(Context context, List<T> datas) {
        this.mContext = context;
        this.setNodes(datas);
    }

    public void setListener(TreeItemClickListener mListener) {
        this.mListener = mListener;
    }

    public void setNodes(List<T> datas) {
        List allNodes = TreeHelper.getSortedNodes(datas, 0);
        this.mNodes = TreeHelper.filterVisibleNode(allNodes);
    }

    public TreeAdapter<T>.TreeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(this.mContext);
        View view = inflater.inflate(R.layout.rv_item_tree, parent, false);
        return new TreeAdapter.TreeViewHolder(view);
    }

    public void onBindViewHolder(TreeAdapter.TreeViewHolder holder, int position) {
        Node node = (Node)this.mNodes.get(position);
        holder.setControl(node);
    }

    public int getItemCount() {
        return this.mNodes.size();
    }

    class TreeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView icon;
        private TextView title;
        private ImageView detail;
        private Node node;

        private TreeViewHolder(View itemView) {
            super(itemView);
            this.icon = (ImageView)itemView.findViewById(R.id.rv_item_tree_icon);
            this.title = (TextView)itemView.findViewById(R.id.rv_item_tree_title);
            this.detail = (ImageView)itemView.findViewById(R.id.tv_item_tree_detail);
            itemView.setOnClickListener(this);
            this.detail.setOnClickListener(this);
            itemView.setTag(Long.valueOf(System.currentTimeMillis()));
        }
        private Integer titleColor = Color.BLACK;
        private void setControl(Node node) {
            this.node = node;
            this.title.setText(node.getName());
            this.title.setPadding(node.getLevel() * 30, 3, 3, 3);
            this.detail.setImageResource(node.getResId());
            if(node.isLeaf()) {
                this.icon.setImageResource(0);
                if(node.getTitleColor()!=null){
                    titleColor = node.getTitleColor();
                }
                this.title.setTextColor(titleColor);

            } else {
                int rotateDegree = node.isExpand()?90:0;
                this.icon.setRotation(0.0F);
                this.icon.setRotation((float)rotateDegree);
                this.icon.setImageResource(R.drawable.ic_node_toggle);
            }
        }

        public void onClick(View view) {
//            if(view.getId() == R.id.tv_item_tree_detail) {
            if(this.node.isLeaf()){
                if(TreeAdapter.this.mListener != null) {
                    TreeAdapter.this.mListener.OnClick(this.node);

                }

            } else {
                if(this.node != null && !this.node.isLeaf()) {
                    long lastClickTime = ((Long)this.itemView.getTag()).longValue();
                    if(System.currentTimeMillis() - lastClickTime < 200L) {
                        return;
                    }
                    this.itemView.setTag(Long.valueOf(System.currentTimeMillis()));
                    int rotateDegree = this.node.isExpand()?-90:90;
                    this.icon.animate().setDuration(100L).rotationBy((float)rotateDegree).start();
                    boolean isExpand = this.node.isExpand();
                    this.node.setExpand(!isExpand);
                    if(!isExpand) {
                        TreeAdapter.this.notifyItemRangeInserted(this.getLayoutPosition() + 1, this.addChildNodes(this.node, this.getLayoutPosition() + 1));
                    } else {
                        TreeAdapter.this.notifyItemRangeRemoved(this.getLayoutPosition() + 1, this.removeChildNodes(this.node));
                    }
                }

            }
        }

        private int addChildNodes(Node n, int startIndex) {
            List childList = n.getChildren();
            int addChildCount = 0;
            Iterator var5 = childList.iterator();

            while(var5.hasNext()) {
                Node node = (Node)var5.next();
                TreeAdapter.this.mNodes.add(startIndex + addChildCount++, node);
                if(node.isExpand()) {
                    addChildCount += this.addChildNodes(node, startIndex + addChildCount);
                }
            }

            return addChildCount;
        }

        private int removeChildNodes(Node n) {
            if(n.isLeaf()) {
                return 0;
            } else {
                List childList = n.getChildren();
                int removeChildCount = childList.size();
                TreeAdapter.this.mNodes.removeAll(childList);
                Iterator var4 = childList.iterator();

                while(var4.hasNext()) {
                    Node node = (Node)var4.next();
                    if(node.isExpand()) {
                        removeChildCount += this.removeChildNodes(node);
                    }
                }

                return removeChildCount;
            }
        }
    }
}

