package com.thcplusplus.covidyab;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public enum Severities {
    Light,
    Normal,
    Severe,
    UnCertain;

    @NonNull
    public String toString(CoordinatorLayout root){
        return root.getResources()
                .getStringArray(R.array.severities)[ordinal()];
    }

    @NonNull
    public String toString(RelativeLayout root){
        return root.getResources()
                .getStringArray(R.array.severities)[ordinal()];
    }
}
