package com.zunder.smart.activity.centercontrol;

import android.app.Activity;
import android.app.Application;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.EditText;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.cusbutton.RMCBean;
import com.zunder.image.ImageUtils;
import com.zunder.image.view.SmartImageView;
import com.zunder.smart.MyApplication;
import com.zunder.smart.R;
import com.zunder.smart.activity.popu.dialog.ActionViewWindow;
import com.zunder.smart.activity.popu.dialog.DeviceTypeViewWindow;
import com.zunder.smart.activity.popu.dialog.IOViewWindow;
import com.zunder.smart.activity.popu.dialog.ProductViewWindow;
import com.zunder.smart.activity.popu.dialog.RmcButtonPopupWindow;
import com.zunder.smart.activity.popu.dialog.RoomViewWindow;
import com.zunder.smart.dao.impl.IWidgetDAO;
import com.zunder.smart.dao.impl.factory.DeviceFactory;
import com.zunder.smart.dao.impl.factory.RmcBeanFactory;
import com.zunder.smart.dialog.TimeAlert;
import com.zunder.smart.model.Device;
import com.zunder.smart.utils.FileUtil;
import com.zunder.smart.utils.ListNumBer;


public class RmcEditeActivity extends Activity implements OnClickListener{

	private TextView backTxt;
	private TextView editeTxt;
	private TextView titleTxt;

	private EditText buttonName;
	private EditText txtSize;

	private RelativeLayout colorLayout;
	private TextView colorTxt;
	private RelativeLayout imageLayout;
	private SmartImageView imageIco;
	private RelativeLayout typeLayout;
	private TextView typeTxt;
	private RelativeLayout actionLayout;
	private TextView actionMsg;
	private TextView actionTxt;
	private RMCBean rmcBean;
	private String imagePath="";

	TimeAlert alert;
	public  Activity activity;
	private static int Id;
	private int typeId = -1;
	RoomViewWindow roomViewWindow;
	DeviceTypeViewWindow deviceTypeViewWindow;
	ProductViewWindow productViewWindow;
	IOViewWindow ioViewWindow;
	private String colorStr="000000";
	private static final int REQUEST_CODE_IMAGE_CAMERA = 1;
	private static final int REQUEST_CODE_IMAGE_OP = 2;
	private static final int REQUEST_CODE_OP = 3;
	public static void startActivity(Activity activity,int _Id) {
		Id=_Id;
		Intent intent = new Intent(activity, RmcEditeActivity.class);
		activity.startActivityForResult(intent,100);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = this;
		setContentView(R.layout.activity_rmc_edite);
		rmcBean=RmcBeanFactory.getInstance().getRMCById(Id);
		titleTxt=(TextView)findViewById(R.id.titleTxt);
		backTxt = (TextView) findViewById(R.id.backTxt);
		editeTxt = (TextView) findViewById(R.id.editeTxt);

		buttonName = (EditText) findViewById(R.id.buttonName);
		txtSize= (EditText) findViewById(R.id.txtSize);
		colorTxt = (TextView) findViewById(R.id.colorTxt);
		imageIco = (SmartImageView) findViewById(R.id.imageIco);
		typeTxt = (TextView) findViewById(R.id.typeTxt);
		actionMsg = (TextView) findViewById(R.id.actionMsg);
		actionTxt = (TextView) findViewById(R.id.actionTxt);

		colorLayout = (RelativeLayout) findViewById(R.id.colorLayout);
		imageLayout = (RelativeLayout) findViewById(R.id.imageLayout);
		actionLayout = (RelativeLayout) findViewById(R.id.actionLayout);
		typeLayout = (RelativeLayout) findViewById(R.id.typeLayout);

		backTxt.setOnClickListener(this);
		editeTxt.setOnClickListener(this);
		colorLayout.setOnClickListener(this);
		imageLayout.setOnClickListener(this);
		actionLayout.setOnClickListener(this);
		typeLayout.setOnClickListener(this);
		alert = new TimeAlert(activity);
		alert.setSureListener(new TimeAlert.OnSureListener() {
			@Override
			public void onCancle() {
				// TODO Auto-generated method stub
				alert.diss();
			}
		});
		rmcBean=RmcBeanFactory.getInstance().getRMCById(Id);
		if(rmcBean!=null){
			if(rmcBean.getBtnType()!=0){
				imageLayout.setVisibility(View.GONE);
			}else{
				imagePath=rmcBean.getBtnImage();
				imageIco.setImageBitmap(ImageUtils.getInstance().decodeImage(imagePath,5,5));
			}
			buttonName.setText(rmcBean.getBtnName());
			actionTxt.setText(rmcBean.getBtnAction());
			typeId=rmcBean.getCodeType();
			codeType(typeId);

		}
	}


