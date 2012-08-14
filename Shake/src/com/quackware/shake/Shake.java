package com.quackware.shake;

import com.quackware.shake.wiigee.device.AndroidDevice;

import android.app.Application;

public class Shake extends Application {
	
	private static Shake mSingleton;
	private AndroidDevice mAndroidDevice;
	
	@Override
	public void onCreate()
	{
		
		super.onCreate();
		mSingleton = this;
		mAndroidDevice = new AndroidDevice(true);

	}
	
	public AndroidDevice getAndroidDevice()
	{
		return mAndroidDevice;
	}
	
	public Shake getInstance()
	{
		return mSingleton;
	}

}
