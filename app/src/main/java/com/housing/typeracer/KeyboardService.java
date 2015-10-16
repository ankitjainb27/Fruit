package com.housing.typeracer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by gayathri_nair on 16/10/15.
 */
public class KeyboardService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        InputMethodManager imm = (InputMethodManager) MainApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
//            writeToLog("Software Keyboard was shown");
        } else {
//            writeToLog("Software Keyboard was not shown");
        }

        return null;
    }
}
