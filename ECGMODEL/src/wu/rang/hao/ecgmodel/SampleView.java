package wu.rang.hao.ecgmodel;

import java.util.Random;  

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.util.Log;  
import android.view.View;  
  
public class SampleView extends View {  
  
    public static final String TAG = "TAG_droidpaint_heartcurve.SampleView";  
    public static final boolean DEBUG = true;  
  
    public static void myLog(String str) {  
        if (DEBUG) {  
            Log.d(TAG, str);  
        }  
    }  
  
    public static void myLogE(String str) {  
        if (DEBUG) {  
            Log.e(TAG, str);  
        }  
    }  
  
    private Paint mPaint = new Paint();  
    private float[] mPts;  
  
    private static final int X0 = 100;  
    private static final int Y0 = 1000;  
    private static final int RD_MAX = Y0 / 2;  
    final int lines = 200;  
    static final int gap = 1;// 至少为1  
    final int DIV = lines / 2;  
  
    Random rd = new Random();  
  
    /* 
     * count 移动列数 
     */  
    public void updatePoints(int count) {  
        myLog("updatePoints():count=" + count);  
  
        int r = rd.nextInt(RD_MAX) + 1;  
  
        int update_lines = Math.abs(lines - count);  
  
        myLog("updatePoints():update_lines=" + update_lines);  
        int j = 0;  
        for (int i = 0; i < update_lines * 4;) {  
  
            mPts[i + 0] = X0 + j;  
            mPts[i + 1] = Y0;// 不变  
            // 终点:  
            mPts[i + 2] = X0 + j;  
            mPts[i + 3] = mPts[i + 3 + count * 4];  
            i = i + 4;  
            j = j + gap;// 向右递增  
  
        }  
  
        for (int i = update_lines * 4; i < lines * 4;) {  
            // 起点:  
            mPts[i + 0] = X0 + j;  
            mPts[i + 1] = Y0;// 不变  
            // 终点:  
            mPts[i + 2] = X0 + j;  
            mPts[i + 3] = Y0 - r;//  
            i = i + 4;  
            j = j + gap;// 向右递增  
  
            if (i % (DIV) == 0) {  
                r = rd.nextInt(RD_MAX) + RD_MAX / 10;  
            }  
  
        }  
    }  
  
    public void buildPoints() {  
        myLog("buildPoints()");  
  
        mPts = new float[lines * 4];  
        int j = 0;  
        int r = rd.nextInt(RD_MAX) + 1;  
        for (int i = 0; i < lines * 4;) {  
            // 起点:  
            mPts[i + 0] = X0 + j;  
            mPts[i + 1] = Y0;// 不变  
            // 终点:  
            mPts[i + 2] = X0 + j;  
            mPts[i + 3] = Y0 - r;//  
            i = i + 4;  
            j = j + gap;// 向右递增  
  
            if (i % (DIV) == 0) {  
                r = rd.nextInt(RD_MAX) + RD_MAX / 10;  
            }  
        }  
  
    }  
  
    /* 
     * count 移动列数 
     */  
    public void append_data(int count) {  
        myLog("append_data():count=" + count);  
  
                  
        updatePoints(count);  
        invalidate();  
    }  
  
    public SampleView(Context context) {  
  
        super(context);  
        myLog("SampleView()");  
  
        buildPoints();  
    }  
  
    public SampleView(Context context, AttributeSet attrs) {  
        // TODO Auto-generated constructor stub  
        super(context, attrs);  
  
        myLog("SampleView(,)");  
  
        buildPoints();  
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
  
        myLog("onDraw()");  
  
        Paint paint = mPaint;  
  
        canvas.translate(10, 10);  
  
        canvas.drawColor(Color.WHITE);  
  
        paint.setColor(Color.RED);  
        paint.setStrokeWidth(0);  
        canvas.drawLines(mPts, paint);  
  
    }  
} 
