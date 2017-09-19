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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {
	private Button button3;
	private ListView listview;
	 private Context mContext;
	 private Button button;
	 private Button button1;
	  private Spinner  spinner1; 
	  private Spinner  spinner2; 
	  private Spinner  spinner3; 
	  private EditText text1;
	  private EditText text2;
	  private WifiManager wifiManager;
		private Handler handler;
		private Button button2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext=this;
	
		wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
		spinner1 = (Spinner)findViewById(R.id.spinner2);
		spinner2 = (Spinner)findViewById(R.id.spinner3);
		spinner3 = (Spinner)findViewById(R.id.spinner4);
		text1=(EditText) findViewById(R.id.text8);
		text2=(EditText) findViewById(R.id.text4);
		button=(Button) findViewById(R.id.button1);
		button1=(Button) findViewById(R.id.button2);
		button1.setOnClickListener(new Listener2());
		 button.setOnClickListener(new Listener1());
		button3=(Button) findViewById(R.id.button3);
		button3.setOnClickListener(new Listener4());
		 handler = new Handler() {
	            @Override
	            public void handleMessage(Message msg) {
	                super.handleMessage(msg);
                    if(msg.arg1==0){
                    	button.setEnabled(true);
                    	 Toast.makeText(mContext, "发送结束", Toast.LENGTH_SHORT).show();
                    }
                    if(msg.arg1==1){
                    	Toast.makeText(mContext, "网络错误/服务器", Toast.LENGTH_SHORT).show();
                    }
	              if(msg.arg1==-1){
	            	 
	            	  ArrayList<wifi> list=new  ArrayList<wifi>();
                    try {
						list=getlist(msg.obj.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            	
	 			     MyListAdapter m=new MyListAdapter(list);
	 			     listview.setAdapter(m);
	              }

	            }
	        };
	
	}
	
	public  ArrayList<wifi> getlist(String js) throws JSONException{
		 ArrayList<wifi> list=new  ArrayList<wifi>();
		 JSONArray arr = new JSONArray(js);  
   	  for (int i = 0; i < arr.length(); i++) {  
   	      JSONObject temp = (JSONObject) arr.get(i);  
   	      wifi w=new wifi();
   	      w.setAddress(temp.getString("address")); 
   	      w.setDirection(temp.getString("direction"));
   	      w.setEquipmentname(temp.getString("equipmentname"));
   	      w.setLevel(temp.getString("level"));
   	      w.setWifiname(temp.getString("wifiname"));
   	     list.add(w);
   	  }  
        return list;
	}
	class Listener1 implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			// TODO Auto-generated method stub
			
			
			String time=spinner2.getSelectedItem().toString();
			String cishu=spinner3.getSelectedItem().toString();
			 
			
			 if(wifiManager.isWifiEnabled()){
				
					button.setEnabled(false);
					 HttpThreada thread=new HttpThreada();
						thread.setHttpThread1("http://10.80.107.45:8080/web/go",Integer.parseInt(time),Integer.parseInt(cishu));
						thread.start();
				
			       
			       
			 }else{
				 Toast.makeText(mContext, "wife没有开启", Toast.LENGTH_SHORT).show();
			 }
			
		      
		    
		}
	
	}
	class Listener2 implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setContentView(R.layout.wifilist);
			listview =(ListView) findViewById(R.id.listview);
			button=(Button) findViewById(R.id.button3);
			button.setOnClickListener(new Listener3());
			new HttpThread2().start();
		}
	
	}
	class Listener3 implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			setContentView(R.layout.activity_main);
			
			
			wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);  
			spinner1 = (Spinner)findViewById(R.id.spinner2);
			spinner2 = (Spinner)findViewById(R.id.spinner3);
			spinner3 = (Spinner)findViewById(R.id.spinner4);
			text1=(EditText) findViewById(R.id.text8);
			text2=(EditText) findViewById(R.id.text4);
			button=(Button) findViewById(R.id.button1);
			button1=(Button) findViewById(R.id.button2);
			button3=(Button) findViewById(R.id.button3);
			button1.setOnClickListener(new Listener2());
			 button.setOnClickListener(new Listener1());
			 button3.setOnClickListener(new Listener4());
		}
	
	}
	class Listener4 implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			  Intent intent = new Intent(); 
			     intent.setClass(MainActivity.this, OneActivity.class);
			     MainActivity.this.startActivity(intent);
		}
	
	}
	//得到信息String
	public String getwifi(String address,String direction,String equipmentname){
		
		 ArrayList<wifi> l=new ArrayList<wifi>() ;
		 List<ScanResult> scanResults=wifiManager.getScanResults();//搜索到的设备列表 
		   for (ScanResult scanResult : scanResults) {  
			   wifi w=new wifi();
			   w.setAddress(address);
			   w.setDirection(direction);
			   w.setEquipmentname(equipmentname);
			   w.setWifiname(scanResult.SSID);
			   w.setLevel(scanResult.level+"");
			   w.setMac(scanResult.BSSID+"");
			   l.add(w);
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
	//线程
	public class HttpThreada extends Thread {  
		String url;
	    int time;
	    int ch;
	   

		public void setHttpThread1(String url,int t,int c){
			
			
			this.url=url;
			this.time=t;
			this.ch=c;
		
		}
		
		private void goGet(){
			
            String direction=spinner1.getSelectedItem().toString();
			
			String address=text1.getText().toString();
			
			String equipmentname=text2.getText().toString();
			String wifixinxi=getwifi(address,direction,equipmentname);
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
				 Message message = Message.obtain();
	             message.arg1 = 1;

	             handler.sendMessage(message);
			}catch(IOException e){
				 Message message = Message.obtain();
	             message.arg1 = 1;

	             handler.sendMessage(message);
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			for(int i=0;i<this.ch;i++){	
				goGet();
				try {
					Thread.sleep(this.time*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			 Message message = Message.obtain();
             message.arg1 = 0;
             handler.sendMessage(message);
		
			
		
					
				
					
			
		
		}
		
		
	}
	public class HttpThread2 extends Thread {

		
		 String dimension;

		private void goGet(){
			
			try {
				
				URL httpUrl=new URL("http://10.80.107.45:8080/web/getwifilist?direction=");
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
			  
			    
			     dimension=sb.toString();
			  str=sb.toString();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch(IOException e){
				
			}
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			goGet();
			 Message message = Message.obtain();
             message.arg1 = -1;
             message.obj=dimension;
             handler.sendMessage(message);
		}
	}
	class MyListAdapter extends BaseAdapter{
	       private ArrayList<wifi> wifilist;
	       public  MyListAdapter(ArrayList<wifi> list){
	    	   this.wifilist=list;
	       }
			
			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return this.wifilist.size();
			}

			@Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView textView=new TextView(mContext);
				
				textView.setText("地点="+this.wifilist.get(position).getAddress()+" wifi名="+this.wifilist.get(position).getWifiname()+"  leve="+this.wifilist.get(position).getLevel());
				
				return textView;
			}
			
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
}
