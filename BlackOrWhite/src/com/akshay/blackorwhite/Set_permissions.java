package com.akshay.blackorwhite;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class Set_permissions extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_permissions);
		
		Button set=(Button)findViewById(R.id.set_permissions);
		
	   final ListView l1=(ListView)findViewById(R.id.listView1);
		
	   String []perms={"Internet","Write to SD card","Phone Calls","SMS"};
	   ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice,perms);
	   final SharedPreferences set1=getSharedPreferences("perm_info", 0);
       final SharedPreferences.Editor edit=set1.edit();
       
      
       
	   l1.setAdapter(adapter);
		set.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				SparseBooleanArray a=l1.getCheckedItemPositions();
				
				for(int i=0;i<l1.getCount();i++)
				{
					if(a.get(i))
					{
						edit.putBoolean(""+i, true);
					}
					else
					{
						edit.putBoolean(""+i, false);
					}
				}
				edit.commit();
			}
		});
		
		
		
		
	}
	
	
}
