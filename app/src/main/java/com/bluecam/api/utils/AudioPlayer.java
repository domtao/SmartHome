
package com.bluecam.api.utils;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;


public class AudioPlayer{
	
	private CustomBuffer audioBuffer = null;
	private boolean bAudioPlaying = false;
	private Thread audioThread = null;
	private AudioTrack m_AudioTrack = null;
	
	public AudioPlayer(CustomBuffer buffer) {
		// TODO Auto-generated constructor stub
		audioBuffer = buffer;
	}
	
	public boolean isAudioPlaying(){
		return bAudioPlaying;
	}
	
	public boolean AudioPlayStart(){
		synchronized (this) {
			if (bAudioPlaying) {
				return true;
			}
			bAudioPlaying = true;
			audioThread = new Thread(new AudioPlayThread());
			audioThread.start();
		}		
		return true;
	}
	
	public void AudioPlayStop(){
		synchronized (this) {
			if (!bAudioPlaying || audioThread == null) {
				return ;
			}
			bAudioPlaying = false;
			try {
				audioThread.join();
			} catch (Exception e) {
			}
			audioThread = null;			
		}
	}
	
	public boolean initAudioDev() {
		int channelConfig;
		int audioFormat = 2;
		int mMinBufSize = 0;

		channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
		//channelConfig = AudioFormat.CHANNEL_IN_STEREO;
		audioFormat = AudioFormat.ENCODING_PCM_16BIT ;
		mMinBufSize = AudioTrack.getMinBufferSize(8000, channelConfig, audioFormat);
		
	    if(mMinBufSize == AudioTrack.ERROR_BAD_VALUE || mMinBufSize == AudioTrack.ERROR)
	    	return false;	   
	    
		try {
			m_AudioTrack = null; //STREAM_VOICE_CALL
			m_AudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 8000, channelConfig, audioFormat, mMinBufSize * 2, AudioTrack.MODE_STREAM);
			m_AudioTrack.play();
		} catch(IllegalArgumentException iae) {
			iae.printStackTrace();
			return false; 
		}catch (IllegalStateException e){
			e.printStackTrace();
			return false;
		}
		return true;
    }
	
	class AudioPlayThread implements Runnable {
		@Override
		public void run() {
			if (!initAudioDev()) {
				return ;
			}
			while (bAudioPlaying)
			{
				CustomBufferData data = audioBuffer.RemoveData();
				if (data == null) 
				{
					try {
						Thread.sleep(10);
						continue;
					} catch (Exception e) {
						m_AudioTrack.stop();
						return;
					}					
				}
				//System.out.println("-------m_AudioTrack.write   -----");
				m_AudioTrack.write(data.data, 0, data.head.length);
			}
	        m_AudioTrack.stop();
	        m_AudioTrack.release();
			m_AudioTrack=null;
		}
	}
}