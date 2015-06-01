package com.weatherservice.operations;

import java.lang.ref.WeakReference;

import com.weatherservice.MainActivity;

import android.app.Activity;

/**
 * 
 * @author marastu
 *
 */
public interface RetainedOp {
	/**
     * Called after a runtime configuration change occurs to finish
     * the initialization steps.
     */
    public void onConfigurationChange(WeakReference<MainActivity> activity);

}
