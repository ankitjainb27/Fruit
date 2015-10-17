package com.housing.typeracer;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements TextWatcher {

    TextView para;
    EditText input;
    String text;
    int counterText = 0;
    int flag = 0;
    int mistake;
    SpannableString styledString;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    ScrollView scrollView;
    ArrayList<Integer> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        para = (TextView) findViewById(R.id.tvPara);
        input = (EditText) findViewById(R.id.etInput);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        input.addTextChangedListener(this);
        text = para.getText().toString();
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        para.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                para.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                final Layout layout = para.getLayout();

                // Loop over all the lines and do whatever you need with
                // the width of the line
                for (int i = 1; i < layout.getLineCount(); i++) {
                    int end = layout.getLineEnd(i);
                    list.add(i);
                    Log.i("ankitscroll", String.valueOf(end));
                    if (i == 0) {
                        //   scrollView.scrollTo(0, para.getLineHeight());
                    }
                   /* SpannableString content = new SpannableString(para.getText().toString());
                    content.setSpan(new StyleSpan(Typeface.BOLD), 0, end, 0);
                    content.setSpan(new StyleSpan(Typeface.NORMAL), end, content.length(), 0);
                   */ //      para.setText(content);
                }
            }
        });
        styledString
                = new SpannableString(text);
        styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), beforeIndex(counterText), afterIndex(counterText), 0);
        para.setText(styledString);

    }

    private int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
//if(counterText )
        if (count > before) {
            if (flag == 0) {
                if (counterText < text.length() - 1) {
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
                            input.setText("");
                            counterText = counterText + start + 1;
                            Log.i("ankit_ON_counter1", String.valueOf(counterText));
                            styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.light_blue)), 0, text.length(), 0);
                            styledString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), counterText, afterIndex(counterText), 0);
                            Log.i("ankit_ON_after", String.valueOf(afterIndex(counterText)));
                            para.setText(styledString);
                        }
                    }
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

    private int afterIndex(int counterText) {
        int afterIndex = counterText;
        if (counterText < text.length() - 1) {
            if (!Character.toString(text.charAt(counterText)).equals(" ")) {
                for (int i = counterText; i < text.length(); i++) {
                    if (Character.toString(text.charAt(i)).equals(" ")) {
                        afterIndex = i;
                        break;
                    }
                }
            }
        }
        return afterIndex;
    }

    private int beforeIndex(int counterText) {
        int beforeIndex = counterText;

        if (!Character.toString(text.charAt(counterText)).equals(" ")) {
            if (counterText == 1) {
                beforeIndex = 0;
            } else {
                for (int i = counterText; i > 0; i--) {
                    if (Character.toString(text.charAt(i)).equals(" ")) {
                        beforeIndex = i;
                        break;
                    }
                }
            }
        }
        return beforeIndex;

    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
