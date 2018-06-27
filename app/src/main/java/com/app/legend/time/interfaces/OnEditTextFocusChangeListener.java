package com.app.legend.time.interfaces;


import android.widget.EditText;

import com.app.legend.time.bean.DiaryInfo;

public interface OnEditTextFocusChangeListener {

    void changeFocus(EditText view, boolean hasFocus, DiaryInfo diaryInfo);

}
