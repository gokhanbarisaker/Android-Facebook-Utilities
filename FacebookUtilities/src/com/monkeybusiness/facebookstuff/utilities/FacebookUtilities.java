/**
 * Copyright 2012 Gökhan Barış Aker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monkeybusiness.facebookstuff.utilities;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

/**
 * Facebook sdk library wrapper.
 * 
 * <p>
 * Remember to add 
 * </p>
 * 
 * @author Gökhan Barış Aker (gokhanbarisaker@gmail.com)
 *
 */
public class FacebookUtilities 
{
	/************************************************
	 ******************* Variables*******************
	 ************************************************/
	
	/************************************************
	 * LOG
	 */
	/** Log tag */
	private static final String TAG = "FacebookUtilities";
	/** Log switch */
	private static final boolean LOG = true;
	
	/************************************************
	 * Preferences
	 */
	/** Preferences file name, that stores Single Sign On data */
	private static final String PREFERENCES_SSO_DATA = "FacebookUtilities_SSOdata";
	
	/************************************************
	 * Cookie
	 */
	/** Context that store cookies */
	private Context cookieJar;
	
	/************************************************
	 * Facebook
	 */
	/** Facebook object */
	private Facebook facebook;
	/** Asyncronious request runner of Facebook */
	private AsyncFacebookRunner asyncFacebookRunner;
	
	/*
	 * TODO add missing permissions and re-check permissions.
	 */
	
