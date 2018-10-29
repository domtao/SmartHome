package com.zunder.smart.langvage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zunder.smart.R;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.activity.StartActivity;

import java.util.Locale;

public class LangvageActivity extends Activity {
    private LinearLayout s_langvage, f_langvage,e_langvage;
    private Context activity;
    TextView backView;
    TextView ServicceTxt;
    ImageView f_img,e_img,s_img;
    public static void startActivity(Activity activity) {
        Intent intent = new Intent(activity, LangvageActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laungvage_set);
        activity = this;
        backView = (TextView) findViewById(R.id.backTxt);
        ServicceTxt = (TextView) findViewById(R.id.serviceTxt);
        f_img=(ImageView) findViewById(R.id.f_img);
        e_img=(ImageView)findViewById(R.id.e_img);
        s_img=(ImageView)findViewById(R.id.s_img);
        backView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        s_langvage = (LinearLayout) findViewById(R.id.s_langvage);
        s_langvage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LanguageUtil.getInstance().changeAppLanguage(activity, Locale.SIMPLIFIED_CHINESE);
                restartApplication();
            }
        });
        f_langvage = (LinearLayout) findViewById(R.id.f_langvage);
        f_langvage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LanguageUtil.getInstance().changeAppLanguage(activity, Locale.TAIWAN);
                restartApplication();
            }
        });

        e_langvage = (LinearLayout) findViewById(R.id.e_langvage);
        e_langvage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                LanguageUtil.getInstance().changeAppLanguage(activity, Locale.ENGLISH);
                restartApplication();
            }
        });
        Locale locale= LanguageUtil.getInstance().getAppLocale(activity);
        if(locale.getCountry().equals("CN")){
            Glide.with(activity)
                    .load(R.mipmap.icon_success)
                    .placeholder(R.mipmap.icon_success)
                    .into(s_img);
        }else if(locale.getCountry().equals("TW")){
            Glide.with(activity)
                    .load(R.mipmap.icon_success)
                    .placeholder(R.mipmap.icon_success)
                    .into(f_img);
        }else{
            Glide.with(activity)
                    .load(R.mipmap.icon_success)
                    .placeholder(R.mipmap.icon_success)
                    .into(e_img);
        }
    }

    private void restartApplication() {
        //切换语言信息，需要重启 Activity 才能实现
        MainActivity.getInstance().stopActivityServer();
        finish();
        overridePendingTransition(0, 0);
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}