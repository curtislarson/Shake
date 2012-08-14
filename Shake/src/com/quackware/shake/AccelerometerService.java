package com.quackware.shake;

import java.util.ArrayList;

import com.quackware.shake.wiigee.device.AndroidDevice;
import com.quackware.shake.wiigee.event.ActionEvent;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AccelerometerService extends Service implements SensorEventListener {

	private static final String TAG = "AccelerometerService";
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	
	private static final double NOISE = .2;
	
	private float x0, y0, z0, x1, y1, z1;

	
	private AndroidDevice mAndroidDevice;
	private int mActionType;
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_UI);
		
		this.x0 = 0;
		this.y0 = -SensorManager.STANDARD_GRAVITY;
		this.z0 = 0;
		this.x1 = SensorManager.STANDARD_GRAVITY;
		this.y1 = 0;
		this.z1 = SensorManager.STANDARD_GRAVITY;
		
		mAndroidDevice = ((Shake)getApplicationContext()).getAndroidDevice();	
	}
	
	public class AccelerometerBinder extends Binder
	{
		AccelerometerService getService()
		{
			return AccelerometerService.this;
		}
	}
	
	private final IBinder mBinder = new AccelerometerBinder();
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        return START_STICKY;
    }
	
	@Override
	public void onDestroy()
	{
		mAndroidDevice.fireActionStopEvent(mActionType);
		
		mSensorManager.unregisterListener(this);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
        
        mActionType = intent.getIntExtra("ActionType", ActionEvent.RECOGNITION_ACTION);    
        mAndroidDevice.fireActionStartEvent(mActionType);
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
		double x,y,z;
		float xraw,yraw,zraw;
		xraw = event.values[0];
		yraw = event.values[1];
		zraw = event.values[2];
		
		x = (double)(xraw-x0) / (double)(x1-x0);
		y = (double)(yraw-y0) / (double)(y1-y0);
		z = (double)(zraw-z0) / (double)(z1-z0);
		
		((Shake)getApplicationContext()).getAndroidDevice().addAccelerationEvent(new double[] {x,y,z});
	}

}