	/*
	 * Facebook Permissions (http://developers.facebook.com/docs/authentication/permissions/)
	 * 
	 * 	- User and Friends Permissions;
	 */
	/** Provides access to the "About Me" section of the profile in the about property */
	public static final String PERMISSION_USER_ABOUT_ME = "user_about_me";
	/** Provides access to the "About Me" section of the profile in the about property */
	public static final String PERMISSION_FRIENDS_ABOUT_ME = "friends_about_me";
	/** Provides access to the user's list of activities as the activities connection */
	public static final String PERMISSION_USER_ACTIVITIES = "user_activities";
	/** Provides access to the user's list of activities as the activities connection */
	public static final String PERMISSION_FRIENDS_ACTIVITIES = "friends_activities";
	/** Provides access to the birthday with year as the birthday property */
	public static final String PERMISSION_USER_BIRTHDAY = "user_birthday";
	/** Provides access to the birthday with year as the birthday property */
	public static final String PERMISSION_FRIENDS_BIRTHDAY = "friends_birthday";
	/** Provides read access to the authorized user's check-ins or a friend's check-ins that the user can see. This permission is superseded by user_status for new applications as of March, 2012. */
	public static final String PERMISSION_USER_CHECKINS = "user_checkins";
	/** Provides read access to the authorized user's check-ins or a friend's check-ins that the user can see. This permission is superseded by user_status for new applications as of March, 2012. */
	public static final String PERMISSION_FRIENDS_CHECKINS = "friends_checkins";
	/** Provides access to education history as the education property */
	public static final String PERMISSION_USER_EDUCATION_HISTORY = "user_education_history";
	/** Provides access to education history as the education property */
	public static final String PERMISSION_FRIENDS_EDUCATION_HISTORY = "friends_education_history";
	/** Provides access to the list of events the user is attending as the events connection */
	public static final String PERMISSION_USER_EVENTS = "user_events";
	/** Provides access to the list of events the user is attending as the events connection */
	public static final String PERMISSION_FRIENDS_EVENTS = "friends_events";
	/** Provides access to the list of groups the user is a member of as the groups connection */
	public static final String PERMISSION_USER_GROUPS = "user_groups";
	/** Provides access to the list of groups the user is a member of as the groups connection */
	public static final String PERMISSION_FRIENDS_USER_GROUPS = "user_groups";
	/** Provides access to the user's hometown in the hometown property */
	public static final String PERMISSION_USER_HOMETOWN = "user_hometown";
	/** Provides access to the user's hometown in the hometown property */
	public static final String PERMISSION_FRIENDS_HOMETOWN = "friends_hometown";
	/** Provides access to the user's list of interests as the interests connection */
	public static final String PERMISSION_USER_INTERESTS = "user_interests";
	/** Provides access to the user's list of interests as the interests connection */
	public static final String PERMISSION_FRIENDS_INTERESTS = "friends_interests";
	/** Provides access to the list of all of the pages the user has liked as the likes connection */
	public static final String PERMISSION_USER_LIKES = "user_likes";
	/** Provides access to the list of all of the pages the user has liked as the likes connection */
	public static final String PERMISSION_FRIENDS_LIKES = "friends_likes";
	/** Provides access to the user's current location as the location property */
	public static final String PERMISSION_USER_LOCATION = "user_location";
	/** Provides access to the user's current location as the location property */
	public static final String PERMISSION_FRIENDS_lOCATION = "friends_location";
	/** Provides access to the user's notes as the notes connection */
	public static final String PERMISSION_USER_NOTES = "user_notes";
	/** Provides access to the user's notes as the notes connection */
	public static final String PERMISSION_FRIENDS_NOTES = "friends_notes";
	/** Provides access to the photos the user has uploaded, and photos the user has been tagged in */
	public static final String PERMISSION_USER_PHOTOS = "user_photos";
	/** Provides access to the photos the user has uploaded, and photos the user has been tagged in */
	public static final String PERMISSION_FRIENDS_PHOTOS = "friends_photos";
	/** Provides access to the questions the user or friend has asked */
	public static final String PERMISSION_USER_QUESTIONS = "user_questions";
	/** Provides access to the questions the user or friend has asked */
	public static final String PERMISSION_FRIENDS_QUESTIONS = "friends_questions";
	/** Provides access to the user's family and personal relationships and relationship status */
	public static final String PERMISSION_USER_RELATIONSHIPS = "user_relationships";
	/** Provides access to the user's family and personal relationships and relationship status */
	public static final String PERMISSION_FRIENDS_RELATIONSHIPS = "friends_relationships";
	/** Provides access to the user's relationship preferences */
	public static final String PERMISSION_USER_RELATIONSHIP_DETAILS = "user_relationship_details";
	/** Provides access to the user's relationship preferences */
	public static final String PERMISSION_FRIENDS_RELATIONSHIP_DETAILS = "friends_relationship_details";
	/** Provides access to the user's religious and political affiliations */
	public static final String PERMISSION_USER_RELIGION_POLITICS = "user_religion_politics";
	/** Provides access to the user's religious and political affiliations */
	public static final String PERMISSION_FRIENDS_RELIGION_POLITICS = "friends_religion_politics";
	/** Provides access to the user's status messages and checkins. Please see the documentation for the <a href="http://developers.facebook.com/docs/reference/fql/location_post/">location_post</a> table for information on how this permission may affect retrieval of information about the locations associated with posts. */
	public static final String PERMISSION_USER_STATUS = "user_status";
	/** Provides access to the user's status messages and checkins. Please see the documentation for the <a href="http://developers.facebook.com/docs/reference/fql/location_post/">location_post</a> table for information on how this permission may affect retrieval of information about the locations associated with posts. */
	public static final String PERMISSION_FRIENDS_STATUS = "friends_status";
	/** Provides access to the user's subscribers and subscribees */
	public static final String PERMISSION_USER_SUBSCRIPTIONS = "user_subscriptions";
	/** Provides access to the user's subscribers and subscribees */
	public static final String PERMISSION_FRIENDS_SUBSCRIPTIONS = "friends_subscriptions";
	/** Provides access to the videos the user has uploaded, and videos the user has been tagged in */
	public static final String PERMISSION_USER_VIDEOS = "user_videos";
	/** Provides access to the videos the user has uploaded, and videos the user has been tagged in */
	public static final String PERMISSION_FRIENDS_VIDEOS = "friends_videos";
	/** Provides access to the user's web site URL */
	public static final String PERMISSION_USER_WEBSITE = "user_website";
	/** Provides access to the user's web site URL */
	public static final String PERMISSION_FRIENDS_WEBSITE = "friends_website";
	/** Provides access to work history as the work property */
	public static final String PERMISSION_USER_WORK_HISTORY = "user_work_history";
	/** Provides access to work history as the work property */
	public static final String PERMISSION_FRIENDS_WORK_HISTORY = "friends_work_history";
	/** Provides access to the user's primary email address in the email property. Do not spam users. Your use of email must comply both with <a href="http://www.facebook.com/terms.php">Facebook policies</a> and with the <a href="Provides access to the user's primary email address in the email property. Do not spam users. Your use of email must comply both with Facebook policies and with the CAN-SPAM Act.">CAN-SPAM Act</a>. */
	public static final String PERMISSION_USER_EMAIL = "email";
	
	
	/************************************************
	 * Self Instances
	 */
	//Map for pre-initialized FacebookUtility
	private static Map<String, FacebookUtilities> selfInstances = new HashMap<String, FacebookUtilities>();
	
