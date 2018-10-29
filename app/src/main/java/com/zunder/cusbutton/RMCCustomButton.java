package com.zunder.cusbutton;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
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
import com.zunder.image.ImageUtils;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.centercontrol.CenterControlActivity;
import com.zunder.smart.activity.centercontrol.RmcEditeActivity;
import com.zunder.smart.dao.impl.factory.RmcBeanFactory;
import com.zunder.smart.dao.impl.factory.RoomFactory;
import com.zunder.smart.model.Room;
import com.zunder.smart.service.SendCMD;

public class RMCCustomButton extends RMCBaseView implements OnClickListener {
	private SmartImageView imageView;
	private TextView txt;
	private Activity context;

	private int Id;

	public int getBtnType() {
		return BtnType;
	}

	public void setBtnType(int btnType) {
		BtnType = btnType;
	}

	private int BtnType;

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}

	private int fontSize;

	@Override
	public int getId() {
		return Id;
	}
	@Override
	public void setId(int id) {
		Id = id;
	}

	public RMCCustomButton(Context context) {
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
        imageView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				RmcEditeActivity.startActivity(CenterControlActivity.getInstance(),getId());
				return false;
			}
		});
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SendCMD sendCMD= SendCMD.getInstance();
				RMCBean rmcBean= RmcBeanFactory.getInstance().getRMCById(getId());
				if(rmcBean!=null&&!TextUtils.isEmpty(rmcBean.getBtnAction())) {
					sendCMD.sendCmd(0, rmcBean.getBtnAction(), null);
					ToastUtils.ShowSuccess(CenterControlActivity.getInstance(), rmcBean.getBtnAction(), Toast.LENGTH_SHORT, true);
				}else{
					ToastUtils.ShowError(CenterControlActivity.getInstance(), "请关联设备", Toast.LENGTH_SHORT, true);

				}
				playKeytone();
			}
		});

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
//		imageView.setOnClickListener(l);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		// TODO Auto-generated method stub
//		imageView.setOnLongClickListener(l);
	}

	@Override
	public void setOnTouchListener(OnTouchListener l) {
		// TODO Auto-generated method stub
//		imageView.setOnTouchListener(l);
	}

	@Override
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
		if(!TextUtils.isEmpty(backgroundString)){
			try {
				if(backgroundString.contains("http")) {
					imageView.setImageUrl(backgroundString);
				}else {
					Bitmap bmp = ImageUtils.getInstance().decodeImage(backgroundString,5,5);
					imageView.setImageBitmap(bmp);
				}
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
}
