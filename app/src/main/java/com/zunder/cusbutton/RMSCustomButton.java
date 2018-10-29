package com.zunder.cusbutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.base.RMCBaseView;
import com.zunder.base.RMSBaseView;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.CenterControlActivity;
import com.zunder.smart.activity.centercontrol.RmcEditeActivity;
import com.zunder.smart.activity.centercontrol.edite.TouchPanelActivity;
import com.zunder.smart.dao.impl.factory.RmcBeanFactory;
import com.zunder.smart.dao.impl.factory.RmsTabFactory;
import com.zunder.smart.service.SendCMD;

public class RMSCustomButton extends RMSBaseView implements OnClickListener {
	private SmartImageView imageView;
	private TextView txt;
	private Activity context;



	public RMSCustomButton(Context context) {
		super(context);
		context=context;
		init();
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		LayoutInflater.from(getContext()).inflate(R.layout.cunstom_button, this);
        imageView=(SmartImageView) findViewById(R.id.imageIco);
        txt=(TextView)findViewById(R.id.txt);
        setOnClickListener(this);


	}
	public void initParams(){
		setText(getBtnName());
		setBackgroundFromProperties(getBtnImage());
		setParams(getBtnWidth(),getBtnHeight(),getBtnX(),getBtnY());
		setTabViewTag(getRoomId());
		setTextColor(getBtnColor());
		setTextSize(getBtnSize());
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
	}

	@Override
	protected int getTitleResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
	}


	public void setText(CharSequence text){
		txt.setText(text);
	}
	
	public CharSequence getText(){
		return txt.getText();
	}
	
	public void setTextSize(float size){
		txt.setTextSize(size);
	}

	public float getTextSize(){
		return txt.getTextSize();
	}
	
	@Override
	public void setBackgroundResource(int resid) {
		imageView.setBackgroundResource(resid);
	}
	
	@Override
	public void setBackgroundDrawable(Drawable background) {
		// TODO Auto-generated method stub
		imageView.setBackgroundDrawable(background);
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		// TODO Auto-generated method stub
		imageView.setOnClickListener(l);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		// TODO Auto-generated method stub
		imageView.setOnLongClickListener(l);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		// TODO Auto-generated method stub
		imageView.setOnTouchListener(l);
	}

	public void setTextColor(String color) {
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(color)){
			txt.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.black));
		}else{
			txt.setTextColor(Color.parseColor("#"+color));
		}
	}

	@Override
	public void setTypeface(Typeface tf) {
		// TODO Auto-generated method stub
		txt.setTypeface(tf);
	}
	public void setBackgroundFromProperties(String backgroundString) {
		setBtnImage(backgroundString);
		if(!TextUtils.isEmpty(backgroundString)){
			try {
				imageView.setImageUrl(backgroundString);
				int typeId=getBtnType();
				if(typeId==1) {
					imageView.setImageResource(R.drawable.custom_button_keypress1);
				}
//				imageView.setBackgroundResource(R.drawable.custom_other_keypress);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			int typeId=getBtnType();
			if(typeId==1) {
				imageView.setImageResource(R.drawable.custom_button_keypress1);
			}else if(typeId==2){
				imageView.setImageResource(R.drawable.custom_button_keypress2);
			}else if(typeId==3){
				imageView.setImageResource(R.drawable.custom_button_keypress3);
			}else{
				imageView.setImageResource(R.drawable.custom_button_keypress4);
			}
		}
	}

	private void playKeytone() {
		MediaPlayer.create(MyApplication.getInstance(), R.raw.ping_short).start();
	}

	@Override
	public View copy() {
		RMSCustomButton cBaseView =new RMSCustomButton(TouchPanelActivity.getInstance());
		cBaseView.setBtnName(getBtnName());
		cBaseView.setBtnWidth(getWidth());
		cBaseView.setBtnHeight(getHeight());
		cBaseView.setBtnX(getBtnX());
		cBaseView.setBtnY(getBtnY());
		cBaseView.setBtnSize(16);
		cBaseView.setBtnColor("000000");
		cBaseView.setBtnImage(getBtnImage());
		cBaseView.initParams();
		return cBaseView;
	}
	@Override
	public String toString() {
		return "自定义按钮ID: "
				+ getId() + "\r\n按钮大小:" + getWidth()+"*"+getHeight()+"\r\n坐标X:"+getX()+"\tY:"+getY();
	}
}
