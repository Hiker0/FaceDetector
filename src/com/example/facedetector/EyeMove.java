package com.example.facedetector;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint("NewApi")
public class EyeMove extends Activity {
	  static final int CAMERA_ID = 1;
	  static final String TAG = "FaceDetection" ;
	  private Camera camera;
	  
	  private Button button;
	  private TextView mTextView;
	  int state = 0;
	  Handler handler = new Handler();
	  
	  
	  Runnable stopRun = new Runnable(){

		@Override
		public void run() {
			// TODO Auto-generated method stub
			refreshState(0);
			reStartCamera();
		}
		  
	  };
	  
	  void reStartCamera(){
			if(camera != null){
				Log.d(TAG, "Detection restart");
				
				camera.stopFaceDetection();
				camera.stopPreview();
				camera.release();
				camera = null;
			}
			
		    camera = Camera.open(CAMERA_ID);
		    if (camera != null){
		    	Log.d(TAG, "Camera opened");
			    camera.setFaceDetectionListener(faceDetectionListener);
			    camera.startPreview();
			    camera.startFaceDetection();
		    }
	  }
	  
	  void refreshState(int st){
		  if(state != st){
			  state = st;
			  if(state == 1){
				  mTextView.setText("Play");
			  }else{
				  mTextView.setText("Stop");
			  }
		  }
	  }
	  Camera.FaceDetectionListener faceDetectionListener=new MyFaceDetectionListener();
	  
	
   @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	class MyFaceDetectionListener implements FaceDetectionListener{

		@Override
		public void onFaceDetection(Face[] faces, Camera camera) {
			// TODO Auto-generated method stub
			Log.d(TAG, "Camera onFaceDetection length="+faces.length);
			if(faces.length!=0){
				printinfo(faces[0]);
			}
			if(state != 1){
				refreshState(1);
			}
			handler.removeCallbacks(stopRun);
			handler.postDelayed(stopRun, 500);		
		}
  	 
   }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.eye_move);
		
		mTextView = (TextView)this.findViewById(R.id.info);
		refreshState(1);
		
		button = (Button)this.findViewById(R.id.redetect);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				reStartCamera();
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
	    if (this.camera != null)
	    {
	      Log.d(TAG, "Camera cancel");
	      this.camera.stopFaceDetection();
	      this.camera.stopPreview();
	      this.camera.release();
	      this.camera = null;
	    }
	    handler.removeCallbacks(stopRun);
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
	    this.camera = Camera.open(CAMERA_ID);
	    if (this.camera != null){
	    	Log.d(TAG, "Camera opened");
		    this.camera.setFaceDetectionListener(this.faceDetectionListener);
		    this.camera.startPreview();
		    this.camera.startFaceDetection();
	    }
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	void printinfo(Face face){
		if(face.leftEye != null && face.rightEye != null){
			Log.d(TAG,"FaceInfo:"+"lefteye("+face.leftEye.x +","+face.leftEye.y+"):"+"righteye("+face.rightEye.x +","+face.rightEye.y+")");
		}
		if(face.mouth != null){
			Log.d(TAG,"FaceInfo:"+"mouth("+face.mouth.x+","+face.mouth.y+"):");
		}
		if(face.rect != null){
			//Log.d(TAG,"FaceInfo:"+"rect("+face.rect.left+","+face.rect.right+","+face.rect.top+","+face.rect.bottom+")");
		}
	}

	@SuppressLint("NewApi")
	class FaceInfo {
		float eyedistance = 0;
		float eyetomouth = 0;
		float middle     = 0;
		FaceInfo(Face face){
			eyedistance=face.leftEye.x-face.rightEye.x;
		}
	}

}
