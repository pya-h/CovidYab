package com.thcplusplus.covidyab;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;

public enum Genders {
    Male,
    Female,
    Other;

    public static Genders convert(int index) {
        switch (index){
            case 1:
                return Female;
            case 2:
                return Other;
            default: // Case 0 or other out of range values
                return Male;
        }

    }

    @NonNull
    public String toString(CoordinatorLayout root) {
        return root.getResources()
                .getStringArray(R.array.genders)[ordinal()];
    }
}
