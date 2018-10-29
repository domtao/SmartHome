package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zunder.smart.R;

public class WarnDialog extends Dialog implements View.OnClickListener {

    private Activity context;
    private TextView msgTitle;
    private TextView msgTxt;
    private Button cancleBtn;



    public WarnDialog(Activity context,String title) {
        super(context, R.style.MyDialog);
        this.context = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_admin);
        msgTitle = (TextView)findViewById(R.id.msgTitle);
        msgTitle.setText(title);
        cancleBtn = (Button) findViewById(R.id.cancle_bt);
        msgTxt = (TextView)findViewById(R.id.msgTxt);
        cancleBtn
                .setOnClickListener(this);
    }

    public void init(String title, String message) {

    }
    public void setMessage(String msg){
        msgTxt.setText(msg);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.cancle_bt:
                if(sureListener!=null) {
                    sureListener.onCancle();
                }
                dismiss();
                break;
            default:
                break;
        }
    }
    public interface OnSureListener {
        public void onCancle();
    }

    private OnSureListener sureListener;

    public void setSureListener(OnSureListener sureListener) {
        this.sureListener = sureListener;
    }
}
