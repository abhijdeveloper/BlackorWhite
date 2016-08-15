package com.akshay.blackorwhite;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShowMessage extends Activity
{

@Override
protected void onCreate(Bundle savedInstanceState) 
{

	super.onCreate(savedInstanceState);
	
	setContentView(R.layout.show_details);
	
	TextView t1=(TextView)findViewById(R.id.package_name);
	
	TextView t2=(TextView)findViewById(R.id.permissions);
	
	Button yes=(Button)findViewById(R.id.yes);
	
	Button no=(Button)findViewById(R.id.no);
	
	t1.setText(""+getIntent().getStringExtra("pack"));
	
	t2.setText(""+getIntent().getStringExtra("perm"));
	
	no.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View arg0) 
		{
			Uri packageURI = Uri.parse("package:"+getIntent().getStringExtra("pack"));
			Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
			startActivity(uninstallIntent);
			finish();
			System.exit(0);
			
		}
	});
	
	yes.setOnClickListener(new OnClickListener() 
	{
		
		@Override
		public void onClick(View arg0) 
		{
			finish();
			System.exit(0);
			
		}
	});
}

}
