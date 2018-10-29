package com.zunder.smart.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.zunder.scrollview.ProgressWheel;
import com.zunder.smart.R;

public class ProgressAlert extends Dialog {
    private Activity mContext;

    TextView tipTxt;
    Button fresh;
    ProgressWheel progressWheel;


    ProgressAlertFace progressAlertFace;
    public ProgressAlert(Activity context) {
        super(context, R.style.MyDialog);
        this.mContext = context;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.progress_dialog);
        findView();
    }

    private void findView() {
        progressWheel=(ProgressWheel)findViewById(R.id.proGress);
        fresh = (Button) findViewById(R.id.refresh);
        tipTxt = (TextView) findViewById(R.id.tipTxt);
        fresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(progressAlertFace!=null){
                    progressWheel.stopSpinning();
                    progressAlertFace.close();
                }
            }
        });
    }

    public void setProgressAlertFace(ProgressAlertFace progressAlertFace) {
        this.progressAlertFace = progressAlertFace;
    }
    public void showAlert(String msg){
        progressWheel.resetCount();
        progressWheel.setText(msg+"…");
        progressWheel.startSpinning();
    }

    public interface ProgressAlertFace {
        public void close();
    }


}