	/************************************************
	 ********* Factories and Contructors ************
	 ************************************************/
	
	/**
	 * Default initializer for FacebookUtilities
	 * 
	 * @param appId appId generated via Facebook for this application.
	 * @return if FacebookUtilities previously initialized, returns it. Else, new FacebookUtilities.
	 */
	public static FacebookUtilities getInstance(String appId)
	{
		FacebookUtilities output = selfInstances.get(appId);
		
		//If we do not poses pre-crafted instance as product
		if(output == null)
		{//Produce new instance
			
			if(LOG)
			{
				Log.d(TAG, "Creating new FacebookUtilities instance for appId: " + appId);
			}
			
			//Initialize FacebookUtilities for corresponding appId
			output = new FacebookUtilities(appId);
			
			//Store new instance on Map for later usage
			selfInstances.put(appId, output);
		}
		
		if(LOG)
		{
			Log.d(TAG, "Returning FacebookUtilities instance of appId: " + appId);
		}
		
		return output;
	}
	
	private FacebookUtilities(String appId)
	{
		//Initialize facebook object
		facebook = new Facebook(appId);
		//Initialize asych facebook runner
		asyncFacebookRunner = new AsyncFacebookRunner(facebook);
	}
	
	/************************************************
	 ****************** Methods *********************
	 ************************************************/
	
	public void authorize(final Activity activity, List<String> permissions, final Handler handler, boolean ssoEnabled)
	{
		if(LOG)
		{
			Log.d(TAG, "Login requested");
		}
		
		if(facebook.isSessionValid())
		{
			if(LOG)
			{
				Log.d(TAG, "Session is valid, no need to re-authorize.");
			}
		}
		else
		{
			if(LOG)
			{
				Log.d(TAG, "Session is invalid, attempting to authorize.");
			}
			
			/*
	         * Get existing access_token if any
	         */
			//Get shared preferences file of SSO data.
	        SharedPreferences ssoPrefs = activity.getSharedPreferences(PREFERENCES_SSO_DATA, Context.MODE_PRIVATE);
	        //Get access token
	        String access_token = ssoPrefs.getString(("access_token_" + facebook.getAppId()), null);
	        //Get expire data.
	        long expires = ssoPrefs.getLong(("access_expires_" + facebook.getAppId()), 0);
	        
	        //If we have have pre-fetched access token
	        if(access_token != null) 
	        {//Use it on authentication
	            facebook.setAccessToken(access_token);
	        }
	        //If we have expire data
	        if(expires != 0) 
	        {//Use it on authentication
	            facebook.setAccessExpires(expires);
	        }
	        
	        //Copy the reference of Context that store cookies, in order to eat cookies at logout ^^.
	        cookieJar = activity;
	        
	        //Change format of permissions from List to String array.
	        String[] loginPermissions = ((permissions == null) ? (new String[]{""}) : ((String []) permissions.toArray()));
	        
	        //If Single Sign On (SSO) enabled
	        if(ssoEnabled)
	        {//Authorize with SSO
	        	facebook.authorize(activity, loginPermissions, new LoginDialogListener(handler, ssoPrefs));
	        }
	        else
	        {//Bypass SSO data and force view authorization dialog of facebook.
	        	facebook.authorize(activity, loginPermissions, Facebook.FORCE_DIALOG_AUTH, new LoginDialogListener(handler, ssoPrefs));
	        }
		}
	}
	
	/**
	 * Invokes logout asynchronously.
	 * 
	 * @param handler
	 */
	public void logout(Handler handler)
	{
		logout(handler, null);
	}
	
	/**
	 * Invokes logout asynchronously.
	 *
	 * @param handler
	 * @param state
	 */
	public void logout(final Handler handler, Object state)
	{
		LogoutRequestListener listener = new LogoutRequestListener(handler);
		
		asyncFacebookRunner.logout(cookieJar, listener, state);
	}
	
