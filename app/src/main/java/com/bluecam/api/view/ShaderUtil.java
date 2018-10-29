package com.bluecam.api.view;

import android.content.res.Resources;
import android.opengl.GLES20;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ShaderUtil {
	
	/**
	 * 加载着色器方法
	 * @param shaderType 着色器类型,顶点着色器(GLES20.GL_FRAGMENT_SHADER), 片元着色器(GLES20.GL_FRAGMENT_SHADER)
	 */
	public static int loadShader(int shaderType , String source){
		//创建一个着色器, 并记录所创建的着色器的id, 如果id==0, 那么创建失败
		int shader = GLES20.glCreateShader(shaderType);
		if(shader != 0){
			GLES20.glShaderSource(shader, source);
			GLES20.glCompileShader(shader);
			int[] compiled = new int[1];
			//结果为0, 说明编译失败
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if(compiled[0] == 0){
				 Log.e("ES20_ERROR", "Could not compile shader " + shaderType + ":");
	             Log.e("ES20_ERROR", GLES20.glGetShaderInfoLog(shader));
	             //编译失败的话, 删除着色器, 并显示log
	             GLES20.glDeleteShader(shader);
	             shader = 0;
			}
		}
		return shader;
	}
	
	/**
	 * 检查每一步的操作是否正确
	 * @param op 具体执行的方法名, 比如执行向着色程序中加入着色器, 
	 * 		使glAttachShader()方法, 那么这个参数就是"glAttachShader"
	 */
	public static void checkGLError(String op){
		int error;
		//错误代码不为0, 就打印错误日志, 并抛出异常
		while( (error = GLES20.glGetError()) != GLES20.GL_NO_ERROR ){
			 Log.e("ES20_ERROR", op + ": glError " + error);
	         throw new RuntimeException(op + ": glError " + error);
		}
	}
	
	/**
	 * 创建着色程序
	 * @param vertexSource		定点着色器脚本字符串
	 * @param fragmentSource	片元着色器脚本字符串
	 * @return
	 */
	public static int createProgram(String vertexSource , String fragmentSource){
		//加载顶点着色器, 返回0说明加载失败
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
		if(vertexShader == 0)
			return 0;
		//加载片元着色器, 返回0说明加载失败
		int fragShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if(fragShader == 0)
			return 0;
		//创建着色程序, 返回0说明创建失败
		int program = GLES20.glCreateProgram();
		if(program != 0){
			GLES20.glAttachShader(program, vertexShader);
			checkGLError("glAttachShader");
			GLES20.glAttachShader(program, fragShader);
			checkGLError("glAttachShader");
			
			GLES20.glLinkProgram(program);
			int[] linkStatus = new int[1];
			//获取链接程序结果
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			if(linkStatus[0] != GLES20.GL_TRUE){
				Log.e("ES20.ERROR", "链接程序失败 : ");
				Log.e("ES20.ERROR", GLES20.glGetProgramInfoLog(program));
				//如果链接程序失败删除程序
				GLES20.glDeleteProgram(program);
				program = 0;
			}			
		}
		return program;
	}
	
	/**
	 * 加载着色脚本
	 */
	public static String loadFromAssetsFile(String fileName, Resources resources){
		String result = null;
		try {
			InputStream is = resources.getAssets().open(fileName);
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while((ch = is.read()) != -1){
				baos.write(ch);
			}
			byte[] buffer = baos.toByteArray();
			baos.close();
			is.close();
			result = new String(buffer, "UTF-8");
			result = result.replaceAll("\\r\\n", "\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
