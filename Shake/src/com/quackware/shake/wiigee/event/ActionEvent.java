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

package com.quackware.shake.wiigee.event;

import java.util.EventObject;

import com.quackware.shake.wiigee.device.Device;

public class ActionEvent extends EventObject {
	
	public static int START_TYPE = 0;
	public static int STOP_TYPE = 1;
	

    public static int RECOGNITION_ACTION = 2;
    public static int TRAIN_ACTION = 3;
    public static int ANALYZE_TRAIN_ACTION = 4;
	
	private int mActionType;
	private int mEventType;
	
	public ActionEvent(Device source, int actionType, int eventType)
	{
		super(source);
		mActionType = actionType;
		mEventType = eventType;
	}
	
	public boolean isRecognitionEvent()
	{
		return mActionType == RECOGNITION_ACTION;
	}
	
	public boolean isTrainEvent()
	{
		return mActionType == TRAIN_ACTION;
	}
	
	public boolean isAnalyzeTrainEvent()
	{
		return mActionType == ANALYZE_TRAIN_ACTION;
	}
	
	public int getActionType()
	{
		return mActionType;
	}
	
	public int getEventType()
	{
		return mEventType;
	}

}
