package com.example.facedetector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraPreviewView extends SurfaceView implements SurfaceHolder.Callback {
	public SurfaceHolder mHolder;
	public CameraPreviewView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public CameraPreviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
        mHolder = getHolder();
        mHolder.addCallback(this);

	}

	public CameraPreviewView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

}
