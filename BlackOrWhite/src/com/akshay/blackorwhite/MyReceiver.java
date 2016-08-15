package com.akshay.blackorwhite;

import java.util.List;



import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver
{

	@Override
	public void onReceive(Context arg0, Intent arg1) 
	{
	
		Log.i("Enter", "Enters here");
		Uri data = arg1.getData();
        String packageName = data.getEncodedSchemeSpecificPart();
        String perm=null;
        Toast.makeText(arg0, "App Installed!!!!. "+packageName, Toast.LENGTH_LONG).show();
        StringBuffer appNameAndPermissions=new StringBuffer();
		PackageManager pm = arg0.getPackageManager();
		List <ApplicationInfo>packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			if ( ( (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) != true)
	        {
		Log.d("test", "App: " + applicationInfo.name + " Package: " + applicationInfo.packageName);
		try {
		PackageInfo packageInfo = pm.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
		              appNameAndPermissions.append(packageInfo.applicationInfo.loadLabel(arg0.getPackageManager()).toString()+":\n");
		 //Get Permissions
		String[] requestedPermissions = packageInfo.requestedPermissions;
		 if(requestedPermissions != null) {
		// for (int i = 0; i < requestedPermissions.length; i++) {
		// Log.d("test", requestedPermissions[i]);
		appNameAndPermissions.append(requestedPermissions[0]+"\n");
		//}
		 appNameAndPermissions.append("\n");
		perm=requestedPermissions[0]+"\n";
		}
		} catch (NameNotFoundException e) {
		 e.printStackTrace();
		}}
		}
		String peri[];
		String ppr[]={"android.permission.INTERNET","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.CALL_PHONE","android.permission.SEND_SMS"};
		peri=perm.split("\n");
		int hh=0;
		SharedPreferences sh=arg0.getSharedPreferences("perm_info", 0);
		for(int i=0;i<4;i++)
		{
			if(sh.getBoolean(""+i, false))
			{
				for(int j=0;j<peri.length;j++)
				{
					if(ppr[i].equals(peri[j]))
					{
						hh++;
					}
				}
			}
			
		}
		if(hh>0)
		{
		Intent i1=new Intent(arg0, ShowMessage.class);
		i1.putExtra("pack", packageName);
		i1.putExtra("perm", perm);
		i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		arg0.startActivity(i1);
		}
	}

}
