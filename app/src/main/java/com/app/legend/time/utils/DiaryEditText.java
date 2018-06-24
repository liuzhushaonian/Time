package com.app.legend.time.utils;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class DiaryEditText extends AppCompatEditText {

    private float startY;
    private float startX;
    private float selectionStart;


    public DiaryEditText(Context context) {
        super(context);
    }

    public DiaryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DiaryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = event.getRawY();
                startX = event.getRawX();
                selectionStart = getSelectionStart();
                break;
            case MotionEvent.ACTION_MOVE:

                setTextIsSelectable(false);
                break;
            case MotionEvent.ACTION_UP:
                float endY = event.getRawY();
                float endX = event.getRawX();

                if (Math.abs(endY - startY) > 10 || Math.abs(endX - startX) > 10) {

                    setTextIsSelectable(false);

                    return true;
                }
                break;
            default:
                break;
        }

        return super.dispatchTouchEvent(event);
    }
}
