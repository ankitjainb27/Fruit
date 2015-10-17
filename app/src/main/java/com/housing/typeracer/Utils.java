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

    public static int countWords(String s) {

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }
}
