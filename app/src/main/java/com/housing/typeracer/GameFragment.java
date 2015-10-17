package com.housing.typeracer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputType;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.housing.typeracer.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GameFragment extends BaseFragment implements TextWatcher {

    private String myDeviceId;
    TextView para;
    EditText input;
    String text;
    int counterText = 0;
    int flag = 0;
    int mistake;
    SpannableString styledString;
    ScrollView scrollView;
    List<Integer> list = new ArrayList<>();
    ImageView[] IMGS;
    public static int VALUES;
    RelativeLayout progressImages;
    private GoogleApiClient mGoogleApiClient;
    private List<String> deviceRemoteIds;
    private boolean startCalled = false;
    private long startTime;
    private long hostEndTime;
    private long clientEndTime;
    private Map<String, Integer> wpmMap;

    public static GameFragment newInstance() {
        return new GameFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wpmMap = new HashMap<>();
        myDeviceId = ((MainActivity) getActivityReference()).myDeviceId;
        mGoogleApiClient = ((MainActivity) getActivityReference()).mGoogleApiClient;
        deviceRemoteIds = new ArrayList<>();
        for (String key : MainApplication.USER_REMOTE_ENDPOINT.keySet()) {
            if (!key.equalsIgnoreCase(myDeviceId)) {
                deviceRemoteIds.add(MainApplication.USER_REMOTE_ENDPOINT.get(key));
            }
        }
    }

    private void pushMyPosition(int pos) {
        if (!MainApplication.mIsHost) {
            try {
                byte[] data = Serializer.serialize(pos);
                Nearby.Connections.sendUnreliableMessage(mGoogleApiClient, ((MainActivity) getActivityReference()).mRemoteHostEndpoint, data);
            } catch (Exception p) {
                p.printStackTrace();
            }
        } else {
            MainApplication.USER_SCORE.put(myDeviceId, pos);
            try {
                byte[] data = Serializer.serialize(MainApplication.USER_SCORE);
                Nearby.Connections.sendUnreliableMessage(mGoogleApiClient, deviceRemoteIds, data);
            } catch (Exception p) {
            }
        }
    }

    private Map<String, Integer> getPlayersPosition() {
        clientEndTime = System.currentTimeMillis();
        printMap(MainApplication.USER_SCORE);
        return MainApplication.USER_SCORE;
    }

    private void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Log.v("map -", " " + pair.getKey() + " = " + pair.getValue());
            calculateClientWPM((Integer) pair.getValue());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.game, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        ((MainActivity) getActivityReference()).hideToolbar();
        para = (TextView) rootView.findViewById(R.id.tvPara);
        input = (EditText) rootView.findViewById(R.id.etInput);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        input.addTextChangedListener(this);
        text = para.getText().toString();
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        progressImages = (RelativeLayout) rootView.findViewById(R.id.progress_images);
        IMGS = new ImageView[VALUES];
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dpToPx(6));
        for (int i = 0; i < VALUES; i++) {
            IMGS[i] = (ImageView) progressImages.getChildAt(0);
            lp.width = 50 * (i + 1);
            IMGS[i].setLayoutParams(lp);
        }
        para.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                para.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                final Layout layout = para.getLayout();

                // Loop over all the lines and do whatever you need with
                // the width of the line
                for (int i = 0; i < layout.getLineCount() - 1; i++) {
                    int end = layout.getLineEnd(i);
                    Log.i("ankitp", String.valueOf(end));
                    list.add(end);
                }
            }
        });

        styledString
                = new SpannableString(text);
        styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, afterIndex(counterText), 0);
        para.setText(styledString);

    }


    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
 /*       Log.i("ankit_before_start", String.valueOf(start));
        Log.i("ankit_before_count", String.valueOf(count));
        Log.i("ankit_before_after", String.valueOf(count));
        if (start == 0 && count == 1) {
            counterText--;
            input.setBackgroundColor(Color.WHITE);
        }
*/
     /*   if (start < count) {
             *//*   Log.i("ankitbefore", String.valueOf(before));
                Log.i("ankitcount", String.valueOf(count));
                Log.i("ankittext", String.valueOf(counterText));
             *//*
            counterText++;

        } else if (start > count) {
             *//*   Log.i("ankitbefore", String.valueOf(before));
                Log.i("ankitcount", String.valueOf(count));
                Log.i("ankittext", String.valueOf(counterText));
             *//*
            counterText--;
        }
  */
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("ankit_ON_start", String.valueOf(start));
        Log.i("ankit_ON_count", String.valueOf(count));
        Log.i("ankit_ON_before", String.valueOf(before));
        Log.i("ankit_ON_S", s.toString());
        Log.i("ankit_ON_counter", String.valueOf(counterText));

        if (!startCalled) {
            startTime = System.currentTimeMillis();
            startCalled = true;
        }

        if (count > before) {
            if (flag == 0) {
                if (counterText + start < text.length()) {
                    if (!(Character.toString(text.charAt(counterText + start))).equals(Character.toString(s.charAt(start)))) {
                        if (!Character.toString(s.charAt(start)).equals(" ")) {
                            input.setBackgroundColor(Color.RED);
                            flag = 1;
                            mistake = start;
                        } else {
                            input.setText((input.getText().toString()).replace(" ", ""));
                        }
                    } else {
                        if ((Character.toString(s.charAt(start))).equals(" ")) {
                            // Log.i("ankitu", String.valueOf(progressStatus));
                            input.setText("");
                            counterText = counterText + start + 1;
                            if (list.contains(counterText)) {
                                scrollView.scrollTo(0, para.getLineHeight() * (list.indexOf(counterText) + 1));
                                //         Log.i("ankit", "came" + String.valueOf(counterText));
                            }
                            //    Log.i("ankit_ON_counter1", String.valueOf(counterText));
                            styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_blue)), 0, text.length(), 0);
                            styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), counterText, afterIndex(counterText), 0);
                            //    Log.i("ankit_ON_after", String.valueOf(afterIndex(counterText)));
                            para.setText(styledString);
                        }
                    }
                } else {
                    input.setText("");
                    hostEndTime = System.currentTimeMillis();
                    calculateHostWPM();
                }
            }
        } else if (count < before) {
            if (start == mistake) {
                input.setBackgroundColor(Color.WHITE);
                flag = 0;
            }
        }

       /* Log.i("ankit_ON_start", String.valueOf(start));
        Log.i("ankit_ON_count", String.valueOf(count));
        Log.i("ankit_ON_before", String.valueOf(before));
        Log.i("ankit_ON_COUNTER", String.valueOf(counterText));
        String last = input.getText().toString();
        if (last.length() > 0) {
            if (last.length() == 1) {
                last = last.substring(0);
            } else {
                last = last.substring(last.length() - 1);
            }
            Log.i("ankit_ON_last", last);
            if (!last.equals(Character.toString(text.charAt(counterText)))) {
                input.setBackgroundColor(Color.RED);
                flag = 1;
            } else {
                if (flag == 0) {
                    if (before < count) {
                        counterText++;
                    } else {
                        counterText--;
                    }
                    styledString.setSpan(new ForegroundColorSpan(Color.BLUE), beforeIndex(counterText), afterIndex(counterText), 0);
                    para.setText(styledString);
                    if (last.equals(" ")) {
                        input.setText("");
                    }
                }
            }
      *//*      Log.i("ankitbefore", String.valueOf(beforeIndex(counterText)));
            Log.i("ankitafter", String.valueOf(afterIndex(counterText)));
*//*
            if (before == 1 && count == 0) {
                input.setBackgroundColor(Color.WHITE);
            }
            Log.i("ankitontext", "came" + counterText);
        }
   */

    }

    private void calculateHostWPM() {
        int noOfWords = Utils.countWords(text);
        long timeToTypeInMillis = hostEndTime - startTime;
        long oneMinInMillis = 60000L;
        long wpm = (oneMinInMillis * noOfWords) / timeToTypeInMillis;
    }

    private void calculateClientWPM(int noOfWords) {
        long timeToTypeInMillis = clientEndTime - startTime;
        long oneMinInMillis = 60000L;
        long wpm = (oneMinInMillis * noOfWords) / timeToTypeInMillis;
    }

    private int afterIndex(int counterText) {
        int afterIndex = counterText;
        Log.i("ankitc", String.valueOf(counterText));
        if (counterText < text.length()) {
            if (!Character.toString(text.charAt(counterText)).equals(" ")) {
                for (int i = counterText; i < text.length(); i++) {
                    Log.i("ankitc", String.valueOf(i));
                    if (Character.toString(text.charAt(i)).equals(" ")) {
                        afterIndex = i;
                        break;
                    } else if (i == text.length() - 1) {
                        afterIndex = i + 1;
                        break;
                    }
                }
            }
        }
        return afterIndex;
    }


    @Override
    public void afterTextChanged(Editable s) {
    }
}
