package com.weatherservice;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
/**
 * This abstract class extends the Activity and override its lifecycle methods for logging various lifecycle events
 * */
public abstract class LifecycleLoggingActivity extends Activity{
   /**
    * Debuuging tag used by the android logger.
    */
	
	protected final String TAG=getClass().getSimpleName();
	/**
	 * hook method called when a new instance of the activity is created
	 */
	@Override
	 protected void onCreate(Bundle savedInstanceState){
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);
		
		if(savedInstanceState != null){
			/*activity is being recreated*/
			Log.d(TAG, "OnCreate: activity recreated");
		}
		else{
			/*activity is created anew*/
			Log.d(TAG, "OnCraete: activity created anew");
		}
	}
	/**
	 * Hook method called after oncreate() or onretsart().
	 * The activity should reaquire resources relinquished when the activity was stopped
	 * Or should acquire resources when the activity is created new.
	 * 	 */
	@Override
	protected void onStart(){
		/**
		 * always call super class for necessary initializations
		 */super.onStart();
		 Log.d(TAG, "onStart:activity started and is about to become visible");
		
	}
	/**
	 * onResume() is called immediately after onStart()
	 *  Place to
     * start animations, acquire exclusive resources, such as the
     * camera.
	 */
	@Override
	protected void onResume(){
		super.onResume();
		Log.d(TAG, "onResume :activity is visible/now resumed");
	}
	
	/**hook method called when an activity losses focus but is still visible in background.
	 * Delegate more CPU intensive operation to onStop
     * for seamless transition to next activity.  Save persistent
     * state (onSaveInstanceState()) in case app is killed. Often used
     * to release exclusive resources.
	 * 
	 */
	@Override
	protected void onPause(){
		super.onPause();
		Log.d(TAG, "onPause: another activity is taking focus/this activity is being paused");
		
	}
	/**
	 * hook method called when the activity is no longer visible.
	 * Release resourses at this time to avoid memory leaks.
	 * save instance state in case activity is  killed
	 *  */
	
	@Override
	protected void onStop(){
		super.onStop();
		Log.d(TAG, "onstop: actiivty is no longer visible");
		
	}
	/**
	 * hook method called when the activity is re started after being stopped.
	
	 */
	@Override 
	protected void onRestart(){
	
		super.onRestart();
		Log.d(TAG, "onRestart: activity restarted");
		
	}
	/**
	 * A final chance to release all the resources and 
	 * stop spawned threads.
	 * onDestroy() may not be always called when system kills hosting process.
	 **/ 
	@Override 
	protected void onDestroy(){
		super.onDestroy();
		Log.d(TAG, "activity is about to be destroyed");
	}
			
}
