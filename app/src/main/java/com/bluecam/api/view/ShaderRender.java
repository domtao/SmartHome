package com.bluecam.api.view;


import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ShaderRender implements Renderer {
	final String tagNewRender = "NewRender";
	String mVertexShader;								//vertex shader 顶点
	String mFragmentShader;								//fragment shader
	int mProgram;										// program id
	int mvPositionHandle;								// vertex position
	int mTextCoordHandle;								// texture coord
	int[] mTexture = new int[3];						// 纹理
	int mWidth = 0;										// 视频宽
	int mHeight = 0;									// 视频高
	ByteBuffer mYBuffer = null;							// y,u,v分量
	ByteBuffer mUBuffer = null;
	ByteBuffer mVBuffer = null;
	FloatBuffer mPositionBuffer = null;
	FloatBuffer mCoordBuffer = null;
	boolean mbNeedSleep = true;							// 等待数据
	float mPos = 1.0f;
	private RenderListener listener;
	public void setListener(RenderListener listener) {
		this.listener = listener;
	}
	public ShaderRender(GLSurfaceView surfaceView) {

		surfaceView.setEGLContextClientVersion(2);

		mVertexShader = "attribute vec4 vPosition;\n";
		mVertexShader += "attribute vec2 myTexCoord;\n";
		mVertexShader += "varying vec2 TexCoordOut;\n";
		mVertexShader += "void main(void)\n";
		mVertexShader += "{\n";
		mVertexShader += "	gl_Position = vPosition;\n";
		mVertexShader += "	TexCoordOut = myTexCoord;\n";
		mVertexShader += "}";

		mFragmentShader = "varying lowp vec2 TexCoordOut;\n";
		mFragmentShader += "uniform sampler2D Ytex;\n";
		mFragmentShader += "uniform sampler2D Utex;\n";
		mFragmentShader += "uniform sampler2D Vtex;\n";
		mFragmentShader += "void main(void)\n";
		mFragmentShader += "{\n";
		mFragmentShader += "	mediump vec3 yuv;\n";
		mFragmentShader += "	lowp vec3 rgb;\n";
		mFragmentShader += "	yuv.x = texture2D(Ytex, TexCoordOut).r;\n";
		mFragmentShader += "	yuv.y = texture2D(Utex, TexCoordOut).r - 0.5;\n";
		mFragmentShader += "	yuv.z = texture2D(Vtex, TexCoordOut).r - 0.5;\n";
		mFragmentShader += "	rgb = mat3( 1, 1, 1,0, -0.39465, 2.03211, 1.13983, -0.58060, 0) * yuv;\n";
		mFragmentShader += "	gl_FragColor = vec4(rgb, 1);\n";
		mFragmentShader += "}";

		float squareVertex[] = new float[]{
	        -1f, -1f, 0.0f,
	        1f, -1f, 0.0f,
	        -1f,  1f, 0.0f,
	        1f,  1f, 0.0f
		};
		ByteBuffer vbb = ByteBuffer.allocateDirect(squareVertex.length * 4);
		//设置字节顺序为本地操作系统顺序
		vbb.order(ByteOrder.nativeOrder());
		//将该缓冲区转换为浮点型缓冲区
		mPositionBuffer = vbb.asFloatBuffer();
		//将顶点的位置数据写入到顶点缓冲区数组中
		mPositionBuffer.put(squareVertex);
		//设置缓冲区的起始位置为0
		mPositionBuffer.position(0);

		float coordVertex[] = new float[]{
			0, 1,
	        1, 1,
	        0,  0,
	        1,  0
		};
		ByteBuffer vbb2 = ByteBuffer.allocateDirect(coordVertex.length * 4);
		//设置字节顺序为本地操作系统顺序
		vbb2.order(ByteOrder.nativeOrder());
		//将该缓冲区转换为浮点型缓冲区
		mCoordBuffer = vbb2.asFloatBuffer();
		//将顶点的位置数据写入到顶点缓冲区数组中
		mCoordBuffer.put(coordVertex);
		//设置缓冲区的起始位置为0
		mCoordBuffer.position(0);

	}


	/**
	 * init shader
	 */
	public void initShader(){
		// shader program
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);

		GLES20.glUseProgram(this.mProgram);
		// get attrib and uniform
		mvPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
		mTextCoordHandle = GLES20.glGetAttribLocation(mProgram, "myTexCoord");
		int textureY = GLES20.glGetUniformLocation(mProgram, "Ytex");
		int textureU = GLES20.glGetUniformLocation(mProgram, "Utex");
		int textureV = GLES20.glGetUniformLocation(mProgram, "Vtex");
		GLES20.glUniform1i(textureY, 0);
		GLES20.glUniform1i(textureU, 1);
		GLES20.glUniform1i(textureV, 2);
	}

	public void setupTexture(){
		GLES20.glGenTextures(3, this.mTexture, 0);
	    if (this.mTexture[0] == 0 || this.mTexture[1] == 0 || this.mTexture[2] == 0)
	    {
	        Log.e(tagNewRender, "<<<<<<<<<<<<create fail!>>>>>>>>>>>>");
	        return;
	    }
		// Y
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.mTexture[0]);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	    // U
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.mTexture[1]);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	    // V
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, this.mTexture[2]);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
	    GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
	}

	public class AutoZoomThread extends Thread {

		public boolean flag = true;
		public boolean zoomout = true;
		@Override
		public void run() {
			while(flag){
				if (zoomout) {
					mPos += 0.1f;
					if (mPos > 5.0f) zoomout = false;
				} else {
					mPos -= 0.1f;
					if (mPos < 0.01f) zoomout = true;
				}
				try {
					Thread.sleep(200);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void AutoZoomInOut() {
		float squareVertex[] = new float[]{
		        -mPos, -mPos, 0.0f,
		        mPos, -mPos, 0.0f,
		        -mPos,  mPos, 0.0f,
		        mPos,  mPos, 0.0f
			};
		ByteBuffer vbb = ByteBuffer.allocateDirect(squareVertex.length * 4);
		//设置字节顺序为本地操作系统顺序
		vbb.order(ByteOrder.nativeOrder());
		//将该缓冲区转换为浮点型缓冲区
		mPositionBuffer = vbb.asFloatBuffer();
		//将顶点的位置数据写入到顶点缓冲区数组中
		mPositionBuffer.put(squareVertex);
		//设置缓冲区的起始位置为0
		mPositionBuffer.position(0);

	}

	private float nLenStart = 0;
	private boolean isZoom  = false;

	public boolean isZoom() {
		return isZoom;
	}
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isZoom = false;
		}
	};


	// 放大
	public void zoomin() {
		if (mPos > 2.5f) return;
		mPos += 0.015f;
		AutoZoomInOut();
	}

	// 缩小
	public void zoomout() {
		if (mPos < 0.5f) return;
		mPos -= 0.015f;
		AutoZoomInOut();
	}

	public void reset() {
		mPos = 1.0f;
		AutoZoomInOut();
	}

	public void Render() {
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // copy y
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, this.mWidth, this.mHeight, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, this.mYBuffer);
        // copy u
		GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, this.mWidth / 2, this.mHeight / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, this.mUBuffer);
        // copy v
		GLES20.glActiveTexture(GLES20.GL_TEXTURE2);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_LUMINANCE, this.mWidth / 2, this.mHeight / 2, 0, GLES20.GL_LUMINANCE, GLES20.GL_UNSIGNED_BYTE, this.mVBuffer);

	    // Update attribute values
		GLES20.glVertexAttribPointer(this.mvPositionHandle, 3, GLES20.GL_FLOAT, false, 0, this.mPositionBuffer);
		GLES20.glEnableVertexAttribArray(this.mvPositionHandle);

		GLES20.glVertexAttribPointer(this.mTextCoordHandle, 2, GLES20.GL_FLOAT, false, 0, this.mCoordBuffer);
		GLES20.glEnableVertexAttribArray(this.mTextCoordHandle);

	    // Draw
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}

	public void onDrawFrame(GL10 gl) {
		synchronized (this) {
			if ((this.mWidth == 0) || (this.mHeight == 0)
					|| (this.mYBuffer == null)
					|| (this.mUBuffer == null)
					|| (this.mVBuffer == null))
				return;

			if (this.mbNeedSleep) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.mbNeedSleep = true;

			this.Render();
		}
	}


	public void onSurfaceChanged(GL10 gl, int width, int height) {
		// TODO Auto-generated method stub
		// 设置视口
		GLES20.glViewport(0, 0, width, height);
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// TODO Auto-generated method stub
		GLES20.glClearColor(0.0F, 0.0F, 0.0F, 1.0F);
		initShader();
		setupTexture();
	}

	public void writeSample(byte[] ArrOfByte, int width, int height) {
		synchronized (this)
		{
			//long start = System.currentTimeMillis();
			if ((width == 0) || (height == 0)) {
				Log.e(this.tagNewRender, "invalid param width or height");
				return;
			}
			//LogUtil.printLog("writeSample ------------>width == "+width);
			if(listener != null)
			{
				listener.loadComplete(ArrOfByte.length,width,height);
			}

			if ((width != this.mWidth) || (height != this.mHeight)) {
				this.mWidth = width;
				this.mHeight = height;
				this.mYBuffer = ByteBuffer.allocate(this.mWidth * this.mHeight);
				this.mUBuffer = ByteBuffer.allocate(this.mWidth * this.mHeight/ 4);
				this.mVBuffer = ByteBuffer.allocate(this.mWidth * this.mHeight/ 4);
			}

			if (this.mYBuffer != null) {
				this.mYBuffer.position(0);
				this.mYBuffer.put(ArrOfByte, 0, this.mWidth * this.mHeight);
				this.mYBuffer.position(0);
			}

			if (this.mUBuffer != null) {
				this.mUBuffer.position(0);
				this.mUBuffer.put(ArrOfByte, this.mWidth * this.mHeight,this.mWidth * this.mHeight / 4);
				this.mUBuffer.position(0);
			}

			if (this.mVBuffer != null) {
				this.mVBuffer.position(0);
				this.mVBuffer.put(ArrOfByte,5 * (this.mWidth * this.mHeight) / 4, this.mWidth* this.mHeight / 4);
				this.mVBuffer.position(0);
			}

			mbNeedSleep = false;
			//long end = System.currentTimeMillis()-start;
			//System.out.println("shared render offTime == "+end/1000F);
		}
	}

	public interface RenderListener
	{
		public void loadComplete(int size, int width, int height);
	} 
}

