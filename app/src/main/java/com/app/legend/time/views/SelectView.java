package com.app.legend.time.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.app.legend.time.R;

public class SelectView extends android.support.v7.widget.AppCompatTextView {

    private int order=-1;
    private Paint paint;

    public SelectView(Context context) {
        super(context);
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    public SelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint=new Paint();
        paint.setAntiAlias(true);
    }

    public SelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint();
        paint.setAntiAlias(true);

    }

    public void setOrder(int order) {
        this.order = order;

//        paint=new Paint();
//        paint.setAntiAlias(true);

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        int center=getWidth()/2;

        if (order>0){

//            paint.setColor(getResources().getColor(R.color.colorTeal));
//            paint.setAlpha(255);
            paint.setColor(getResources().getColor(R.color.colorDeepOrange));
            canvas.drawCircle(center,center,center,paint);
            paint.setColor(Color.WHITE);
            paint.setTextSize(50);
            paint.setTextAlign(Paint.Align.CENTER);

            String text=""+order;

            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
//            float height = fontMetrics.descent - fontMetrics.ascent;
//            float width = paint.measureText(text);
//
//            int baseline = (int) ((height - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top);

            int bas= (int) (getMeasuredHeight()/2-fontMetrics.top/2-fontMetrics.bottom/2);


            canvas.drawText(text,center,bas,paint);
//            setText(""+order);

        }else {
//            paint.setAlpha(0);
            paint.setColor(getResources().getColor(R.color.colorAlphaBlack));
            canvas.drawCircle(center,center,center,paint);

        }



    }
}
