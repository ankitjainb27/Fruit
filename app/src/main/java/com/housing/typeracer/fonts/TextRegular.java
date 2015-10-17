package com.housing.typeracer.fonts;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class TextRegular extends CustomTextView {

    public static final String FONT_NAME = "fonts/SanFranciscoText-Regular.otf";


    public TextRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextRegular(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public TextRegular(Context context) {
        super(context);
    }

    @Override
    protected String getFontName() {
        return FONT_NAME;
    }
}