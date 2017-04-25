package wu.rang.hao.readerwriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	private Button write;
	private Button read;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		write=(Button)findViewById(R.id.write);
		read=(Button)findViewById(R.id.read);
		write.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writeDataToSD();
				
			}
		});
		read.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				readDataFromSD();
			}
		});
	}
	public void writeDataToSD(){  
		try{  
		    /* 获取File对象，确定数据文件的信息 */  
		    //File file  = new File(Environment.getExternalStorageDirectory()+"/f.txt");  
		  File file  = new File(Environment.getExternalStorageDirectory(),"f.txt");  

		   /* 判断sd的外部设置状态是否可以读写 */  
		    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){  
		          
		        /* 流的对象 *//*  */  
		     FileOutputStream fos = new FileOutputStream(file);  

		        /* 需要写入的数据 */  
		        String message = "天气不是很好";  

		       /* 将字符串转换成字节数组 */  
		        byte[] buffer = message.getBytes();  
		  
		        /* 开始写入数据 */  
		        fos.write(buffer);  

		       /* 关闭流的使用 */  
		        fos.close();  
		      Toast.makeText(MainActivity.this, "文件写入成功", 1000).show();  
		  }  
		}catch(Exception ex){  
		    Toast.makeText(MainActivity.this, "文件写入失败", 1000).show();  
		}  

	}
	
	public void readDataFromSD(){  
		  try{  
		         
		       /* 创建File对象，确定需要读取文件的信息 */  
		       File file = new File(Environment.getExternalStorageDirectory(),"f.txt");  
		        
		        /* FileInputSteam 输入流的对象， */    FileInputStream fis = new FileInputStream(file);  
		        /* 准备一个字节数组用户装即将读取的数据 */  
		        byte[] buffer = new byte[fis.available()];  
		        
		      /* 开始进行文件的读取 */  
		     fis.read(buffer);            
		        /* 关闭流  */  
		     fis.close();  
		         
		       /* 将字节数组转换成字符创， 并转换编码的格式 */  
		        String res = EncodingUtils.getString(buffer, "UTF-8");  
		          
		       Toast.makeText(MainActivity.this, "文件读取成功，您读取的数据为："+res, 1000).show();  
		         
		  }catch(Exception ex){  
		       Toast.makeText(MainActivity.this, "文件读取失败！", 1000).show();  
		   }  
		}  
}
