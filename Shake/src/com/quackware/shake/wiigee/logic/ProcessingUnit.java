package com.quackware.shake.wiigee.logic;

import java.util.Vector;

import com.quackware.shake.wiigee.event.AccelerationEvent;
import com.quackware.shake.wiigee.event.ActionEvent;
import com.quackware.shake.wiigee.event.ActionListener;
import com.quackware.shake.wiigee.event.AccelerationListener;
import com.quackware.shake.wiigee.event.GestureEvent;
import com.quackware.shake.wiigee.event.GestureListener;
import com.quackware.shake.wiigee.event.MotionStartEvent;
import com.quackware.shake.wiigee.event.MotionStopEvent;
import com.quackware.shake.wiigee.util.Log;

public abstract class ProcessingUnit implements AccelerationListener, ActionListener {

    // Classifier
    protected Classifier classifier;
    
    // Listener
    private Vector<GestureListener> gesturelistener = new Vector<GestureListener>();

    public ProcessingUnit() {
        this.classifier = new Classifier();
    }

    /**
     * Add an GestureListener to receive GestureEvents.
     *
     * @param g
     * 	Class which implements GestureListener interface.
     */
    public void addGestureListener(GestureListener g) {
        this.gesturelistener.add(g);
    }

    protected void fireGestureEvent(boolean valid, int id, double probability) {
        GestureEvent w = new GestureEvent(this, valid, id, probability);
        for (int i = 0; i < this.gesturelistener.size(); i++) {
            this.gesturelistener.get(i).gestureReceived(w);
        }
    }

    public abstract void accelerationReceived(AccelerationEvent event);
    
    public abstract void motionStartReceived(MotionStartEvent event);

    public abstract void motionStopReceived(MotionStopEvent event);
    
    public abstract void actionStartReceived(ActionEvent event);
	
	public abstract void actionStopReceived(ActionEvent event);

    /**
     * Resets the complete gesturemodel. After reset no gesture is known
     * to the system.
     */
    public void reset() {
        if (this.classifier.getCountOfGestures() > 0) {
            this.classifier.clear();
            Log.write("### Model reset ###");
        } else {
            Log.write("There doesn't exist any data to reset.");
        }
    }

    // File IO
    public abstract void loadGesture(String filename);

    public abstract void saveGesture(int id, String filename);
}
