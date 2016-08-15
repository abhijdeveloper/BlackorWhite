package com.akshay.blackorwhite;

import java.io.File;

import java.io.FileOutputStream;
import java.util.List;
import java.util.Locale;



import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartPage extends Activity {

	public String LOCATION=null;
	double LATITUDE = 15.3738579;
	double LONGITUDE = 75.1191676;
	Geocoder geocoder;
	final int maxResult =5;
	String addressList[] = new String[maxResult];
	MyReceiver mr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start_page);
		
		Button black_list=(Button)findViewById(R.id.black);
		Button white_list=(Button)findViewById(R.id.white);
		Button start_service=(Button)findViewById(R.id.start_service);
		Button stop_service=(Button)findViewById(R.id.stop_service);
		Button view_apps=(Button)findViewById(R.id.view_apps);
		
		Button set_perms=(Button)findViewById(R.id.set);
		final TextView white_data=(TextView)findViewById(R.id.white_text);
		final TextView black_data=(TextView)findViewById(R.id.black_text);
		final TextView installed=(TextView)findViewById(R.id.installed_apps);
		mr=new MyReceiver();
		
		
		set_perms.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				
				Intent i1=new Intent(getApplicationContext(), Set_permissions.class);
				startActivity(i1);
			}
		});
		
		
		
		start_service.setOnClickListener(new OnClickListener() 
		{
			
			
			@Override
			public void onClick(View arg0) 
			{
				
			    
			    IntentFilter intentFilter = new IntentFilter();
			    intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
			    intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
			    intentFilter.addDataScheme("package");
			    registerReceiver(mr, intentFilter);
				Toast.makeText(getApplicationContext(), "Started", 1000).show();
			}
		});
		
		
		stop_service.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				unregisterReceiver(mr);
				Toast.makeText(getApplicationContext(), "Stopped", 1000).show();
			}
		});
		
		view_apps.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				black_data.setText("");
				white_data.setText("");
				StringBuffer appNameAndPermissions=new StringBuffer();
				PackageManager pm = getPackageManager();
				List <ApplicationInfo>packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
				for (ApplicationInfo applicationInfo : packages) {
					if ( ( (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
			        {
				Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);
				try {
				PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
				              appNameAndPermissions.append(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString()+":\n");
				 //Get Permissions
				String[] requestedPermissions = packageInfo.requestedPermissions;
				 if(requestedPermissions != null) {
				 for (int i = 0; i < requestedPermissions.length; i++) {
				 Log.d("test", requestedPermissions[i]);
				appNameAndPermissions.append(requestedPermissions[i]+"\n");
				}
				 appNameAndPermissions.append("\n");
				
				}
				} catch (NameNotFoundException e) {
				 e.printStackTrace();
				}}
				}
				installed.setText(appNameAndPermissions);
				
				
			}
		});
		
		
		
		
		black_list.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				GPS_Locator gps;
			    gps=new GPS_Locator(getApplicationContext());
					if(gps.canGetLocation())
				{
					LATITUDE=gps.getLatitude();
					LONGITUDE=gps.getLongitude();
					Toast.makeText(getApplicationContext(), ""+LATITUDE+" "+LONGITUDE, 1000).show();
				}
					else
					{
						gps.showSettingsAlert();
					}
			  
			   
			    //Set the language of Locator
			    geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

			    try {
			    	//Get all the addresses in form of list
			          List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, maxResult);

						if(addresses != null) {
				
						 for (int j=0; j<maxResult; j++){
						  Address returnedAddress = addresses.get(j);
						  StringBuilder strReturnedAddress = new StringBuilder();
						  for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
						   strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
						  }
						  addressList[j] = strReturnedAddress.toString();
						 }
						 
						//Take first 3 addresses for location
						LOCATION=addressList[1]+" "+addressList[2]+" "+addressList[3];
						
						Toast.makeText(getApplicationContext(), "Location of wifi saved "+LOCATION, 4000).show();
						}
						else
						{
							Toast.makeText(getApplicationContext(), "No address found", 4000).show();
						}

			        }catch (Exception e) 
					{
						Toast.makeText(getApplicationContext(), "Cannot get Address"+e.getMessage().toString(), 4000).show();
					}
			    
			    TelephonyManager tMgr = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
			    String mPhoneNumber = tMgr.getLine1Number().toString();
			    String mIMEI=tMgr.getDeviceId().toString();
			    WifiManager wimanager = (WifiManager) getBaseContext().getSystemService(Context.WIFI_SERVICE);
			    wimanager.setWifiEnabled(true);
			    String macAddress = wimanager.getConnectionInfo().getMacAddress();
			    if (macAddress == null) {
			        macAddress = "Device don't have mac address or wi-fi is disabled";
			    }
			    white_data.setText("");
			    black_data.setText("Your current location-"+LOCATION+"\n"+"Your Cell number-"+mPhoneNumber+"\n"+"Your Cell IMEI-"+mIMEI+"\n"+"Your cell MAC address-"+macAddress+"\n");
			    
			    
				
			}
		});
		
		white_list.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
			
				// Device model
				String PhoneModel = android.os.Build.MODEL;

				// Android version
				String AndroidVersion = android.os.Build.VERSION.RELEASE;
				
				String whatsappphoneNumber = null;
				black_data.setText("");
				white_data.setText("Your Phone Model-"+PhoneModel+"\n"+"Your Cell Android Version-"+AndroidVersion+"\n");
				AccountManager am = AccountManager.get(getApplicationContext());
				Account[] accounts = am.getAccounts();

				for (Account ac : accounts) {
				    String acname = ac.name;
				    String actype = ac.type;
				    // Take your time to look at all available accounts
				    white_data.append("Accounts : " + acname + ", " + actype+"\n");
				    if(actype.equals("com.whatsapp")){
				        whatsappphoneNumber = ac.name;
				        
				    }
				}
				white_data.append("WhatsApp Account Number:- "+whatsappphoneNumber+"\n");
				
			}
		});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_page, menu);
		return true;
	}

}
