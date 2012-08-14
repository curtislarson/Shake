package com.quackware.shake;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;

public class ApplicationInfoUtility {

	private static ActivityManager mActivityManager;
	private static Context mContext;
	
	
	private static void setContext(Context context)
	{
		mContext = context;
	}
	
	private static RunningAppProcessInfo getForegroundApp() {
	    RunningAppProcessInfo result=null, info=null;

	    if(mActivityManager==null)
	        mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List <RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
	    Iterator <RunningAppProcessInfo> i = l.iterator();
	    while(i.hasNext()){
	        info = i.next();
	        if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
	                && !isRunningService(info.processName)){
	            result=info;
	            break;
	        }
	    }
	    
	    return result;
	}

	private static ComponentName getActivityForApp(RunningAppProcessInfo target){
	    ComponentName result=null;
	    ActivityManager.RunningTaskInfo info;

	    if(target==null)
	        return null;

	    if(mActivityManager==null)
	        mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List <ActivityManager.RunningTaskInfo> l = mActivityManager.getRunningTasks(9999);
	    Iterator <ActivityManager.RunningTaskInfo> i = l.iterator();

	    while(i.hasNext()){
	        info=i.next();
	        if(info.baseActivity.getPackageName().equals(target.processName)){
	            result=info.topActivity;
	            break;
	        }
	    }
	    
	    return result;
	}

	private static boolean isStillActive(RunningAppProcessInfo process, ComponentName activity)
	{
	    // activity can be null in cases, where one app starts another. for example, astro
	    // starting rock player when a move file was clicked. we dont have an activity then,
	    // but the package exits as soon as back is hit. so we can ignore the activity
	    // in this case
	    if(process==null)
	        return false;

	    RunningAppProcessInfo currentFg=getForegroundApp();
	    ComponentName currentActivity=getActivityForApp(currentFg);

	    if(currentFg!=null && currentFg.processName.equals(process.processName) &&
	            (activity==null || currentActivity.compareTo(activity)==0))
	        return true;

	    //Slog.i(TAG, "isStillActive returns false - CallerProcess: " + process.processName + " CurrentProcess: "
	    //        + (currentFg==null ? "null" : currentFg.processName) + " CallerActivity:" + (activity==null ? "null" : activity.toString())
	    //        + " CurrentActivity: " + (currentActivity==null ? "null" : currentActivity.toString()));
	    return false;
	}

	private static boolean isRunningService(String processname){
	    if(processname==null || processname.isEmpty())
	        return false;

	    RunningServiceInfo service;

	    if(mActivityManager==null)
	        mActivityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
	    List <RunningServiceInfo> l = mActivityManager.getRunningServices(9999);
	    Iterator <RunningServiceInfo> i = l.iterator();
	    while(i.hasNext()){
	        service = i.next();
	        if(service.process.equals(processname))
	            return true;
	    }

	    return false;
	}
}
