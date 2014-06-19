package com.cyclo;

import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cyclo.zvxgw008_srv_entities.v1.ZVXGW008_SRV_EntitiesRequestHandler;
import com.cyclo.zvxgw008_srv_entities.v1.entitytypes.Customer;
import com.cyclo.zvxgw008_srv_entities.v1.helpers.IZVXGW008_SRV_EntitiesRequestHandlerListener;
import com.cyclo.zvxgw008_srv_entities.v1.helpers.ZVXGW008_SRV_EntitiesRequestID;
import com.sap.gwpa.proxy.RequestStatus;
import com.sap.gwpa.proxy.RequestStatus.StatusType;
import com.sap.mobile.lib.request.IResponse;
import com.sap.mobile.lib.supportability.ISDMLogger;
import com.sap.mobile.lib.supportability.SDMLogger;

/**
 * Details screen.
 */
public class KundeDetailsActivity extends ListActivity implements IZVXGW008_SRV_EntitiesRequestHandlerListener 
{
	public static final String TAG = "SIProjekt";
	private ISDMLogger logger;
	
	protected static Customer parentEntry;
	
	// result of the Detail Request
	private Customer entry; 

	// handler for callbacks to the UI thread
	final Handler mHandler = new Handler();
	
	// connectivity error message
	private String emessage = "";
	
	private KundeDetailsAdapter adapter; 
	
	// create runnable for posting
	final Runnable mUpdateResults = new Runnable()
	{
		public void run()
		{
			updateResultsInUi();
		}
	};

	// Refresh UI from background thread
	protected void updateResultsInUi()
	{
		if (entry == null)
		{
			// error occurred
			LinearLayout loadingView = (LinearLayout) findViewById(com.cyclo.R.id.loading_view);
			loadingView.setVisibility(View.GONE);
			Toast.makeText(getApplicationContext(), emessage, Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		adapter = new KundeDetailsAdapter(this, entry);

		setListAdapter(adapter);

		LinearLayout loadingView = (LinearLayout) findViewById(com.cyclo.R.id.loading_view);
		loadingView.setVisibility(View.VISIBLE);
	}

	
    @Override
	protected void onListItemClick(ListView l, View v, int position, long id) 
	{
		String value = adapter.getPropertyValue(position);
		KundeDetailsAdapter.SapSemantics sapSemantics = adapter.getSapSemantics(position);
		if (sapSemantics == null || value == null)
		{
			return;
		}
		
		switch (sapSemantics)
		{
			case tel:   Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + value));
					    startActivity(callIntent);
					    break;
					    
			case email: Intent emailIntent = new Intent(Intent.ACTION_SEND);
						emailIntent.setType("plain/text");
						emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {value});
						startActivity(Intent.createChooser(emailIntent, ""));
				        break;
				        
			case url:   
						if (!value.startsWith("http") && !value.startsWith("HTTP"))
						{
							value = "http://" + value;
						}
						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(value));
					    startActivity(browserIntent);
				        break;
		}
		
		super.onListItemClick(l, v, position, id);
	}
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(com.cyclo.R.layout.details);
		
		Button kunde = (Button) findViewById(com.cyclo.R.id.btnKunde);
		kunde.setOnClickListener(new OnClickListener() {
			
		
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mat = new Intent(getApplicationContext(), KundeListActivity.class);
	  			startActivity(mat);
	  			
	  			
				
			}
		});
		
		Button material = (Button) findViewById(com.cyclo.R.id.btnMaterial);
		material.setOnClickListener(new OnClickListener() {
			
		
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent mat = new Intent(getApplicationContext(), MaterialListActivity.class);
	  			startActivity(mat);
	  			
	  			
				
			}
		});
		
		// initialize the Logger
		logger = new SDMLogger();
		// register to listen to notifications from the Request Handler
		ZVXGW008_SRV_EntitiesRequestHandler.getInstance(getApplicationContext()).register(this, ZVXGW008_SRV_EntitiesRequestID.LOAD_CUSTOMERSET_ENTRY);
		
		// make the request
		// the response should be in "requestCompleted"
		ZVXGW008_SRV_EntitiesRequestHandler.getInstance(getApplicationContext()).loadCustomerSetEntry(parentEntry.getCustomerID());
	}
	
	public void requestCompleted(ZVXGW008_SRV_EntitiesRequestID requestID, List<?> entries, RequestStatus requestStatus) 
	{
		// first check the request's status
		StatusType type = requestStatus.getType();

		if (type == StatusType.OK) 
		{
			if (requestID.equals(ZVXGW008_SRV_EntitiesRequestID.LOAD_CUSTOMERSET_ENTRY))
			{
				// cast to the right type
				this.entry = (Customer) entries.get(0);
				// post in the UI
				mHandler.post(mUpdateResults);
			}
		}
		else
		{
			// do some error handling
			logger.e(TAG, "The request has returned with an error");
			entry = null;
			emessage = requestStatus.getMessage();
			mHandler.post(mUpdateResults);
		}
	}

	public void authenticationNeeded(String message) 
	{
		logger.e(TAG, "Authentication is needed");
		entry = null;
		emessage = message;
		mHandler.post(mUpdateResults);
		// navigate back to login page
		Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
	}
	
	public void batchCompleted(String batchID, IResponse response,
			RequestStatus requestStatus)
	{
		// here you can handle the response of the batch request.
	} 	
}
