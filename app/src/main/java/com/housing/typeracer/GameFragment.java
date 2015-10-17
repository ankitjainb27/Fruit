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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.Nearby;
import com.housing.typeracer.fragments.BaseFragment;
import com.housing.typeracer.fragments.LaunchFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameFragment extends BaseFragment implements TextWatcher, View.OnClickListener {

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
    public static int VALUES = 0;
    RelativeLayout progressImages;
    int progressMy = 0;
    Thread t;
    RelativeLayout.LayoutParams lp, lp1;
    int width;
    private GoogleApiClient mGoogleApiClient;
    private List<String> deviceRemoteIds;
    Map<String, Integer> map;
    Set<String> keys;
    ProgressBar progressBar;

    private boolean startCalled = false;
    private long startTime;
    private long hostEndTime;
    private long clientEndTime;

    private Map<String, Integer> wpmMap;
    TextView position;
    ImageView profile;
    private TextView exit;


    public static GameFragment newInstance() {
        return new GameFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.wpmMap = new HashMap<>();
        myDeviceId = ((MainActivity) getActivityReference()).myDeviceId;
        mGoogleApiClient = ((MainActivity) getActivityReference()).mGoogleApiClient;
        deviceRemoteIds = new ArrayList<>();
        for (String key : MainApplication.USER_REMOTE_ENDPOINT.keySet()) {
            if (!key.equalsIgnoreCase(myDeviceId)) {
                deviceRemoteIds.add(MainApplication.USER_REMOTE_ENDPOINT.get(key));
            }
        }
        map = getPlayersPosition();
        keys = map.keySet();
        map = sortByComparator(map);
        VALUES = map.size();
        Log.i("ankitv", String.valueOf(VALUES));
        // printMap(map);

    }

    public static void printMap1(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("[Key] : " + entry.getKey()
                    + " [Value] : " + entry.getValue());
        }
    }

    private boolean checkIfFinished(Map<String, Integer> map) {
        int finished = 0;
        for (String key : map.keySet()) {
            if (map.get(key) >= text.length()) {
                finished++;
            }
            int wpm = calculateClientWPM(map.get(key));
        }
        if (finished == VALUES) {
            return true;
        }
        return false;
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap) {

        // Convert Map to List
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // Convert sorted map back to a Map
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<String, Integer> entry = it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
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
                p.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.interrupt();

    }

    private Map<String, Integer> getPlayersPosition() {
        clientEndTime = System.currentTimeMillis();
        return MainApplication.USER_SCORE;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.game, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {
        exit = (TextView) rootView.findViewById(R.id.exit);
        exit.setOnClickListener(this);
        ((MainActivity) getActivityReference()).hideToolbar();
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar2);
        para = (TextView) rootView.findViewById(R.id.tvPara);
        input = (EditText) rootView.findViewById(R.id.etInput);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        profile = (ImageView) rootView.findViewById(R.id.profile);
        final ImageView imageView = (ImageView) rootView.findViewById(R.id.image_view);
        imageView.post(new Runnable() {
                           public void run() {
                               width = imageView.getMeasuredWidth();
                           }
                       }
        );
        width = imageView.getMeasuredWidth();
        position = (TextView) rootView.findViewById(R.id.position);
        position.setText("Position " + 0 + "/" + VALUES);
        input.addTextChangedListener(this);
        text = para.getText().toString();
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        IMGS = new ImageView[VALUES];
        if (VALUES == 2) {
            IMGS[0] = (ImageView) rootView.findViewById(R.id.image_view1);
            IMGS[1] = (ImageView) rootView.findViewById(R.id.image_view2);
        } else if (VALUES == 3) {
            IMGS[0] = (ImageView) rootView.findViewById(R.id.image_view1);
            IMGS[1] = (ImageView) rootView.findViewById(R.id.image_view2);
            IMGS[2] = (ImageView) rootView.findViewById(R.id.image_view3);
        }


        lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dpToPx(6));
        lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
       /* for (int i = 0; i < VALUES; i++) {
            IMGS[i] = (ImageView) progressImages.getChildAt(0);
        }
       */
        para.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                para.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                final Layout layout = para.getLayout();

                // Loop over all the lines and do whatever you need with
                // the width of the line
                for (int i = 0; i < layout.getLineCount() - 1; i++) {
                    int end = layout.getLineEnd(i);
                    list.add(end);
                }
            }
        });

        styledString
                = new SpannableString(text);
        styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), 0, afterIndex(counterText), 0);
        para.setText(styledString);
        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(2000);
                        getActivityReference().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pushMyPosition(progressMy);
                                updateProgressBar();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
    }


    private void updateProgressBar() {
        map = getPlayersPosition();
        printMap1(map);
        boolean finished = checkIfFinished(map);
        Log.v("finished = " + finished, "");
        if (finished) {
            getFragmentController().performOperation(Controller.OPEN_LEADERBOARD, null);
        }
        int pos = new ArrayList<String>(keys).indexOf(myDeviceId);
        position.setText("Position " + pos + "/" + VALUES);

        printMap1(map);
        for (int i = 0; i < VALUES; i++) {
            width = (width * map.get(new ArrayList<>(keys).get(i))) / text.length();
            progressBar.setProgress(width);

        }
        /*
        for (String key : keys) {
            lp.width = (width * map.get(key)) / text.length();
            Log.i("width", String.valueOf(lp.width));
            IMGS[i].setLayoutParams(lp);
            if (lp.width < (width - dpToPx(16))) {
                lp1.setMargins(dpToPx(16) + lp.width, dpToPx(30), 0, 0);
                profile.setLayoutParams(lp1);
            }
            i++;
        */   /* if (i == 0) {
                IMGS[i].setImageResource(R.drawable.green);
              *//*  IMGS[i].setImageDrawable(getResources().getDrawable(R.drawable.green));
              *//*  // IMGS[i].setBackground(getResources().getDrawable(R.drawable.green));
            } else if (i == 1) {
                IMGS[i].setImageResource(R.drawable.pink);
                //     IMGS[i].setImageDrawable(getResources().getDrawable(R.drawable.pink));

                //   IMGS[i].setBackground(getResources().getDrawable(R.drawable.pink));
            }
           */
            /*}*/
    }

      /*  for (int i = 0; i < VALUES; i++) {
      *//*  for (String key : keys) {
      *//*      *//*if (i < VALUES) {
            *//*
            lp.width = (width * map.get(new ArrayList<>(keys).get(i))) / text.length();
            Log.i("width", String.valueOf(lp.width));
            IMGS[i].setLayoutParams(lp);
            if (i == 0) {
                IMGS[i].setImageResource(R.drawable.green);
              *//*  IMGS[i].setImageDrawable(getResources().getDrawable(R.drawable.green));
              *//*  // IMGS[i].setBackground(getResources().getDrawable(R.drawable.green));
            } else if (i == 1) {
                IMGS[i].setImageResource(R.drawable.pink);
                //     IMGS[i].setImageDrawable(getResources().getDrawable(R.drawable.pink));

                //   IMGS[i].setBackground(getResources().getDrawable(R.drawable.pink));
            }
            *//*}*//*
        }
      *//*  for (int i = 0; i < VALUES; i++) {

            lp.width = (width * map.get(keys.)) / text.length();
            Log.i("width", String.valueOf(lp.width));
            IMGS[i].setLayoutParams(lp);

            //  IMGS[i].setBackground();
        }
    */


    private String getKey(Integer value) {
        for (String key : map.keySet()) {
            if (map.get(key).equals(value)) {
                return key; //return the first found
            }
        }
        return null;
    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getActivityReference().getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
       /* DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
   */
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
     /*   Log.i("ankit_ON_start", String.valueOf(start));
        Log.i("ankit_ON_count", String.valueOf(count));
        Log.i("ankit_ON_before", String.valueOf(before));
        Log.i("ankit_ON_S", s.toString());
        Log.i("ankit_ON_counter", String.valueOf(counterText));
*/

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
                        progressMy++;
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
                    progressMy++;

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
        int noOfChars = progressMy;
        int noOfWords = noOfChars / 5;
        long timeToTypeInMillis = hostEndTime - startTime;
        long oneMinInMillis = 60000L;
        long wpm = (oneMinInMillis * noOfWords) / timeToTypeInMillis;
        Log.v("hola host wpm =", " = " + wpm);
    }

    private int calculateClientWPM(int charCount) {
        long timeToTypeInMillis = clientEndTime - startTime;
        long oneMinInMillis = 60000L;
        long wpm = (oneMinInMillis * charCount) / (5 * timeToTypeInMillis);
        Log.v("hola client wpm =", " = " + wpm);
        return (int) wpm;
    }

    private int afterIndex(int counterText) {
        int afterIndex = counterText;
        //  Log.i("ankitc", String.valueOf(counterText));
        if (counterText < text.length()) {
            if (!Character.toString(text.charAt(counterText)).equals(" ")) {
                for (int i = counterText; i < text.length(); i++) {
                    //       Log.i("ankitc", String.valueOf(i));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                getFragmentController().clearBackStack(false, LaunchFragment.class.getCanonicalName());
                break;
        }
    }


}

