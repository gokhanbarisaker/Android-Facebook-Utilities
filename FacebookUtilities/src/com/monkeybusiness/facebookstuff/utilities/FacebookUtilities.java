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
import java.lang.ref.WeakReference;
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
	 * Facebook Permissions (http://developers.facebook.com/docs/authentication/permissions/)
	 * 
	 * 	- User and Friends Permissions;
	 */
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the "About Me" section of the profile in the about property */
	public static final String PERMISSION_USER_ABOUT_ME = "user_about_me";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the "About Me" section of the profile in the about property */
	public static final String PERMISSION_FRIENDS_ABOUT_ME = "friends_about_me";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's list of activities as the activities connection */
	public static final String PERMISSION_USER_ACTIVITIES = "user_activities";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's list of activities as the activities connection */
	public static final String PERMISSION_FRIENDS_ACTIVITIES = "friends_activities";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the birthday with year as the birthday property */
	public static final String PERMISSION_USER_BIRTHDAY = "user_birthday";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the birthday with year as the birthday property */
	public static final String PERMISSION_FRIENDS_BIRTHDAY = "friends_birthday";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides read access to the authorized user's check-ins or a friend's check-ins that the user can see. This permission is superseded by user_status for new applications as of March, 2012. */
	public static final String PERMISSION_USER_CHECKINS = "user_checkins";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides read access to the authorized user's check-ins or a friend's check-ins that the user can see. This permission is superseded by user_status for new applications as of March, 2012. */
	public static final String PERMISSION_FRIENDS_CHECKINS = "friends_checkins";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to education history as the education property */
	public static final String PERMISSION_USER_EDUCATION_HISTORY = "user_education_history";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to education history as the education property */
	public static final String PERMISSION_FRIENDS_EDUCATION_HISTORY = "friends_education_history";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the list of events the user is attending as the events connection */
	public static final String PERMISSION_USER_EVENTS = "user_events";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the list of events the user is attending as the events connection */
	public static final String PERMISSION_FRIENDS_EVENTS = "friends_events";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the list of groups the user is a member of as the groups connection */
	public static final String PERMISSION_USER_GROUPS = "user_groups";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the list of groups the user is a member of as the groups connection */
	public static final String PERMISSION_FRIENDS_USER_GROUPS = "user_groups";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's hometown in the hometown property */
	public static final String PERMISSION_USER_HOMETOWN = "user_hometown";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's hometown in the hometown property */
	public static final String PERMISSION_FRIENDS_HOMETOWN = "friends_hometown";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's list of interests as the interests connection */
	public static final String PERMISSION_USER_INTERESTS = "user_interests";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's list of interests as the interests connection */
	public static final String PERMISSION_FRIENDS_INTERESTS = "friends_interests";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the list of all of the pages the user has liked as the likes connection */
	public static final String PERMISSION_USER_LIKES = "user_likes";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the list of all of the pages the user has liked as the likes connection */
	public static final String PERMISSION_FRIENDS_LIKES = "friends_likes";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's current location as the location property */
	public static final String PERMISSION_USER_LOCATION = "user_location";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's current location as the location property */
	public static final String PERMISSION_FRIENDS_lOCATION = "friends_location";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's notes as the notes connection */
	public static final String PERMISSION_USER_NOTES = "user_notes";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's notes as the notes connection */
	public static final String PERMISSION_FRIENDS_NOTES = "friends_notes";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the photos the user has uploaded, and photos the user has been tagged in */
	public static final String PERMISSION_USER_PHOTOS = "user_photos";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the photos the user has uploaded, and photos the user has been tagged in */
	public static final String PERMISSION_FRIENDS_PHOTOS = "friends_photos";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the questions the user or friend has asked */
	public static final String PERMISSION_USER_QUESTIONS = "user_questions";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the questions the user or friend has asked */
	public static final String PERMISSION_FRIENDS_QUESTIONS = "friends_questions";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's family and personal relationships and relationship status */
	public static final String PERMISSION_USER_RELATIONSHIPS = "user_relationships";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's family and personal relationships and relationship status */
	public static final String PERMISSION_FRIENDS_RELATIONSHIPS = "friends_relationships";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's relationship preferences */
	public static final String PERMISSION_USER_RELATIONSHIP_DETAILS = "user_relationship_details";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's relationship preferences */
	public static final String PERMISSION_FRIENDS_RELATIONSHIP_DETAILS = "friends_relationship_details";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's religious and political affiliations */
	public static final String PERMISSION_USER_RELIGION_POLITICS = "user_religion_politics";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's religious and political affiliations */
	public static final String PERMISSION_FRIENDS_RELIGION_POLITICS = "friends_religion_politics";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's status messages and checkins. Please see the documentation for the <a href="http://developers.facebook.com/docs/reference/fql/location_post/">location_post</a> table for information on how this permission may affect retrieval of information about the locations associated with posts. */
	public static final String PERMISSION_USER_STATUS = "user_status";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's status messages and checkins. Please see the documentation for the <a href="http://developers.facebook.com/docs/reference/fql/location_post/">location_post</a> table for information on how this permission may affect retrieval of information about the locations associated with posts. */
	public static final String PERMISSION_FRIENDS_STATUS = "friends_status";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's subscribers and subscribees */
	public static final String PERMISSION_USER_SUBSCRIPTIONS = "user_subscriptions";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's subscribers and subscribees */
	public static final String PERMISSION_FRIENDS_SUBSCRIPTIONS = "friends_subscriptions";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the videos the user has uploaded, and videos the user has been tagged in */
	public static final String PERMISSION_USER_VIDEOS = "user_videos";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the videos the user has uploaded, and videos the user has been tagged in */
	public static final String PERMISSION_FRIENDS_VIDEOS = "friends_videos";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's web site URL */
	public static final String PERMISSION_USER_WEBSITE = "user_website";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's web site URL */
	public static final String PERMISSION_FRIENDS_WEBSITE = "friends_website";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to work history as the work property */
	public static final String PERMISSION_USER_WORK_HISTORY = "user_work_history";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to work history as the work property */
	public static final String PERMISSION_FRIENDS_WORK_HISTORY = "friends_work_history";
	/** <u><b>Permission group:</b></u> User and Friends Permissions<br><br> Provides access to the user's primary email address in the email property. Do not spam users. Your use of email must comply both with <a href="http://www.facebook.com/terms.php">Facebook policies</a> and with the <a href="Provides access to the user's primary email address in the email property. Do not spam users. Your use of email must comply both with Facebook policies and with the CAN-SPAM Act.">CAN-SPAM Act</a>. */
	public static final String PERMISSION_USER_EMAIL = "email";
	
	/*
	 * - Extended permissions
	 */
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides access to any friend lists the user created. All user's friends are provided as part of basic data, this extended permission grants access to the lists of friends a user has created, and should only be requested if your application utilizes lists of friends. */
	public static final String PERMISSION_READ_FRIENDLISTS = "read_friendlists";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides read access to the Insights data for pages, applications, and domains the user owns. */
	public static final String PERMISSION_READ_INSIGHTS = "read_insights";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides the ability to read from a user's Facebook Inbox. */
	public static final String PERMISSION_READ_MAILBOX = "read_mailbox";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides read access to the user's friend requests */
	public static final String PERMISSION_READ_REQUESTS = "read_requests";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides access to all the posts in the user's News Feed and enables your application to perform searches against the user's News Feed */
	public static final String PERMISSION_READ_STREAM = "read_stream";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides applications that integrate with Facebook Chat the ability to log in users. */
	public static final String PERMISSION_XMPP_LOGIN = "xmpp_login";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides the ability to manage ads and call the Facebook Ads API on behalf of a user. */
	public static final String PERMISSION_ADS_MANAGEMENT = "ads_management";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Enables your application to create and modify events on the user's behalf */
	public static final String PERMISSION_CREATE_EVENT = "create_event";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Enables your app to create and edit the user's friend lists. */
	public static final String PERMISSION_MANAGE_FRIENDLISTS = "manage_friendlists";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Enables your app to read notifications and mark them as read. <b>Intended usage</b>: This permission should be used to let users read and act on their notifications; it should not be used to for the purposes of modeling user behavior or data mining. Apps that misuse this permission may be banned from requesting it. */
	public static final String PERMISSION_MANAGE_NOTIFICATIONS = "manage_natifications";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides access to the user's online/offline presence */
	public static final String PERMISSION_USER_ONLINE_PRESENCE = "user_online_presence";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Provides access to the user's friend's online/offline presence */
	public static final String PERMISSION_FRIENDS_ONLINE_PRESENCE = "friends_online_presence";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Enables your app to perform checkins on behalf of the user. */
	public static final String PERMISSION_PUBLISH_CHECKINS = "publish_checkins";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Enables your app to post content, comments, and likes to a user's stream and to the streams of the user's friends. This is a superset <a href="https://developers.facebook.com/docs/publishing/">publishing permission</a> which also includes <b>publish_actions</b>. However, please note that Facebook recommends a user-initiated sharing model. Please read the <a href="https://developers.facebook.com/policy/">Platform Policies<a> to ensure you understand how to properly use this permission. Note, you do <b>not</b> need to request the <b>publish_stream</b> permission in order to use the <a href="https://developers.facebook.com/docs/reference/dialogs/feed/">Feed Dialog</a>, the <a href="https://developers.facebook.com/docs/reference/dialogs/requests/">Requests Dialog</a> or the <a href="https://developers.facebook.com/docs/reference/dialogs/send/">Send Dialog</a>. */
	public static final String PERMISSION_STREAM = "publish_stream";
	/** <u><b>Permission group:</b></u> Extended Permissions<br><br> Enables your application to RSVP to events on the user's behalf */
	public static final String PERMISSION_RSVP_EVENT = "rsvp_event";

	/*
	 * - Open Graph Permissions
	 */
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows your app to publish to the Open Graph using <a href="https://developers.facebook.com/docs/opengraph/actions/builtin/">Built-in Actions</a>, <a href="https://developers.facebook.com/docs/achievements/">Achievements</a>, <a href="https://developers.facebook.com/docs/score/">Scores</a>, or <a href="https://developers.facebook.com/docs/opengraph/define-actions/">Custom Actions</a>. Your app can also publish other activity which is detailed in the <a href="https://developers.facebook.com/docs/publishing/">Publishing Permissions</a> doc. Note: The user-prompt for this permission will be displayed in the first screen of the <a href="https://developers.facebook.com/docs/opengraph/authentication/">Enhanced Auth Dialog</a> and cannot be revoked as part of the authentication flow. However, a user can later revoke this permission in their Account Settings. If you want to be notified if this happens, you should subscribe to the <b>permissions</b> object within the <a href="https://developers.facebook.com/docs/reference/api/realtime/">Realtime API</a>. */
	public static final String PERMISSION_PUBLISH_ACTIONS = "publish_actions";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you to retrieve the actions published by all applications using the built-in <b>music.listens</b> action. */
	public static final String PERMISSION_USER_ACTIONS_MUSIC = "user_actions.music";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you to retrieve the actions published by all applications using the built-in <b>music.listens</b> action. */
	public static final String PERMISSION_FRIENDS_ACTIONS_MUSIC = "friends_actions.music";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you to retrieve the actions published by all applications using the built-in <b>news.reads</b> action. */
	public static final String PERMISSION_USER_ACTIONS_NEWS = "user_actions.news";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you to retrieve the actions published by all applications using the built-in <b>news.reads</b> action. */
	public static final String PERMISSION_FRIENDS_ACTIONS_NEWS = "friends_actions.news";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you to retrieve the actions published by all applications using the built-in <b>video.watches</b> action. */
	public static final String PERMISSION_USER_ACTIONS_VIDEO = "user_actions.video";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you to retrieve the actions published by all applications using the built-in <b>video.watches</b> action. */
	public static final String PERMISSION_FRIENDS_ACTIONS_VIDEO = "friends_actions.video";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you retrieve the actions published by another application as specified by the app namespace. For example, to request the ability to retrieve the actions published by an app which has the namespace <b>awesomeapp</b>, prompt the user for the <b>users_actions:awesomeapp</b> and/or <b>friends_actions:awesomeapp</b> permissions. */
	public static final String PERMISSION_USER_ACTIONS_APP_NAMESPACE = "user_actions:APP_NAMESPACE";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you retrieve the actions published by another application as specified by the app namespace. For example, to request the ability to retrieve the actions published by an app which has the namespace <b>awesomeapp</b>, prompt the user for the <b>users_actions:awesomeapp</b> and/or <b>friends_actions:awesomeapp</b> permissions. */
	public static final String PERMISSION_FRIENDS_ACTIONS_APP_NAMESPACE = "friends_actions:APP_NAMESPACE";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you post and retrieve game achievement activity. */
	public static final String PERMISSION_USER_GAMES_ACTIVITY = "user_games_activity";
	/**  <u><b>Permission group:</b></u> Open Graph Permissions<br><br> Allows you post and retrieve game achievement activity. */
	public static final String PERMISSION_FRIENDS_GAMES_ACTIVITY = "friends_games_activity";

	/*
	 *  - Page Permissions
	 */
	/**  <u><b>Permission group:</b></u> Page Permissions<br><br> Enables your application to retrieve access_tokens for Pages and Applications that the user administrates. The access tokens can be queried by calling <b>/&#60;user_id&#62;/accounts</b> via the Graph API. This permission is only compatible with the Graph API, not the deprecated REST API.<br> See <a href="">here</a> for generating long-lived Page access tokens that do not expire after 60 days. */
	public static final String PERMISSION_MANAGE_PAGES = "manage_pages";
	
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
	
	/**
	 * Invokes login process asynchronously.
	 * 
	 * @param activity
	 * @param permissions
	 * @param handler
	 * @param ssoEnabled
	 */
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
	 * Invokes logout asynchronously. State of logout delivered to given handler on process finish.
	 * 
	 * @param handler
	 */
	public void logout(Handler handler)
	{
		logout(handler, null);
	}
	
	/**
	 * Invokes logout asynchronously. State of logout delivered to given handler on process finish.
	 *
	 * @param handler
	 * @param state
	 */
	public void logout(Handler handler, Object state)
	{
		if(LOG)
		{
			Log.d(TAG, "Logout requested");
		}
		
		if(!facebook.isSessionValid())
		{
			if(LOG)
			{
				Log.d(TAG, "Session is invalid, no need to logout.");
			}
		}
		else
		{
			if(LOG)
			{
				Log.d(TAG, "Session is valid, attempting to logout.");
			}
		
			LogoutRequestListener listener = new LogoutRequestListener(handler);
		
			asyncFacebookRunner.logout(cookieJar, listener, state);
		}
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
	public void onActivityResult(Handler handler, int requestCode, int resultCode, Intent data)
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
				throw new NullPointerException("A custom handler to inform activity with login status notifications has to be decleared. Take AuthenticationHandler as reference, or use it directly for communication");
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
			loginStatusMessage.obj = new Exception(e);
			
			//Inform handler about login status
			boolean messageSended = handler.sendMessage(loginStatusMessage);

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
    
    /**
     * 
     * @author Gökhan Barış Aker (gokhanbarisaker@gmail.com)
     *
     */
    class LogoutRequestListener implements AsyncFacebookRunner.RequestListener
    {
    	private Handler handler;
    	
    	public LogoutRequestListener(Handler handler)
    	{
    		//If handler isn't initialized
			if(handler == null)
			{//Inform developer with exception ^^.
				throw new NullPointerException("A custom handler, to inform activity with logout status notifications, has to be decleared. Take AuthenticationHandler as reference, or use it directly for communication");
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
