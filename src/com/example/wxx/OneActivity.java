package com.example.wxx;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.wxx.MainActivity.HttpThread2;
import com.example.wxx.MainActivity.HttpThreada;
import com.example.wxx.MainActivity.MyListAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class OneActivity extends Activity {
	private Handler handler1;
	 private Spinner sp = null; 
	 private Spinner sp1;
	 private Button button10;
	 private Button button11; 
	 private WifiManager wifiManager;
	  private EditText text1;
	  private EditText text2;
	  private EditText text3;
	  private Context mContext;
	  private HttpThreadc thread3;
	  private boolean r=true;
	 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one);
		mContext=this;
		button10=(Button) findViewById(R.id.button10);
		button11=(Button) findViewById(R.id.button11);
		text1=(EditText) findViewById(R.id.editText1);
		text2=(EditText) findViewById(R.id.editText2);
		text3=(EditText) findViewById(R.id.editText3);
		
		sp1=(Spinner) findViewById(R.id.spinner2);
		button10.setOnClickListener(new Listener11());
		button11.setOnClickListener(new Listener12());
		button11.setEnabled(false);
		 add_list(); 
		 new HttpThreadd().start();
		 handler1 = new Handler() {
	            @Override
	            public void handleMessage(Message msg) {
	                super.handleMessage(msg);
                
	              if(msg.arg1==1){
	            	 
	            	  Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
	              }

	            }
	        };
		
		
	}
	
	 @SuppressWarnings("null")
	private void add_list() { 
		 wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
		 List<ScanResult> scanResults=wifiManager.getScanResults();//搜索到的设备列表
	  String[] mCountries =new String[scanResults.size()]; 
		 int i=0;
		
		   for (ScanResult scanResult : scanResults) {  
			   
			   mCountries[i]=scanResult.SSID+"||"+scanResult.BSSID;
			  
			   i++;
		       } 
		   
	        sp = (Spinner) findViewById(R.id.spinner1); 
	        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
	                android.R.layout.simple_spinner_item, mCountries); 
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
	        sp.setAdapter(adapter); 
	    } 

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	//线程
		public class HttpThreadc extends Thread {  
			String url;
		    int time;
		    public volatile boolean exit = false; 

		   
		   

			public void setHttpThread1(String url,int t){
				
				
				this.url=url;
				this.time=t;
				
			
			}
			
			private void goGet(){
				
	            String wifiname=sp.getSelectedItem().toString();
	            
				String direction=sp1.getSelectedItem().toString();
				
				String address=text1.getText().toString();
				
				String equipmentname=text2.getText().toString();
				
				String wifixinxi=getwifi(address,direction,equipmentname,wifiname);
				try {
					String url1=this.url+"?wifilist="+wifixinxi;
					
					URL httpUrl=new URL(url1);
					HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(2000);
					conn.setReadTimeout(2000);
		            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
				     String str;
				     StringBuffer sb=new StringBuffer();
				     while((str=reader.readLine())!=null){
				    	 sb.append(str);
				     }
				  
				    
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}catch(IOException e){
					 
				}
			}
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(!exit){
					goGet();
					try {
						Thread.sleep(this.time*1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
				
			
				
			
						
					
						
				
			
			}
			
			
		}
		//得到信息String
		public String getwifi(String address,String direction,String equipmentname,String wifi){
			
			 ArrayList<wifi> l=new ArrayList<wifi>() ;
			 List<ScanResult> scanResults=wifiManager.getScanResults();//搜索到的设备列表 
			   for (ScanResult scanResult : scanResults) {  
				   if(wifi.equals(scanResult.SSID+"||"+scanResult.BSSID)){
					   wifi w=new wifi();
					   w.setAddress(address);
					   w.setDirection(direction);
					   w.setEquipmentname(equipmentname);
					   w.setWifiname(scanResult.SSID);
					   w.setLevel(scanResult.level+"");
					   w.setMac(scanResult.BSSID+"");
					   l.add(w);  
				   }
				 
			       }  
			     JSONArray jsonArray = new JSONArray();
			      JSONObject tmpObj = null;
			     
			      for(int i = 0; i < l.size(); i++)
			      {
			           tmpObj = new JSONObject();
			           
			           try {
			        	   tmpObj.put("address",l.get(i).getAddress());
			        	   tmpObj.put("direction", l.get(i).getDirection());
			        	   tmpObj.put("equipmentname",l.get(i).getEquipmentname());
			        	   tmpObj.put("wifiname", l.get(i).getWifiname());
						  tmpObj.put("level",l.get(i).getLevel());
						  tmpObj.put("mac", l.get(i).getMac());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
			           jsonArray.put(tmpObj);
			           tmpObj = null;
			      }
			return jsonArray.toString();
		}
		class Listener11 implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 if(wifiManager.isWifiEnabled()){
					 button10.setEnabled(false);	
					 button11.setEnabled(true);
					 String s=text3.getText().toString();
					 thread3=new HttpThreadc();
						
						thread3.setHttpThread1("http://10.80.107.45:8080/web/go",Integer.parseInt(s));
								thread3.start();
								 Toast.makeText(mContext, "开始运行", Toast.LENGTH_SHORT).show();
				       
				       
				 }else{
					 Toast.makeText(mContext, "wife没有开启", Toast.LENGTH_SHORT).show();
				 }
				
				
			}
		
		}
		//得到当前wifi和强度
		public String getnl(){
			String nl=sp.getSelectedItem().toString();
			String ret=null;
			 List<ScanResult> scanResults=wifiManager.getScanResults();//搜索到的设备列表 
			   for (ScanResult scanResult : scanResults) {  
				   if(nl.equals(scanResult.SSID+"||"+scanResult.BSSID)){
					  ret=scanResult.SSID+"-"+scanResult.level;
				   }
				 
			       }  
			   return ret;
		}
		class Listener12 implements OnClickListener{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 button11.setEnabled(false);	
				 button10.setEnabled(true);
				thread3.exit=true;
				 Toast.makeText(mContext, "停止运行", Toast.LENGTH_SHORT).show();
			}
			
		}
		//线程（提示信号强度）
				public class HttpThreadd extends Thread {  
					
					
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						try {
							while(r){
								String nl=getnl();
								Message message = Message.obtain();
					             message.arg1 = 1;
                                 message.obj=nl;
					             handler1.sendMessage(message);
								Thread.sleep(2000);
							}
							 
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
						

				}
				
				
				//返回键监听
				 @Override    
				    public void onBackPressed() {    
				        super.onBackPressed();    
				          r=false;        
				    }    
				      
}
