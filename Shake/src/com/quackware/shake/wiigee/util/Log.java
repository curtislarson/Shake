package com.quackware.shake.wiigee.util;

public class Log {

	public static final int OFF = -1;
	public static final int NORMAL = 0;
	public static final int DEBUG = 1;
	
	public static final int PRINT = 0;
	public static final int FILE = 1;
	
	public static int level = Log.NORMAL;
	public static int mode = Log.PRINT;
	
	private static final String TAG = "Log";
	
	public static void setLevel(int n) {
		level = n;
	}
	
	public static void write(String s) {
		write(Log.NORMAL, s, null);
	}
	
	public static void write(String s, Object o) {
		write(Log.NORMAL, s, o);
	}
	
	public static void write(int n, String s, Object o) {
		if(level>=n) {
			if(mode==Log.PRINT) {
				// console output enabled
				if(o!=null) {
					android.util.Log.i(TAG,o.getClass()+": "+s);
				} else {
					android.util.Log.i(TAG,s);
				}
			} else if(mode==Log.FILE) {
				// file output enabled
				// TODO
			}
		}
	}
	
}