	/**
	 * <p>
	 * Call this Activity's onActivityResult(...) method. This method calls the Facebook.authorizeCallback(). Quoting from Facebook.authorizeCallback(...);
	 * </p>
	 * IMPORTANT: This method must be invoked at the top of the calling activity's onActivityResult() function or Facebook authentication will not function properly! If your calling activity does not currently implement onActivityResult(), you must implement it and include a call to this method if you intend to use the authorize() method in this SDK. For more information, see http://developer.android.com/reference/android/app/ Activity.html#onActivityResult(int, int, android.content.Intent)
	 *
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
        System.out.println("Activity resulted with; requestCode: " + requestCode + ", resultCode: " + resultCode);
		
		facebook.authorizeCallback(requestCode, resultCode, data);
	}
	
	/**
	 * With the deprecation of offline_access, you need to extend your access_token every time a user opens your app. To do this, call FacebookUtilities method extendAccessTokenIfNeeded in your activity's onResume() function:
	 * 
	 * @param context
	 */
	public void onResume(Context context)
	{
        facebook.extendAccessTokenIfNeeded(context, null);
    }
    
	/************************************************
	 **************** Sub-Classes *******************
	 ************************************************/
	
	/**
	 * 
	 * @author Gökhan Barış Aker (gokhanbarisaker@gmail.com)
	 *
	 */
    class LoginDialogListener implements Facebook.DialogListener
    {
    	private Handler handler;
    	private SharedPreferences ssoPreferences;
    	
		public LoginDialogListener(Handler handler, SharedPreferences ssoPreferences) throws NullPointerException
		{
			//If handler isn't initialized
			if(handler == null)
			{//Inform developer with exception ^^.
				throw new NullPointerException("A custom handler to inform activity with login status notifications has to be decleared. Take LoginHandler as reference, or use it directly for communication");
			}
			else
			{
				//Copy handlers reference for later use in communication.
				this.handler = handler;
				//Copy preferences reference for later use in storing fetched SSO data.
				this.ssoPreferences = ssoPreferences;
			}
		}

		public void onComplete(Bundle values) 
		{
			if(LOG)
			{
				Log.d(TAG, "Facebook login completed (successful).");
			}
			
			if(ssoPreferences != null)
			{
				//Store fetched Single Sign On data on preferences
				SharedPreferences.Editor editor = ssoPreferences.edit();
				editor.putString(("access_token_" + facebook.getAppId()), facebook.getAccessToken());
				editor.putLong(("access_expires_" + facebook.getAppId()), facebook.getAccessExpires());
				editor.commit();
			}
			
			//Inform handler about login status
			boolean messageSended = handler.sendEmptyMessage(AuthenticationHandler.MESSAGE_LOGIN_SUCCESFUL);
			
			if(LOG)
			{
				Log.d(TAG, "login success message sended: " + messageSended);
			}
		}

		public void onFacebookError(FacebookError e) 
		{
			if(LOG)
			{
				Log.e(TAG, "Facebook login error.");
				e.printStackTrace();
			}
			
			//Obtain message from message pool
			Message loginStatusMessage = Message.obtain(handler);
			//Identify message as error
			loginStatusMessage.what = AuthenticationHandler.MESSAGE_LOGIN_ERROR;
			//Attach error object to message
			loginStatusMessage.obj = e;
			
			//Inform handler about login status
			boolean messageSended = handler.sendMessage(loginStatusMessage);
			//boolean messageSended = handler.sendEmptyMessage(LoginHandler.MESSAGE_LOGIN_ERROR);
			
			if(LOG)
			{
				Log.d(TAG, "login error message sended: " + messageSended);
			}
		}

		public void onError(DialogError e) 
		{	
			if(LOG)
			{
				Log.e(TAG, "Facebook login error.");
				e.printStackTrace();
			}
			
			//Obtain message from message pool
			Message loginStatusMessage = Message.obtain(handler);
			//Identify message as error
			loginStatusMessage.what = AuthenticationHandler.MESSAGE_LOGIN_ERROR;
			//Attach error object to message
			loginStatusMessage.obj = e;
			
			//Inform handler about login status
			boolean messageSended = handler.sendMessage(loginStatusMessage);
			//boolean messageSended = handler.sendEmptyMessage(LoginHandler.MESSAGE_LOGIN_ERROR);

			if(LOG)
			{
				Log.d(TAG, "login error message sended: " + messageSended);
			}
		}

		public void onCancel() 
		{
			if(LOG)
			{
				Log.e(TAG, "Facebook login cancelled.");
			}
			
			//Inform handler about login status
			boolean messageSended = handler.sendEmptyMessage(AuthenticationHandler.MESSAGE_LOGIN_CANCELLED);
			
			if(LOG)
			{
				Log.d(TAG, "login cancel message sended: " + messageSended);
			}
		}
    }
    
