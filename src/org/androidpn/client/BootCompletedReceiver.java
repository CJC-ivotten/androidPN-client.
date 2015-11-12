package org.androidpn.client;

import org.androidpn.demoapp.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

/**
 * 接受开机自动启动的广播，实现启动service逻辑
 * @author chenjiacheng
 *
 */
public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences pref = context
				.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		if(pref.getBoolean(Constants.SETTINGS_AUTO_START, true)){
			 // Start the service
	        ServiceManager serviceManager = new ServiceManager(context);
	        serviceManager.setNotificationIcon(R.drawable.notification);
	        serviceManager.startService();

		}
		
		/* // Start the service
        ServiceManager serviceManager = new ServiceManager(context);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
		Toast.makeText(context, "Boot completed!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();*/
		
	}

}
