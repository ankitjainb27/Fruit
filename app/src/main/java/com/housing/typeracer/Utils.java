package com.housing.typeracer;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class Utils {

    public static float convertDpToPixel(Context context, float dp) {
        Context localContext = context;
        if (null == localContext) {
            localContext = MainApplication.getContext();
        }
        DisplayMetrics displayMetrics = localContext.getResources().getDisplayMetrics();
        return dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelToDp(Context context, float px) {
        Context localContext = context;
        if (null == localContext) {
            localContext = MainApplication.getContext();
        }
        DisplayMetrics displayMetrics = localContext.getResources().getDisplayMetrics();
        return px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }
}
