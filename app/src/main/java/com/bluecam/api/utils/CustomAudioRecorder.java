package com.bluecam.api.utils;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.media.audiofx.AcousticEchoCanceler;
import android.media.audiofx.NoiseSuppressor;

public class CustomAudioRecorder {

	//读取录音数据线程
	private Thread recordThread = null;
	private boolean bRecordThreadRuning = false;
	
	private byte[] m_in_bytes = null;
	private int m_in_buf_size = 0;
	
	//录音类
	private AudioRecord m_in_rec = null;
	//回声抑制类
	private AcousticEchoCanceler canceler;
	//声音播放类
	private AudioTrack audioTrack;
	
	private AudioRecordResult audioResult = null;
	private boolean isSupport = true;
	
	private android.content.Context Context;
	
	//音频数据回调接口
	public interface AudioRecordResult 
	{
		public void AudioRecordData(byte[] data, int len);
	}

	public CustomAudioRecorder(AudioRecordResult result) 
	{
		audioResult = result;
		initRecorder();
	}

	//开始
	public void startRecord()
	{
		synchronized (this) 
		{
			if (bRecordThreadRuning) 
			{
				return;
			}
			bRecordThreadRuning = true;
			recordThread = new Thread(new RecordThread());
			recordThread.start();
		}
	}

	//结束
	public void stopRecord()
	{
		synchronized (this) 
		{
			if (!bRecordThreadRuning)
			{
				return;
			}
			bRecordThreadRuning = false;
			/*try {
				recordThread.join();

			} catch (Exception e) {
			}*/
			recordThread = null;
		}
	}

	//释放
	public void releaseRecord() {
		if (m_in_rec != null) {
			m_in_rec.release();
			m_in_rec = null;
		}
		release();
	}

	class RecordThread implements Runnable
	{
		@Override
		public void run() 
		{
			try {
				m_in_rec.startRecording();
				while (bRecordThreadRuning) 
				{
					if(m_in_rec != null)
					{
						int nRet = m_in_rec.read(m_in_bytes, 0, m_in_buf_size);
						if (nRet == 0) {
							return;
						}
						if (audioResult != null) {
							//LogUtil.printLog("m_in_bytes len =="+m_in_bytes.length);
							audioResult.AudioRecordData(m_in_bytes, nRet);
						}
					}
				}
				m_in_rec.stop();
			} catch (IllegalStateException e)
			{
				e.printStackTrace();
			} 
		}
	}
	
	//录音初始化函数
	public boolean initRecorder()
	{
		m_in_buf_size = AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		m_in_rec = new AudioRecord(MediaRecorder.AudioSource.MIC,8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, m_in_buf_size);
		m_in_bytes = new byte[m_in_buf_size];
		if(chkNewDev())
		{
			//回声抑制
			if(isDeviceSupport())
			{
				isSupport= initAEC(m_in_rec.getAudioSessionId());
				System.out.println("-------------支持回声抑制-------------isSupport=="+isSupport);
			}
			else
			{
				isSupport = false;
				System.out.println("-------------不支持回声抑制-------------");
			}
			//静音消除
			if (isNSAvailable())
			{
				//System.out.println("-------------支持静音消除-------------");
				int resultCode = 0;
				NoiseSuppressor noiseSuppressor = NoiseSuppressor.create(m_in_rec.getAudioSessionId());
				if(noiseSuppressor != null)
				{
					resultCode = noiseSuppressor.setEnabled(true);
					System.out.println("-------------支持静音消除-------------resultCode=="+resultCode);
				}
				else
				{
					System.out.println("-------------不支持静音消除-------------");
				}
			}
			else
			{
				isSupport = false;
				System.out.println("-------------不支持静音消除-------------");
			}

			//int trackBufferSizeInBytes = AudioTrack.getMinBufferSize(8000, AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT);
			//audioTrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL,8000,AudioFormat.CHANNEL_OUT_MONO,AudioFormat.ENCODING_PCM_16BIT, trackBufferSizeInBytes, AudioTrack.MODE_STREAM,m_in_rec.getAudioSessionId());
		}
		else
		{
			System.out.println("-------------不支持回声抑制-------------");
		}
		if (m_in_rec == null) {
			return false;
		}
		return true;
	}
	
	//初始化回声抑制函数
	public boolean initAEC(int audioSession)
	{
	    if (canceler != null)
	    {
	        return false;
	    }
	    canceler = AcousticEchoCanceler.create(audioSession);
	    if(canceler == null)
	    {
	    	System.out.println("-------------不支持回声抑制-------------");
	    	isSupport = false;
	    	return false;
	    }
	    canceler.setEnabled(true);
	    return canceler.getEnabled();
	}
	
	public boolean release()
	{
	    if (null == canceler)
	    {
	        return false;
	    }
	    canceler.setEnabled(false);
	    canceler.release();
	    return true;
	}
	
	//是否支持静音消除
	@SuppressLint("NewApi")
	public static boolean isNSAvailable() {
		return NoiseSuppressor.isAvailable();
	}
	
	public static boolean chkNewDev()
	{
	    return android.os.Build.VERSION.SDK_INT >= 16;
	}
	//是否支持回声抑制
	public static boolean isDeviceSupport()
	{
	    return AcousticEchoCanceler.isAvailable();
	}
	
	public boolean isDeviceSupport1()
	{
		System.out.println("-------isSupport=="+isSupport);
		return isSupport;
	}
}