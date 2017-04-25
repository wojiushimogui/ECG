package wu.rang.hao.readecg;
/*************************************************************/
/* Project Shmimn 
 *  Mobile Health-care Device
 *  Yuhua Chen
 *  2011-4-24 16:35:21
 */
/*************************************************************/
import java.util.List;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawECGWaveForm {
	private SurfaceView sfv;
	private SurfaceHolder sfh;
	private int sfHeight;
	private int sfWidth;
	private int tmpX = 0, tmpY = 0;
	private int scaleX = 2, scaleY = 80;
	private Paint mPaint;
	public DrawECGWaveForm(SurfaceView msfv)
	{
		this.sfv = msfv;
	}
	
	public void InitCanvas()
	{
		sfh = sfv.getHolder();//得到Sacefure对象实例
		sfHeight = sfv.getHeight();
		sfWidth = sfv.getWidth();
		mPaint = new Paint();//创建画笔并设置画笔的一些属性
		mPaint.setColor(Color.BLUE);
		mPaint.setStrokeWidth(3);
		mPaint.setAntiAlias(true);
	}
	
	public void CleanCanvas(){
		//Canvas mCanvas = sfh.lockCanvas(new Rect(0,0,sfv.getWidth(),sfv.getHeight()));
		//将上面的一条语句分开来写如下 ：
		//Rect rect=new Rect(0,0,sfv.getWidth(),sfv.getHeight());
		//Canvas mCanvas = sfh.lockCanvas();
		//mCanvas.drawRect(recr,mPaint);
		//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了
		Canvas mCanvas = sfh.lockCanvas(new Rect(0,0,sfv.getWidth(),sfv.getHeight()));
		
		mCanvas.drawColor(Color.WHITE);//设置画布背景颜色
		tmpX = 0;
		tmpY = sfHeight/2;
		sfh.unlockCanvasAndPost(mCanvas);
	}
	
	public void DrawtoView(List<Float> ECGDataList){
		if (sfh == null){
			this.InitCanvas();
			this.CleanCanvas();
		}
		int ptsNumber = ECGDataList.size();
		int posLst = 0;
		while(posLst < ptsNumber){
			int posCan = 0;
			float drawPoints[] = new float[(ptsNumber)*4];
			float ECGValue = tmpY;			
			if (tmpX ==0){
				drawPoints[0] = 0;drawPoints[1] = tmpY;
			}
			else{
				drawPoints[0] = tmpX; drawPoints[1] = tmpY;
			}

			for (posCan = tmpX ; posCan < sfWidth && posLst < ptsNumber; posCan+=scaleX){
				try{
					ECGValue = -ECGDataList.get(posLst)*scaleY + sfHeight/2;
				}catch(Exception e){
					e.printStackTrace();
					posLst++;
					continue;
				}
				drawPoints[4*posLst+2] = posCan;
				drawPoints[4*posLst+3] = ECGValue;
				if(posLst < ptsNumber -1){
				drawPoints[4*posLst+4] = posCan;
				drawPoints[4*posLst+5] = ECGValue;
				}
				posLst++;
			}
			if (posCan >= sfWidth){
				this.CleanCanvas();
			}
			else
			{
			Canvas mCanvas = sfh.lockCanvas(new Rect(tmpX,0,posCan-scaleX,sfHeight));
			mCanvas.drawColor(Color.WHITE);			
			mCanvas.drawLines(drawPoints,mPaint);
			sfh.unlockCanvasAndPost(mCanvas);
			tmpX = posCan-scaleX;
			tmpY = (int)ECGValue; 
			}
		}

//		curX+=3;
//
//		if (curX > sfWidth)
//			curX = 0;	
//		
//		mPaint.setColor(Color.BLUE);
//		mPaint.setAntiAlias(true);
//		if (curX == 0){
//			preX = curX;
//			preY = ECGValue;
//			this.CleanCanvas();
//		}
//		else{
//			curY = ECGValue;
//			mCanvas = sfh.lockCanvas(new Rect(preX,0,curX,sfHeight));
//			mCanvas.drawColor(Color.WHITE);
//			mCanvas.drawLine(preX, preY, curX, curY, mPaint);
//			preX = curX;
//			preY = curY;
//			sfh.unlockCanvasAndPost(mCanvas);
//		}


	}
	
}
