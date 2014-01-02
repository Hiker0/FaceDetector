package com.example.facedetector;


import java.io.IOException;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.TextView;
public class CsdnCameraFaceDetect extends Activity implements SurfaceHolder.Callback{

	 Camera camera;
	 SurfaceView surfaceView;
	 SurfaceHolder surfaceHolder;
	 boolean previewing = false;
	 LayoutInflater controlInflater = null;
	 
	 Button buttonTakePicture;
	 TextView prompt;
	 
	 final int RESULT_SAVEIMAGE = 0;
	 
	   /** Called when the activity is first created. */
	   @Override
	   public void onCreate(Bundle savedInstanceState) {
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.csdn_camera_facedetect);
	       setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	      
	       getWindow().setFormat(PixelFormat.UNKNOWN);
	       surfaceView = (SurfaceView)findViewById(R.id.camerapreview);
	       surfaceHolder = surfaceView.getHolder();
	       surfaceHolder.addCallback(this);
	       surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	      
//	       controlInflater = LayoutInflater.from(getBaseContext());
//	       View viewControl = controlInflater.inflate(R.layout.control, null);
//	       LayoutParams layoutParamsControl
//	        = new LayoutParams(LayoutParams.FILL_PARENT,
//	        LayoutParams.FILL_PARENT);
//	       this.addContentView(viewControl, layoutParamsControl);
	      
	   }
	  
	   FaceDetectionListener faceDetectionListener
	   = new FaceDetectionListener(){

	  @Override
	  public void onFaceDetection(Face[] faces, Camera camera) {
	   
	   if (faces.length == 0){
	    prompt.setText(" No Face Detected! ");
	   }else{
	    prompt.setText(String.valueOf(faces.length) + " Face Detected :) ");
	    Log.i("faceScore", faces[0].score+"");
	    Point leftEye = faces[0].leftEye;
	    
//	    leftEye
	    
	   }
	   
	   
	  }};
	  
	  
	 @Override
	 public void surfaceChanged(SurfaceHolder holder, int format, int width,
	   int height) {
//	  // TODO Auto-generated method stub
	  if(previewing){
	   camera.stopFaceDetection();
	   camera.stopPreview();
	   previewing = false;
	  }
	  
	  if (camera != null){
	   try {
	    camera.setPreviewDisplay(surfaceHolder);
	    camera.startPreview();

	    prompt.setText(String.valueOf(
	      "Max Face: " + camera.getParameters().getMaxNumDetectedFaces()));
	    camera.startFaceDetection();
	    previewing = true;
	   } catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	   }
	  }
	 }

	 @Override
	 public void surfaceCreated(SurfaceHolder holder) {
	  // TODO Auto-generated method stub
	  camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
	  camera.setFaceDetectionListener(faceDetectionListener);
	 }

	 @Override
	 public void surfaceDestroyed(SurfaceHolder holder) {
	  // TODO Auto-generated method stub
	  camera.stopFaceDetection();
	  camera.stopPreview();
	  camera.release();
	  camera = null;
	  previewing = false;
	 }
	 
	 Camera.FaceDetectionListener faceDetionListener = new Camera.FaceDetectionListener() {
		
		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	}
