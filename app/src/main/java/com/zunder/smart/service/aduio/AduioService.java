package com.zunder.smart.service.aduio;

import android.app.Activity;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.door.Utils.ToastUtils;
import com.zunder.smart.activity.main.MainActivity;
import com.zunder.smart.dao.impl.factory.GateWayFactory;
import com.zunder.smart.json.ServiceUtils;
import com.zunder.smart.model.GateWay;
import com.zunder.smart.service.SendCMD;
import com.zunder.smart.socket.info.ISocketCode;
import com.zunder.smart.tools.AppTools;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import java.util.List;

/**
 * Created by Administrator on 2017/7/11.
 */

public class AduioService {
    private static AduioService install;
    private EditText ed;
    private String edOldText = "";
    private   Activity activity;
    // 语音听写对象
    private SpeechRecognizer mIat;
    public boolean recStart = false;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private OnCallInterFace onCallInterFace;
    private int ret=0;

    private boolean mTranslateEnable = false;
    // private SharedPreferences sp;
    public AduioService() {
    }

    public static synchronized AduioService getInstance() {
        if (install == null) {
            install = new AduioService();
        }
        return install;
    }

    public void setInit(Activity _activity) {
        this.activity = _activity;
        initPrams();
    }
    public void setEditDevice(Activity _activity,EditText _ed, String oldText) {
        this.activity = _activity;
        this.ed = _ed;
        this.edOldText = oldText;
        initPrams();
    }

