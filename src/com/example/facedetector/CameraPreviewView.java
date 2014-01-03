package com.example.facedetector;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreviewView extends SurfaceView  {
	public SurfaceHolder mHolder;
	public CameraPreviewView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public CameraPreviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
        mHolder = getHolder();
    

	}

	public CameraPreviewView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void setHolderCallback(SurfaceHolder.Callback  callback){
		mHolder.addCallback(callback);
	}
	


}
