package com.thcplusplus.covidyab;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

public class TopSnack {
    // Snackbar usually appears on bottom of the screen
    // this is a custom edit that puts Snackbar on top

    // SnackBar is a final class and final classes cannot be inherited, so its not extended from Snackbar

    public static final String TOP_SNACK_ERROR_TAG = "TOP_SNACK";
    private static int sGravity = Gravity.TOP, sMargin = 0;
    private static int sDuration = Snackbar.LENGTH_SHORT;

    private static void setGravity(int gravity){
        sGravity = gravity ==
                Gravity.BOTTOM || gravity == Gravity.CENTER
                    ? gravity
                    : Gravity.TOP;
    }

    private static void setMargin(int margin){
        sMargin = margin;
    }

    private static void setMessageDuration(int duration){
        sDuration = duration ==
                Snackbar.LENGTH_LONG || duration == Snackbar.LENGTH_INDEFINITE
                    ? duration
                    : Snackbar.LENGTH_SHORT;
    }

    public static void show(View root, String text){
        try {
            final Snackbar snack = Snackbar.make(root, text, sDuration);
            View view = snack.getView();
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) view.getLayoutParams();

            params.gravity = sGravity;
            if(sGravity == Gravity.TOP)
                params.topMargin =
                        sMargin == 0
                                ? (int) root.getResources().getDimension(R.dimen.snackbar_top_margin)
                                : sMargin;
            else if(sGravity == Gravity.BOTTOM)
                params.bottomMargin =
                        sMargin == 0
                                ? (int) root.getResources().getDimension(R.dimen.snackbar_top_margin)
                                : sMargin;
            // if gravity is center margin has not meaning
            view.setLayoutParams(params);
            snack.show();

        }
        catch (Exception ex){
            Log.e(TOP_SNACK_ERROR_TAG, ex.getMessage());
        }
    }

}