    class LogoutRequestListener implements AsyncFacebookRunner.RequestListener
    {
    	private Handler handler;
    	
    	public LogoutRequestListener(Handler handler)
    	{
    		//If handler isn't initialized
			if(handler == null)
			{//Inform developer with exception ^^.
				throw new NullPointerException("A custom handler to inform activity with login status notifications has to be decleared. Take LoginHandler as reference, or use it directly for communication");
			}
			else
			{
				//Copy handlers reference for later use in communication.
				this.handler = handler;
			}
    	}

		public void onComplete(String response, Object state) 
		{
			if(LOG)
			{
				Log.d(TAG, "Facebook logout completed (successful).");
			}
			
			//Obtain message from message pool
			Message logoutStatusMessage = Message.obtain(handler);
			//Identify message as error
			logoutStatusMessage.what = AuthenticationHandler.MESSAGE_LOGOUT_SUCCESSFUL;
			//Glue extra objects in a Map
			HashMap<Integer, Object> attachments = new HashMap<Integer, Object>();
			attachments.put(AuthenticationHandler.LOGOUT_RESPONSE, response);
			attachments.put(AuthenticationHandler.LOGOUT_STATE, state);
			//Attach objects to message
			logoutStatusMessage.obj = attachments;
			
			//Inform handler about logout status
			boolean messageSended = handler.sendMessage(logoutStatusMessage);
			
			if(LOG)
			{
				Log.d(TAG, "logout success message sended: " + messageSended);
			}
		}

		public void onIOException(IOException e, Object state) 
		{
			if(LOG)
			{
				Log.d(TAG, "Facebook logout error.");
			}
			
			//Obtain message from message pool
			Message logoutStatusMessage = Message.obtain(handler);
			//Identify message as error
			logoutStatusMessage.what = AuthenticationHandler.MESSAGE_LOGOUT_ERROR;
			//Glue extra objects in a Map
			HashMap<Integer, Object> attachments = new HashMap<Integer, Object>();
			attachments.put(AuthenticationHandler.LOGOUT_EXCEPTION, e);
			attachments.put(AuthenticationHandler.LOGOUT_STATE, state);
			//Attach objects to message
			logoutStatusMessage.obj = attachments;
			
			//Inform handler about logout status
			boolean messageSended = handler.sendMessage(logoutStatusMessage);
			
			if(LOG)
			{
				Log.d(TAG, "logout error message sended: " + messageSended);
			}
		}

		public void onFileNotFoundException(FileNotFoundException e, Object state) 
		{
			if(LOG)
			{
				Log.d(TAG, "Facebook logout error.");
			}
			
			//Obtain message from message pool
			Message logoutStatusMessage = Message.obtain(handler);
			//Identify message as error
			logoutStatusMessage.what = AuthenticationHandler.MESSAGE_LOGOUT_ERROR;
			//Glue extra objects in a Map
			HashMap<Integer, Object> attachments = new HashMap<Integer, Object>();
			attachments.put(AuthenticationHandler.LOGOUT_EXCEPTION, e);
			attachments.put(AuthenticationHandler.LOGOUT_STATE, state);
			//Attach objects to message
			logoutStatusMessage.obj = attachments;
			
			//Inform handler about logout status
			boolean messageSended = handler.sendMessage(logoutStatusMessage);
			
			if(LOG)
			{
				Log.d(TAG, "logout error message sended: " + messageSended);
			}
		}

		public void onMalformedURLException(MalformedURLException e, Object state) 
		{
			if(LOG)
			{
				Log.d(TAG, "Facebook logout error.");
			}
			
			//Obtain message from message pool
			Message logoutStatusMessage = Message.obtain(handler);
			//Identify message as error
			logoutStatusMessage.what = AuthenticationHandler.MESSAGE_LOGOUT_ERROR;
			//Glue extra objects in a Map
			HashMap<Integer, Object> attachments = new HashMap<Integer, Object>();
			attachments.put(AuthenticationHandler.LOGOUT_EXCEPTION, e);
			attachments.put(AuthenticationHandler.LOGOUT_STATE, state);
			//Attach objects to message
			logoutStatusMessage.obj = attachments;
			
			//Inform handler about logout status
			boolean messageSended = handler.sendMessage(logoutStatusMessage);
			
			if(LOG)
			{
				Log.d(TAG, "logout error message sended: " + messageSended);
			}
		}

