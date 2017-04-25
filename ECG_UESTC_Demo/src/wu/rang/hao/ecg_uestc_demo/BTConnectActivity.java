package wu.rang.hao.ecg_uestc_demo;
/*************************************************************/
/* Project Shmimn 
 *  Mobile Health-care Device
 *  Yuhua Chen
 *  2011-4-24 16:35:21
 */
/*************************************************************/
import java.io.IOException;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BTConnectActivity extends Activity {
    /** Called when the activity is first created. */
	public static final int REQUEST_ENABLE_BT = 8807;
	public BroadcastReceiver mBTReceiver;
	public static BluetoothSocket mBTSocket;
	public BluetoothAdapter mBTAdapter;
	private Button btnSearchDevices;
	private ToggleButton tBtnBTSwitch;
	private BluetoothDevice mBTDevice;
	private ArrayAdapter<String> adtDvcs;
	private List<String> lstDvcsStr = new ArrayList<String>();	
	private ListView lvDevicesList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.btconnect);
        //第一步：在AndroidManifest.xml中声明蓝牙权限
        // 第二步：获得BluetoothAdapter对象
        //Init BluetoothAdapter
        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        //判断BluetoothAdapter对象是否为空，若为空，则提示：没有蓝牙设备
        if (mBTAdapter == null){
        	Toast.makeText(BTConnectActivity.this, " No devices supporting Bluetooth! ", Toast.LENGTH_SHORT).show();
        	this.finish();
        }
        
        // Set up BroadCast Receiver
        mBTReceiver = new BroadcastReceiver(){
        	public void onReceive(Context context,Intent intent){
        		String act = intent.getAction();//获取广播中的ACTION
        		// To see whether the action is that already found devices
        		if(act.equals(BluetoothDevice.ACTION_FOUND)){
        			// If found one device, get the device object
        			//如果发现蓝牙设备，则获取蓝牙设备对象
        			BluetoothDevice tmpDvc = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        			// Put the name & address into a string
        			String tmpDvcStr = tmpDvc.getName()+"|"+tmpDvc.getAddress();
        			if (lstDvcsStr.indexOf(tmpDvcStr)==-1){
        				// Avoid duplicate add devices
        				lstDvcsStr.add(tmpDvcStr);
        				adtDvcs.notifyDataSetChanged();
        				Toast.makeText(BTConnectActivity.this, "Find a new device!", Toast.LENGTH_SHORT).show();
        			}
        		}
        		if(act.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
        			Toast.makeText(BTConnectActivity.this, " Searching Complete!", Toast.LENGTH_SHORT).show();
        		}
       		
        		if (act.equals(BluetoothAdapter.ACTION_DISCOVERY_STARTED)){
    				Toast.makeText(BTConnectActivity.this, "Begin Searching Devices", Toast.LENGTH_SHORT).show();
        		}
         	}
        };
        
        //Register the broadcastReceiver
        //下面为在活动中注册广播，也可以在AndroidManifest.xml文件中进行注册
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBTReceiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        registerReceiver(mBTReceiver,filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mBTReceiver,filter); 
        
        //获取各个控件的实例对象--- Init Buttons
        btnSearchDevices = (Button)findViewById(R.id.btnSearchDevice);
        tBtnBTSwitch = (ToggleButton)findViewById(R.id.tBtnBTSwitch);
        lvDevicesList = (ListView)findViewById(R.id.lvDevicesList);
        //判断蓝牙适配器是否打开，来对控件按钮给与不同的显示
        if(mBTAdapter.getState()==BluetoothAdapter.STATE_OFF)
        	tBtnBTSwitch.setChecked(false);
        if(mBTAdapter.getState()==BluetoothAdapter.STATE_ON)
        	tBtnBTSwitch.setChecked(true);

        // ListView And Data Adapter
        adtDvcs = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lstDvcsStr);
        lvDevicesList.setAdapter(adtDvcs);
        
        // Add Click Listeners to Buttons
        //为按钮设置监听事件
        tBtnBTSwitch.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View view){
                // 开启蓝牙----Open Bluetooth
        		if(tBtnBTSwitch.isChecked()){//判断按钮是否为打开状态
        			if(!mBTAdapter.isEnabled()){//mBTAdapter.isEnabled()返回true表示蓝牙以打开，可用，否则启动蓝牙
            			//Open a new dialog to ask user whether wanna open BT
                    	Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    	startActivityForResult(enabler,REQUEST_ENABLE_BT);       				
        			}
        			tBtnBTSwitch.setChecked(true);//控制按钮的状态变为true
        		}
        		
        		// Close Bluetooth
        		if(!tBtnBTSwitch.isChecked()){//功能与上面类似，只是执行相反的操作
        			if(mBTAdapter.isEnabled()){
            			mBTAdapter.disable();
            			Toast.makeText(BTConnectActivity.this, "Bluetooth has been closed!", Toast.LENGTH_SHORT).show();      				
        			}
        			tBtnBTSwitch.setChecked(false);
        		}
        	}
        });
        
        // 第三步：查找本机周围的蓝牙设备----Search Bluetooth Devices
        btnSearchDevices.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View view){
        		if (mBTAdapter.isDiscovering()){
        			Toast.makeText(BTConnectActivity.this, "Already Searching", Toast.LENGTH_SHORT).show();
        		}
    			else{
    				lstDvcsStr.clear();
    				adtDvcs.notifyDataSetChanged();
    				mBTDevice = null;
    				mBTAdapter.startDiscovery();
    			}
        	}
        });
        
        // Create Click listener for the items on devices list
        //为ListView中的子项创建点击事件
        lvDevicesList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
    				long arg3){
        		if (mBTAdapter == null)
        			Toast.makeText(BTConnectActivity.this, "No devices support BlueTooth", Toast.LENGTH_SHORT).show();
        		else{
        			// stop searching
        			mBTAdapter.cancelDiscovery();
        			// Get address of remote device
        			String str = lstDvcsStr.get(arg2);//参数arg2为ListView中的子项在lstDvcsStr中的位置
        			String[] dvcValues = str.split("\\|");//[0]表示的是蓝牙设备的名字，[1]为蓝牙设备的地址
        			String dvcAddr = dvcValues[1];
        			//UUID dvcUUID = UUID.randomUUID();
        			//00001101-0000-1000-8000-00805F9B34FB SPP protocal
        			UUID dvcUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        			// Set BT device
        			mBTDevice = mBTAdapter.getRemoteDevice(dvcAddr);//根据蓝牙的地址获取该蓝牙设备的对象
        			// Connect Device
        			try{
        				mBTSocket = mBTDevice.createRfcommSocketToServiceRecord(dvcUUID);
        				mBTSocket.connect();
        				Intent mInt = new Intent(BTConnectActivity.this,RTMonitorActivity.class);
        				startActivity(mInt);
        			}
        			catch(IOException e){
        				e.printStackTrace();
        			}
        		}        		
        	}
        });        
    }
    
    public void onActivityResult(int RequestCode, int ResultCode,Intent data){
    	switch(RequestCode){
    	case REQUEST_ENABLE_BT:
    		if(ResultCode == RESULT_OK){
    			Toast.makeText(this.getApplicationContext(), "Bluetooth Launched!", Toast.LENGTH_SHORT).show();
    		}
    		else
    			if(ResultCode == RESULT_CANCELED){
        			Toast.makeText(this.getApplicationContext(), "Launched Bluetoth cancled!", Toast.LENGTH_SHORT).show();   				
    			}
    		break;
    	}
    }    
    @Override
	protected void onDestroy() {
	    this.unregisterReceiver(mBTReceiver);
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}