   public void initPrams(){
       recStart = true;
       mIat = SpeechRecognizer.createRecognizer(activity, mInitListener);
       mIatDialog = new RecognizerDialog(activity, mInitListener);
       setParam();
       mIatDialog.setListener(mRecognizerDialogListener);
       mIatDialog.show();
//       showTip("开始说话");
   }
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtils.ShowError(activity,"初始化失败，错误码：" + code,Toast.LENGTH_SHORT,true);
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (recStart == true) {
                recStart = false;
                String text = JsonParser.parseIatResult(
                        results.getResultString()).replace("��", "").replace("。","").replace("，",",");
                if (ed != null) {
                    if (edOldText.length() > 0) {
                        if (edOldText.indexOf(text) == -1) {
//                            ed.setText(edOldText + "," + text);

                            int index = ed.getSelectionStart();
                            Editable editable = ed.getText();
                            editable.insert(index, "," + text);
                        }
                    } else {
//                        ed.setText(text);
                        int index = ed.getSelectionStart();
                        Editable editable = ed.getText();
                        editable.insert(index, text);
                    }
                    ed = null;
                    return;
                }
                if (text.length() > 0) {
                    switchService(text);
                }
                showTip(text);
                if (isLast) {
                    text = "";
                }
            }
        }
        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            recStart = false;
            ed=null;
            edOldText="";
            if(mTranslateEnable && error.getErrorCode() == 14002) {
                showTip( error.getPlainDescription(true)+"\n请确认是否已开通翻译功能" );
            } else {
                showTip(error.getPlainDescription(true));
            }
        }
    };

    private void showTip(final String str) {
        ToastUtils.ShowSuccess(activity,str,Toast.LENGTH_SHORT,true);
//        MainActivity.getInstance().showToast(str);
    }

    /**
     * 参数设置
     * @param
     * * @return
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        if( mTranslateEnable ){
            mIat.setParameter( SpeechConstant.ASR_SCH, "1" );
            mIat.setParameter( SpeechConstant.ADD_CAP, "translate" );
            mIat.setParameter( SpeechConstant.TRS_SRC, "its" );
        }

        String lag = "zh_cn";
        if (lag.equals("en_us")) {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
            mIat.setParameter(SpeechConstant.ACCENT, null);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "en" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "cn" );
            }
        } else {
            // 设置语言
            mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mIat.setParameter(SpeechConstant.ACCENT, lag);

            if( mTranslateEnable ){
                mIat.setParameter( SpeechConstant.ORI_LANG, "cn" );
                mIat.setParameter( SpeechConstant.TRANS_LANG, "en" );
            }
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS,"4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS,  "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,  "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
    }

    private void printTransResult (RecognizerResult results) {
        String trans  = JsonParser.parseTransResult(results.getResultString(),"dst");
        String oris = JsonParser.parseTransResult(results.getResultString(),"src");

        if( TextUtils.isEmpty(trans)||TextUtils.isEmpty(oris) ){
            showTip( "解析结果失败，请确认是否已开通翻译功能。" );
        }else{

        }
    }

    public void setOnCallInterFace(OnCallInterFace onCallInterFace) {
        this.onCallInterFace = onCallInterFace;
    }

   public interface OnCallInterFace{
        public void OnCall(String cmd);
    }
    public void switchService(String str_zl) {
        int ActionFlag = 0;

        if (str_zl.contains("叮咚")) {
            List<GateWay> list = GateWayFactory.getInstance().getAll();
            for (int i = 0; i < list.size(); i++) {
                GateWay gateWay = list.get(i);
                if (gateWay.getTypeId() == 2) {
                    String cmd = ISocketCode.setForwardNameControl(str_zl.replace("叮咚", ""),gateWay.getGatewayID());
                    MainActivity.getInstance().sendCode(cmd);
                }
            }
        } else {
            String[] changServer = {"设置", "选择", "切换", "服务器"};
            for (int i = 0; i < changServer.length; i++) {
                if (str_zl.indexOf(changServer[i]) != -1) {// 有识别词
                    ActionFlag++;
                }
            }
            if (ActionFlag >= 2) {
                String pinying = AppTools.getPinYin(str_zl);
                int timeVal = AppTools.timeComprartion(1, 0, str_zl);
                if ((timeVal == 1)
                        || (str_zl.indexOf("和利扬") != -1)
                        || AppTools.PinYinComparison(pinying, "和利扬")) {// 有识别词
                    ServiceUtils.getServiceInfo().setServiceName("和利扬");
                    ServiceUtils.getServiceInfo().setIP("112.74.64.82");
                    ServiceUtils.getServiceInfo().setPort(2017);
                    ServiceUtils.saveServiceInfo();
                    MainActivity.getInstance().setIP("112.74.64.82", 2017);
                    showTip("已切换到1号和利扬服务器");
                } else if ((timeVal == 2)
                        || (str_zl.indexOf("创力特") != -1)
                        || AppTools.PinYinComparison(pinying, "创力特")) {// 有识别词
                    ServiceUtils.getServiceInfo().setServiceName("创力特");
                    ServiceUtils.getServiceInfo().setIP("60.251.48.20");
                    ServiceUtils.getServiceInfo().setPort(2017);
                    ServiceUtils.saveServiceInfo();
                    MainActivity.getInstance().setIP("60.251.48.20", 2017);
                    showTip("已切换到2号创力特服务器");
                }else if ((timeVal == 3)
                        || (str_zl.indexOf("准德") != -1)
                        || AppTools.PinYinComparison(pinying, "准德")) {// 有识别词
                    ServiceUtils.getServiceInfo().setServiceName("准德");
                    ServiceUtils.getServiceInfo().setIP("47.94.154.118");
                    ServiceUtils.getServiceInfo().setPort(2017);
                    ServiceUtils.saveServiceInfo();
                    MainActivity.getInstance().setIP("47.94.154.118", 2017);
                    showTip("已切换到3号准德服务器");
                }
                else if ((timeVal == 4)
                        || (str_zl.indexOf("自定义") != -1)
                        || AppTools.PinYinComparison(pinying, "自定义")) {// 有识别词
                    ServiceUtils.getServiceInfo().setServiceName("自定义");
                    ServiceUtils.getServiceInfo().setIP("192.168.2.124");
                    ServiceUtils.getServiceInfo().setPort(2017);
                    ServiceUtils.saveServiceInfo();
                    MainActivity.getInstance().setIP("192.168.2.124", 2017);
                    showTip("已切换到4号自定义服务器");
                }
            } else {
                SendCMD sendCMD = new SendCMD().getInstance();
                sendCMD.sendCMD(0, str_zl, null);
                if (onCallInterFace != null) {
                    onCallInterFace.OnCall(str_zl);
                }
            }
        }
    }
    }