		public void onFacebookError(FacebookError e, Object state) 
		{
			if(LOG)
			{
				Log.d(TAG, "Facebook logout error.");
			}
			
			//Obtain message from message pool
			Message logoutStatusMessage = Message.obtain(handler);
			//Identify message as error
			logoutStatusMessage.what = AuthenticationHandler.MESSAGE_LOGOUT_ERROR;
			//Glue extra objects in a Map
			HashMap<Integer, Object> attachments = new HashMap<Integer, Object>();
			attachments.put(AuthenticationHandler.LOGOUT_EXCEPTION, e);
			attachments.put(AuthenticationHandler.LOGOUT_STATE, state);
			//Attach objects to message
			logoutStatusMessage.obj = attachments;
			
			//Inform handler about logout status
			boolean messageSended = handler.sendMessage(logoutStatusMessage);
			
			if(LOG)
			{
				Log.d(TAG, "logout error message sended: " + messageSended);
			}	
		}
    }
    
    /**
     * Sample sub-class of handler with login status handling capacity.
     * 
     * @author Gökhan Barış Aker (gokhanbarisaker@gmail.com)
     *
     */
    public static abstract class AuthenticationHandler extends Handler
    {
    	/************************************************
    	 * Messages
    	 */
    	/** Login successful */
    	public static final int MESSAGE_LOGIN_SUCCESFUL = 93535;
    	/** Error during login operation */
    	public static final int MESSAGE_LOGIN_ERROR = 93536;
    	/** Login operation cancelled */
    	public static final int MESSAGE_LOGIN_CANCELLED = 93537;
    	/** Logout successful */
    	public static final int MESSAGE_LOGOUT_SUCCESSFUL = 93538;
    	/** Error during logout operation */
    	public static final int MESSAGE_LOGOUT_ERROR = 93539;
    	
    	/*************************************************
    	 * Attachment keys
    	 */
    	/** Exception key */
    	public static final int LOGOUT_EXCEPTION = 0;
    	/** Response key */
    	public static final int LOGOUT_RESPONSE = 1;
    	/** State key */
    	public static final int LOGOUT_STATE = 2;
    	
    	
    	/**
    	 * Constructor
    	 * 
    	 * @param looper Some looper related to Application. (eg. Activity.getMainLooper(), or Looper.myLooper())
    	 */
    	public AuthenticationHandler(Looper looper)
    	{
    		super(looper);
    	}

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) 
		{
			switch(msg.what)
			{
				case MESSAGE_LOGIN_SUCCESFUL:
				{
					onLoginSuccess();
					
					break;
				}
				case MESSAGE_LOGIN_ERROR:
				{
					Exception receivedException = (Exception) msg.obj;
					
					onLoginError(receivedException);
					
					break;
				}
				case MESSAGE_LOGIN_CANCELLED:
				{
					onLoginCancelled();
					
					break;
				}
				case MESSAGE_LOGOUT_SUCCESSFUL:
				{
					Map<Integer, Object> attachments = (Map<Integer, Object>) msg.obj;
					String response = (String) attachments.get(AuthenticationHandler.LOGOUT_RESPONSE);
					Object state = attachments.get(AuthenticationHandler.LOGOUT_STATE);
					
					onLogoutSucceess(response, state);
					
					break;
				}
				case MESSAGE_LOGOUT_ERROR:
				{
					Map<Integer, Object> attachments = (Map<Integer, Object>) msg.obj;
					Exception e = (Exception) attachments.get(AuthenticationHandler.LOGOUT_EXCEPTION);
					Object state = attachments.get(AuthenticationHandler.LOGOUT_STATE);
					
					onLogoutError(e, state);
					
					break;
				}
				default:
				{
					super.handleMessage(msg);
					
					break;
				}
			}
		}
		
		/**
		 * Fill with operations that need to be done after successful login.
		 */
		abstract public void onLoginSuccess();
		
		/**
		 * Fill with operations that need to be done after error received during login.
		 * 
		 * @param e Exception related to error
		 */
		abstract public void onLoginError(Exception e);
		
		/**
		 * Fill with operations that need to be done after login cancelled before completion.
		 */
		abstract public void onLoginCancelled();
		
		/**
		 * Fill with operations that need to be done after successful logout.
		 * 
		 * @param response
		 * @param state
		 */
		abstract public void onLogoutSucceess(String response, Object state);
		
		/**
		 * Fill with operations that need to be done after error received during login.
		 * 
		 * @param e
		 * @param state
		 */
		abstract public void onLogoutError(Exception e, Object state);
    }
}
