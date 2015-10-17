package com.housing.typeracer.fonts;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by gayathri_nair on 17/10/15.
 */
public class TextBold extends CustomTextView {

    public static final String FONT_NAME = "fonts/SanFranciscoText-Bold.otf";


    public TextBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public TextBold(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public TextBold(Context context) {
        super(context);
    }

    @Override
    protected String getFontName() {
        return FONT_NAME;
    }
}
