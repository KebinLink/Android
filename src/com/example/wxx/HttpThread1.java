package com.example.wxx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.view.inputmethod.InputBinding;

public class HttpThread1 extends Thread {

	String url;
	String l;
    int time;
    int ch;
   

	public void set(String url,String list,int t,int c){
		
		url=url+"?wifilist="+list;
		this.url=url;
		this.l=list;
		this.time=t;
		this.ch=c;
	
	}
	
	private void goGet(){
		
		try {
			URL httpUrl=new URL(url);
			HttpURLConnection conn=(HttpURLConnection)httpUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
            BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
		     String str;
		     StringBuffer sb=new StringBuffer();
		     while((str=reader.readLine())!=null){
		    	 sb.append(str);
		     }
		     System.out.println(sb.toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(IOException e){
			
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
		
	
	}
}
