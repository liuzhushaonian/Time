package com.app.legend.time.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.legend.time.R;

import java.lang.reflect.Field;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }

    protected int getStatusBarHeight(){

        try {
            Class<?> c=Class.forName("com.android.internal.R$dimen");
            Object object=c.newInstance();
            Field field=c.getField("status_bar_height");
            int x=Integer.parseInt(field.get(object).toString());
            return getResources().getDimensionPixelSize(x);

        }catch (Exception e){
            e.printStackTrace();
        }

        return 0;
    }

}
