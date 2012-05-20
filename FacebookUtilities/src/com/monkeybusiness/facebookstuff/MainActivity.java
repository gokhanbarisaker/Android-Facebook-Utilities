package com.monkeybusiness.facebookstuff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.monkeybusiness.facebookstuff.utilities.FacebookUtilities;

public class MainActivity extends Activity 
{
	private static final String FACEBOOK_APPLICATION_ID = "236939399748463";
	private FacebookUtilities facebookUtilities;
	private FacebookUtilities.AuthenticationHandler handler;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        System.out.println("onCreate() invoked");
        
        initializeHandler();
        facebookUtilities = facebookUtilities.getInstance(FACEBOOK_APPLICATION_ID);
        
        findViewById(R.id.button_with_sso).setOnClickListener(new View.OnClickListener() 
        {	
			public void onClick(View v) 
			{
				facebookUtilities.authorize(MainActivity.this, null, handler, true);
			}
		});
        
        findViewById(R.id.button_without_sso).setOnClickListener(new View.OnClickListener() 
        {
			public void onClick(View v) 
			{
				facebookUtilities.authorize(MainActivity.this, null, handler, false);					
			}
		});
        
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() 
        {	
			public void onClick(View v) 
			{
				facebookUtilities.logout(handler);	
			}
		});
    }

	private void initializeHandler() 
	{
		handler = new FacebookUtilities.AuthenticationHandler(getMainLooper()) 
		{	
			@Override
			public void onLogoutSucceess(String response, Object state) 
			{
				System.out.println("Logout succeeded and received at handler.");		
			}
			
			@Override
			public void onLogoutError(Exception e, Object state) 
			{
				System.out.println("Logout failed and received at handler.");
				
				if(e != null)
				{
					e.printStackTrace();
				}	
			}
			
			@Override
			public void onLoginSuccess() 
			{
				System.out.println("Login succeeded and received at handler.");	
			}
			
			@Override
			public void onLoginError(Exception e) 
			{
				System.out.println("Login failed and received at handler.");
				
				if(e != null)
				{
					e.printStackTrace();
				}	
			}
			
			@Override
			public void onLoginCancelled() 
			{
				System.out.println("Login cancelled and received at handler.");						
			}
		};
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
        super.onActivityResult(requestCode, resultCode, data);
        
        facebookUtilities.onActivityResult(requestCode, resultCode, data);
    }
}