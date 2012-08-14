/*
 * Shake - Android gesture recognition and mapping based off of Wiigee
 * Copyright (C) 2012 Curtis Larson
 *
 *
 * This file is part of Shake.
 *
 * Shake is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
package com.quackware.shake;

import com.quackware.shake.wiigee.device.AndroidDevice;
import com.quackware.shake.wiigee.event.ActionEvent;
import com.quackware.shake.wiigee.event.GestureListener;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShakeActivity extends Activity {
	
	private AccelerometerService mBoundService;
	private boolean mIsBound;
	
	private AndroidDevice mAndroidDevice;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        setupButtonListeners();
        
        
    }
    
    private void setupButtonListeners()
    {
    	((Button)findViewById(R.id.trainButton)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				doBindService(ActionEvent.TRAIN_ACTION);
			}});
    	((Button)findViewById(R.id.recognizeButton)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				doBindService(ActionEvent.RECOGNITION_ACTION);
			}});
    	((Button)findViewById(R.id.saveTrainingButton)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {	
				((Shake)getApplicationContext()).getAndroidDevice().fireActionStartEvent(ActionEvent.ANALYZE_TRAIN_ACTION);
				((Shake)getApplicationContext()).getAndroidDevice().fireActionStopEvent(ActionEvent.ANALYZE_TRAIN_ACTION);
			}});
    	
    	((Button)findViewById(R.id.stopButton)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				doUnbindService();
			}});
    }
    
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  Because we have bound to a explicit
            // service that we know is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            mBoundService = ((AccelerometerService.AccelerometerBinder)service).getService();
        }

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mBoundService = null;
		}
    };
    
    private void doUnbindService()
    {
    	if(mIsBound)
    	{
    		mIsBound = false;
    		unbindService(mConnection);
    	}
    }
    private void doBindService(int actionType)
    {
    	Intent intent = new Intent(ShakeActivity.this,AccelerometerService.class);
    	intent.putExtra("ActionType",actionType);
    	bindService(intent,mConnection,Context.BIND_AUTO_CREATE);
    	mIsBound = true;
    }
}