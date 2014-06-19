package com.cyclo;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


/**
 * Login screen
 */
public class Startseite extends Activity 
{
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(com.cyclo.R.layout.startseite);
		
		Button material = (Button) findViewById(com.cyclo.R.id.btnMaterial);
		material.setOnClickListener(new OnClickListener() {
			
		
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mat = new Intent(getApplicationContext(), MaterialListActivity.class);
	  			startActivity(mat);
	  			
	  			
				
			}
		});
		
		Button kunde = (Button) findViewById(com.cyclo.R.id.btnKunde);
		kunde.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent kun = new Intent(getApplicationContext(), KundeListActivity.class);
	  			startActivity(kun);
			}});
	}
	
}