	public static IWidgetDAO sql() {
		return MyApplication.getInstance().getWidgetDataBase();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.backTxt:
				back();
				break;
			case R.id.editeTxt:
				save();
			break;
			case R.id.colorLayout:{
				final ActionViewWindow typeWindow=new ActionViewWindow(activity,"颜色选择", ListNumBer.getColors(),0);
				typeWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void cancle() {

					}
					@Override
					public void onItem(int pos, String ItemName) {

						switch (pos){
							case 0:
								colorStr="000000";
								break;
							case 1:
								colorStr="FFFFFF";
								break;
							case 2:
								colorStr="FF0000";
								break;
							case 3:
								colorStr="FF6100";
								break;
							case 4:
								colorStr="FFFF00";
								break;
							case 5:
								colorStr="00FF00";
								break;
							case 6:
								colorStr="0000FF";
								break;
							case 7:
								colorStr="A020F0";
								break;
							case 8:
								colorStr="00FFFF";
								break;
							case 9:
								colorStr="C0C0C0";
								break;
						}
						colorTxt.setTextColor(Color.parseColor("#"+colorStr));
						typeWindow.dismiss();
					}
				});
				typeWindow.show();
			}
				break;
			case R.id.imageLayout:{
				final ActionViewWindow typeWindow=new ActionViewWindow(activity,"图片类型", ListNumBer.getImageType(),0);
				typeWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void cancle() {

					}
					@Override
					public void onItem(int pos, String ItemName) {
						switch (pos){
							case 0:
								Intent getImageByalbum = new Intent(Intent.ACTION_GET_CONTENT);
								getImageByalbum.addCategory(Intent.CATEGORY_OPENABLE);
								getImageByalbum.setType("image/jpeg");
								startActivityForResult(getImageByalbum, REQUEST_CODE_IMAGE_OP);
								break;
							case 1:
								Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
								ContentValues values = new ContentValues(1);
								values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
								Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
								MyApplication.getInstance().setCaptureImage(uri);
								intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
								startActivityForResult(intent, REQUEST_CODE_IMAGE_CAMERA);
								break;
						}
						typeWindow.dismiss();
					}
				});
				typeWindow.show();
			}
				break;
			case R.id.typeLayout: {
				final ActionViewWindow typeWindow = new ActionViewWindow(activity, "按钮动作", ListNumBer.getButtonType(), 0);
				typeWindow.setAlertViewOnCListener(new ActionViewWindow.AlertViewOnCListener() {
					@Override
					public void cancle() {

					}

					@Override
					public void onItem(int pos, String ItemName) {
						typeId = pos;
						actionMsg.setText(ItemName);
						typeTxt.setText(ItemName);
						actionTxt.setText("点击选择");
						typeWindow.dismiss();
					}
				});
				typeWindow.show();
			}
				break;
			case R.id.actionLayout:
				if(typeId==0){
					ModeRmcActivity.startActivity(activity,0);
				}else if(typeId==1){
					final RmcButtonPopupWindow rmcButtonPopupWindow=new RmcButtonPopupWindow(activity,"设备");
						rmcButtonPopupWindow.setOnOCListene(new RmcButtonPopupWindow.OnOCListener() {
						@Override
						public void onResult(int oneIndex, String oneData, int twoIndex, String twoData) {
						Device device= DeviceFactory.getInstance().getDevicesByName(oneData,twoData);
						if(device!=null){
							RMCActionActivity.startActivity(activity,device.getId());
						}else{
							ToastUtils.ShowError(activity,"设备不存在", Toast.LENGTH_SHORT,true);
						}
						rmcButtonPopupWindow.dismiss();
						}
					});
					rmcButtonPopupWindow.show();
				}
				break;
		default:
			break;
		}
	}

	public void save(){
		if(typeId==-1){
			ToastUtils.ShowError(activity,"请选择动作类型",Toast.LENGTH_SHORT,true);
			return;
		}
		if(rmcBean!=null){
			rmcBean.setBtnName(buttonName.getText().toString());
			rmcBean.setCodeType(typeId);
			rmcBean.setBtnAction(actionTxt.getText().toString());
			rmcBean.setBtnSize(Integer.parseInt(txtSize.getText().toString()));
			rmcBean.setBtnColor(colorStr);
			rmcBean.setBtnImage(imagePath);
			int result=sql().updateRMCBean(rmcBean);

			if(result>0){
				CenterControlActivity.getInstance().updateSubCustomButton(Id,buttonName.getText().toString(),imagePath);
				ToastUtils.ShowSuccess(activity,"修改成功",Toast.LENGTH_SHORT,true);
				RmcBeanFactory.getInstance().clearList();
			}
		}
	}
	public void back(){
		Intent resultIntent = new Intent();
		this.setResult(100, resultIntent);
		this.finish();
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void codeType(int codeType){
		 if(codeType==0){
			typeTxt.setText("本地场景");
			actionMsg.setText("本地场景");
		}else if(codeType==1){
			typeTxt.setText("自定义动作");
			actionMsg.setText("自定义动作");
		}else if(codeType==-1){
		 	actionTxt.setText("点击选择");
		 }
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==101){
			String dataStr=data.getStringExtra("actionValue");
			if(!dataStr.equals("")){
				actionTxt.setText(dataStr);
			}
		}else if(resultCode==102){
			String dataStr=data.getStringExtra("actionValue");
			if(!dataStr.equals("")){
				actionTxt.setText(dataStr);
			}
		}else if (requestCode == REQUEST_CODE_IMAGE_OP && resultCode == RESULT_OK) {
			Uri mPath = data.getData();
			String file = getPath(mPath);
			String fileName=file.substring(file.lastIndexOf("/"));
			String newFile=MyApplication.getInstance().getImagePath()+fileName;
			FileUtil.copyFile(file,newFile);
			imagePath=newFile;
			Bitmap bmp = ImageUtils.getInstance().decodeImage(newFile,5,5);
			if (bmp == null || bmp.getWidth() <= 0 || bmp.getHeight() <= 0 ) {
			} else {
				imageIco.setImageBitmap(bmp);
			}
			//joe 文件路径
			//startRegister(bmp, file);
		} else if (requestCode == REQUEST_CODE_OP) {

			if (data == null) {
				return;
			}
			Bundle bundle = data.getExtras();
			String path = bundle.getString("imagePath");

		} else if (requestCode == REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
			Uri mPath = MyApplication.getInstance().getCaptureImage();
			String file = getPath(mPath);
			String fileName=file.substring(file.lastIndexOf("/"));
			String newFile=MyApplication.getInstance().getImagePath()+fileName;
			FileUtil.copyFile(file,newFile);
			imagePath=newFile;
			Bitmap bmp = ImageUtils.getInstance().decodeImage(newFile,5,5);
			imageIco.setImageBitmap(bmp);

		}
	}

	/**
	 * @param uri
	 * @return
	 */
	private String getPath(Uri uri) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (DocumentsContract.isDocumentUri(this, uri)) {
				// ExternalStorageProvider
				if (isExternalStorageDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}

					// TODO handle non-primary volumes
				} else if (isDownloadsDocument(uri)) {

					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

					return getDataColumn(this, contentUri, null, null);
				} else if (isMediaDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}

					final String selection = "_id=?";
					final String[] selectionArgs = new String[] {
							split[1]
					};

					return getDataColumn(activity, contentUri, selection, selectionArgs);
				}
			}
		}
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor actualimagecursor = this.getContentResolver().query(uri, proj, null, null, null);
		int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		actualimagecursor.moveToFirst();
		String img_path = actualimagecursor.getString(actual_image_column_index);
		String end = img_path.substring(img_path.length() - 4);
		if (0 != end.compareToIgnoreCase(".jpg") && 0 != end.compareToIgnoreCase(".png")) {
			return null;
		}
		return img_path;
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}

	/**
	 * @param uri The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	}

	/**
	 * Get the value of the data column for this Uri. This is useful for
	 * MediaStore Uris, and other file-based ContentProviders.
	 *
	 * @param context The context.
	 * @param uri The Uri to query.
	 * @param selection (Optional) Filter used in the query.
	 * @param selectionArgs (Optional) Selection arguments used in the query.
	 * @return The value of the _data column, which is typically a file path.
	 */
	public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

}
