/**
 * 
 */
package com.bluecam.api.view;

import android.widget.ListView;

/**
 * @author wang.jingui
 *
 * @date 2013-1-8
 */
public class MyListView extends ListView {
	public MyListView(android.content.Context context,
                      android.util.AttributeSet attrs)
    {  
        super(context, attrs);  
    }  
  
    /** 
     * 设置不滚动 
     */  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
    {  
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
  
    }  
}