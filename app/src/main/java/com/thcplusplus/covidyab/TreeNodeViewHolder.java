package com.thcplusplus.covidyab;
import android.util.Log;
import android.view.View;

import com.thcplusplus.covidyab.databinding.TreeNodeBinding;

public class TreeNodeViewHolder {

    public TreeNodeBinding mBinding;

    private TreeNodeType mNodeType = TreeNodeType.Field;
    private int mNodeLevel;

    public void setNodeLevel(int level){
        this.mNodeLevel = level;
    }
    TreeNodeViewHolder(View view, TreeQuizFragment containerFragment) {
        this.mBinding = TreeNodeBinding.bind(view);

        mBinding.nodeItemFieldButton.setOnClickListener(v -> {
            if(this.mNodeLevel == containerFragment.getCurrentLevel()) {
                if (mNodeType == TreeNodeType.YesAnswer || mNodeType == TreeNodeType.NoAnswer) {
                    containerFragment.surveyTree(mNodeType == TreeNodeType.YesAnswer ? 1.0f : 0.0f);
                }
            }
            Log.e("LEVEL", String.valueOf(mNodeLevel));

        });
    }

    public void setNodeType(TreeNodeType nodeType){
        this.mNodeType = nodeType;

    }
}
