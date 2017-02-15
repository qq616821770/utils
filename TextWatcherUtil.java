package com.pingan.pinganwifiboss.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by chenlong on 15/11/24.
 */
public class TextWatcherUtil {

    /**
     * 银行卡输入格式转换
     *
     * @param s
     * @param count
     * @param editText
     */
    public static void resetBankCardNumber(CharSequence s, int count, EditText editText) {
        int length = s.length();
        if (count > 0) {
            if (length == 4 || length == 9 || length == 14 || length == 19 || length == 24) {
                editText.setText(s + " ");
                editText.setSelection(length + 1);
            }
        } else if (count == 0) {
            if (length > 0 && (length == 4 || length == 9 || length == 14 || length == 19 || length == 24)) {
                editText.setText(s.subSequence(0, length - 1));
                editText.setSelection(length - 1);
            }
        }
    }

    /**
     * 银行卡号TextWatcher
     */
    public static class BankCardNumTW implements TextWatcher {
        private EditText edtCardNum;

        public BankCardNumTW(EditText edt) {
            this.edtCardNum = edt;
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            resetBankCardNumber(s, count, edtCardNum);
        }
    }

}
