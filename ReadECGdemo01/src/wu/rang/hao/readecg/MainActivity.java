package wu.rang.hao.readecg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button buttonRead;
	
	private SurfaceView sfvECG;
	
	private BTReadThread mReadThread = new BTReadThread(50);//线程实例化
	private Handler msgHandler;
	private DrawECGWaveForm mECGWF;
	BufferedReader bufferReader;
	StringBuffer strBuffer;
	File file;
	String str=new String();
	Boolean enRead=false;
	
	private String revTmpStr = new String();
	
	public List<Float> ECGDataList = new ArrayList<Float>();
	public boolean ECGDLIsAvailable = true;
	private float ECGData = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonRead=(Button)findViewById(R.id.read);
		sfvECG=(SurfaceView)findViewById(R.id.sfvECG);
		mECGWF = new DrawECGWaveForm(sfvECG);//DrawECGWaveForm对象的实例化，传入的参数为SrufaceView的实例
	
		Looper lp = Looper.myLooper();
		msgHandler = new MsgHandler(lp);//实例化一个Handler对象
		
		// Setting Timer to Draw and Save data
		Timer mDSTimer = new Timer();
		TimerTask mDSTask = new TimerTask(){
			public void run(){
				Message msg = Message.obtain();
				msg.what = 1;
				msgHandler.sendMessage(msg);
			}
		};
		
		// Set Timer
		mDSTimer.schedule(mDSTask,1000,1000);
		buttonRead.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				enRead=true;
				Log.d("MainActivity.this","马上开始-----------------》写数据了");
				writeDataToSD();
				Log.d("MainActivity.this","马上开始--------------》读数据了");
				mReadThread.start();
				
				
			}
		});
	}

	
	
	// MsgHandler class to Update UI
		class MsgHandler extends Handler{
			public MsgHandler(Looper lp){
				super(lp);
			}
			
			@Override
			public void handleMessage(Message msg){
				switch(msg.what){
				case 1:
					if (ECGDataList.size() > 1){
						List<Float> ECGCacheData = new ArrayList<Float>();
						ECGCacheData.addAll(ECGDataList);
						ECGDLIsAvailable = false;
						ECGDataList.clear();
						ECGDLIsAvailable = true;
						// Draw To View
						mECGWF.DrawtoView(ECGCacheData);	//调用DrawECGWaveForm类中的DrawtoView方法对接受到的数据画图显示				
					}
					break;
				}

			}
		}
		
		public void writeDataToSD(){  
			try{  
			    /* 获取File对象，确定数据文件的信息 */  
			    //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");  
			  File file1  = new File(Environment.getExternalStorageDirectory(),"100.txt");
			  File file2=new File(Environment.getExternalStorageDirectory(),"wu.txt");
			  Log.d("MainActivity.this","两个文件已经------------------建立好了");
			   /* 判断sd的外部设置状态是否可以读写 */  
			    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){ 
			    	//将文件的内容改为我们需要的格式内容
			    	BufferedReader bufferReader1=new BufferedReader(new InputStreamReader(new FileInputStream(file1)));
			    	StringBuffer strBuffer1=new StringBuffer();
			    	String str1[];
			    	Log.d("MainActivity.this","马上开始读取-----------------------数据了-----------");
					while((str=bufferReader1.readLine())!=null){
							Log.d("MainActivity", "bufferReader读取数据语句-----------之前");
							str1=str.split("");
							strBuffer1.append(str1[2]).append(";");
							
					}
			          
			        /* 流的对象 *//*  */  
					Log.d("MainActivity.this","马上开始写数据了--------------------------到");
			     FileOutputStream fos = new FileOutputStream(file2);  
			      byte[] buffer = strBuffer1.toString().getBytes();  
				  
			        /* 开始写入数据 */  
			        fos.write(buffer);  

			       /* 关闭流的使用 */  
			        fos.close();  
			        bufferReader1.close();
			        

			        /* 需要写入的数据 */  
			       /* String message = "0.160;0.180;0.170;0.180;0.180;0.170;0.170"
			        		+ ";0.130;0.130;0.100;0.060;0.040;0.010;-0.010;-0.020;"
			        		+ "-0.020;-0.020;-0.030;0.000;0.000;0.000;0.020;0.030;0.040;"
			        		+ "0.030;0.070;0.050;0.060;0.050;0.050;0.050;0.050;0.060;0.050;"
			        		+ "0.050;0.030;0.030;0.030;0.020;0.020;0.020;0.010;0.000;0.000;0.000"
			        		+ ";-0.010;0.000;-0.010;-0.010;-0.010;-0.010;-0.010;-0.020;-0.020;"
			        		+ "-0.010;-0.010;0.000;0.060;0.050;0.090;0.110;0.160;0.130;0.050;0.030;"
			        		+ "-0.090;-0.120;-0.090;-0.080;-0.090;-0.080;-0.080;-0.070;-0.080;0.090;"
			        		+ "0.450;0.790;-1.170;-1.910;-1.640;-1.280;-0.520;-0.150;-0.030;-0.030;0."
			        		+ "010;0.030;0.050;0.060;0.080;0.090;0.100;0.130;0.140;0.160;0.150;0.180;"
			        		+ "0.200;0.220;0.220;0.260;0.280;0.280;0.300;0.290;0.290;0.270;0.240;0.210;"
			        		+ "0.170;0.130;0.100;0.050;0.030;0.040;0.050;0.040;0.050;0.060;0.060;0."
			        		+ "060;0.060;0.050;0.040;0.070;0.060;0.070;0.060;0.080;0.080;"
			        		+ "0.080;0.090;0.100;0.100;0.090;0.090;0.090;0.080;0.070;0.070;0.060;"
			        		+ "0.050;0.050;0.050;0.020;0.040;0.030;0.010;0.010;0.000;0.010;0.000;"
			        		+ "0.000;0.000;0.000;-0.020;-0.010;-0.010;-0.020;-0.020;-0.020;-0.020;"
			        		+ "0.020;0.020;0.060;0.060;0.140;0.100;0.020;-0.020;-0.130;-0.130;-0.130;"
			        		+ "-0.100;-0.100;-0.100;-0.110;-0.110;-0.090;0.160;0.430;0.750;-1.520;-1.930"
			        		+ ";-1.360;-1.110;-0.440;-0.080;-0.060;-0.040;0.000;0.020;0.050;0.060;0.060;"
			        		+ "0.090;0.090;0.100;0.120;0.140;0.140;0.160;0.190;0.210;0.210;0.240;0.250;"
			        		+ "0.250;0.250;0.260;0.240;0.210;0.190;0.170;0.110;0.070;0.050;0.010;"
			        		+ "0.010;-0.010;0.010;0.000;0.010;0.020;0.010;0.010;0.020;0.020;"
			        		+ "0.010;0.010;0.020;0.020;0.020;0.020;0.020;0.020;0.020;0.030;"
			        		+ "0.030;0.020;0.020;0.020;0.030;0.010;0.010;0.000;-0.020;0.000;"
			        		+ "-0.010;-0.020;-0.020;-0.020;-0.020;-0.030;-0.040;-0.050;-0.050;"
			        		+ "-0.060;-0.060;-0.060;-0.060;-0.060;-0.060;-0.060;-0.060;-0.020;"
			        		+ "-0.020;-0.010;0.030;0.080;0.090;0.000;-0.050;-0.130;-0.180;-0.160;"
			        		+ "-0.150;-0.130;-0.130;-0.140;-0.130;-0.140;-0.040;0.340;0.710;-1.050;"
			        		+ "-1.870;-1.580;-1.250;-0.540;-0.170;-0.070;-0.070;-0.030;-0.010;0.010;"
			        		+ "0.020;0.030;0.040;0.040;0.040;0.060;0.080;0.080;0.090;0.100;0.110;0.120;"
			        		+ "0.140;0.150;0.170;0.150;0.160;0.140;0.120;0.120;0.080;0.040;0.030;0.010;"
			        		+ "-0.020;-0.030;-0.010;-0.030;-0.010;0.000;0.010;0.010;0.020;0.020;0.010;"
			        		+ "0.000;0.000;0.010;0.010;0.020;0.010;0.010;0.030;0.030;0.010;0.020;0.010;"
			        		+ "0.030;0.010;-0.010;0.010;0.010;0.000;-0.010;-0.010;-0.010;-0.020;-0.010;"
			        		+ "-0.010;-0.020;-0.050;-0.020;-0.030;-0.020;-0.050;-0.030;-0.040;-0.040;"
			        		+ "-0.010;0.030;-0.020;0.070;0.060;0.130;0.100;0.030;-0.020;-0.120;-0.140;"
			        		+ "-0.150;-0.110;-0.130;-0.130;-0.140;-0.120;-0.140;0.080;0.340;0.670;-0.810;"
			        		+ "-1.640;-1.350;-1.110;-0.610;-0.210;-0.110;-0.090;-0.060;-0.030;-0.030;"
			        		+ "0.000;0.000;0.010;0.020;0.040;0.050;0.040;0.060;0.080;0.080;0.100;0.120;"
			        		+ "0.140;0.150;0.170;0.170;0.170;0.160;0.150;0.140;0.100;0.100;0.060;0.020;"
			        		+ "0.000;0.010;-0.020;-0.020;-0.010;-0.020;0.010;0.000;0.000;0.010;0.010;"
			        		+ "0.010;0.020;0.020;0.030;0.020;0.030;0.030;0.020;0.030;0.020;0.010;"
			        		+ "0.010;0.010;-0.010;0.000;0.000;0.010;-0.020;0.000;-0.010;-0.020;"
			        		+ "-0.010;-0.010;-0.020;-0.020;-0.020;-0.020;-0.020;-0.030;-0.020;"
			        		+ "-0.020;-0.040;-0.020;0.020;0.040;0.040;0.080;0.120;0.150;0.060;0.010;"
			        		+ "-0.050;-0.140;-0.110;-0.120;-0.110;-0.100;-0.100;-0.080;-0.100;-0.030;"
			        		+ "0.350;0.610;-0.460;-1.880;-1.900;-1.250;-0.630;-0.260;-0.030;-0.040;0.000;";*/
			        		

			       /* 将字符串转换成字节数组 */  
			       // byte[] buffer = message.getBytes();  
			  
			        /* 开始写入数据 */  
			        //fos.write(buffer);  

			       /* 关闭流的使用 */  
			      //  fos.close();  
			      Toast.makeText(MainActivity.this, "文件写入成功", 1000).show();  
			  }  
			}catch(Exception ex){  
			    Toast.makeText(MainActivity.this, "文件写入失败", 1000).show();  
			}  

		}
	
	
	class BTReadThread extends Thread{//读取数据的线程
		private int wait = 50;// Time to wait
		public BTReadThread(int wait){
			this.wait = wait;
		}
		/** 
		 * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡] 
		 *  
		 * @return 
		 */  
		public  boolean isSdCardExist() {  
		    return Environment.getExternalStorageState().equals(  
		            Environment.MEDIA_MOUNTED);  
		}
		
		/** 
		 * 获取SD卡根目录路径 
		 *  
		 * @return 
		 */  
		public  String getSdCardPath() {  
		    boolean exist = isSdCardExist();  
		    String sdpath = "";  
		    if (exist) {  
		        sdpath = Environment.getExternalStorageDirectory()  
		                .getAbsolutePath();  
		    } else {  
		        sdpath = "不适用";  
		    }  
		    return sdpath;  
		  
		} 
		/** 
		 * 获取默认的文件路径 
		 *  
		 * @return 
		 */  
		public  String getDefaultFilePath() {  
		    String filepath = "";  
		    File file1 = new File(Environment.getExternalStorageDirectory(),  
		            "ECG1.txt");  
		    if (file1.exists()) {  
		        filepath = file1.getAbsolutePath();  
		    } else {  
		        filepath = "不适用";  
		    }  
		    Log.d("MainActivity", "文件路径：-------"+filepath);
		    return filepath;  
		} 
		
		
		public void run(){
			while(enRead){
				
				//Log.d("MainActivity", "文件路径：-------"+Environment.getExternalStorageDirectory());
				//Log.d("MainActivity", "文件路径：-------"+Environment.getExternalStorageDirectory().getAbsolutePath());
					file=new File(Environment.getExternalStorageDirectory(),"wu.txt");
				    //file=new File(getDefaultFilePath());
					
				    try {
				    	//Log.d("MainActivity", "bufferReader对象构建语句---------之前");
						bufferReader=new BufferedReader(new InputStreamReader(new FileInputStream(file)));
						//Log.d("MainActivity", "bufferReader对象构建语句----------之后");
					} catch (FileNotFoundException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
						Log.d("MainActivity", "bufferReader对象构建语句-----------出异常啦");
					}
					 strBuffer=new StringBuffer();
				
					try {
						while((str=bufferReader.readLine())!=null){
							Log.d("MainActivity", "bufferReader读取数据语句-----------之前");
							strBuffer.append(str);
							
						}
						//bufferReader.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						Log.d("MainActivity", "bufferReader读取数据语句-----------出异常了啦");
					}
					revTmpStr=strBuffer.toString();
					
					if(revTmpStr.indexOf(';')!=-1){
						try{
							String ECGDataStrs[] = revTmpStr.split(";");
							for (int i = 0; i < ECGDataStrs.length -1; i++){
								try{
									ECGData = Float.parseFloat(ECGDataStrs[i].replace(';',' '));
									ECGDataList.add(ECGData);											
								}catch(Exception e){
									e.printStackTrace();
									continue;
								}

							}
							if (ECGDataStrs[ECGDataStrs.length -1].length()==6 || ECGDataStrs[ECGDataStrs.length -1].length()==7&&ECGDataStrs[ECGDataStrs.length -1].indexOf('-')==0){
								try{
									ECGData = Float.parseFloat(ECGDataStrs[ECGDataStrs.length -1].replace(';',' '));
									ECGDataList.add(ECGData);
								}catch(Exception e){
									e.printStackTrace();
								}
								revTmpStr = "";
							}
							else{
								revTmpStr = ECGDataStrs[ECGDataStrs.length -1];
							}									
							
						}
						catch(Exception e){
							e.printStackTrace();
						}		
				
			}
		}
	}
}
}
/*
你要得到手机外置sd卡的路径：最好用反射的方法： 
public List getSDPaths(){//得到全部存储地址（SD卡，手机内存）
List paths = new ArrayList();
StorageManager storageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
try {
Class<?>[] paramClasses = {};
Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
getVolumePathsMethod.setAccessible(true);
Object[] params = {};
Object invoke = getVolumePathsMethod.invoke(storageManager, params);
for (int i = 0; i < ((String[])invoke).length; i++) {
paths.add(((String[])invoke)[i]);
Log.i("tag1", "路径："+((String[])invoke)[i]);
}
} catch (NoSuchMethodException e1) {
e1.printStackTrace();
} catch (IllegalArgumentException e) {
e.printStackTrace();
} catch (IllegalAccessException e) {
e.printStackTrace();
} catch (InvocationTargetException e) {
e.printStackTrace();
}
return paths;
}
若集合的大小为2，则有外置的sd卡，
若是自身储存的路径是:list.get(0),外置的sd卡的路径是list.get(1);*/

