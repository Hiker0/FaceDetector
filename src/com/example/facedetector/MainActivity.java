package com.example.facedetector;

import java.io.IOException;
import java.util.List;

import com.example.facedetector.FaceDetect.MyFaceDetectionListener;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

	
	static final int CAMERA_ID = 1;
	static final String TAG = "FaceDetection";
	private Camera camera;
	private CameraPreviewView previewView = null;

	int state = 0;


	private void dumpCameraCaps(int id) {
		int n = Camera.getNumberOfCameras();
		if (id > n - 1)
			return;

		Camera cam = Camera.open(id);
		Camera.Parameters params = cam.getParameters();
		List<Integer> formats = params.getSupportedPreviewFormats();
		List<int[]> frameRates = params.getSupportedPreviewFpsRange();
		List<Camera.Size> sizes = params.getSupportedPreviewSizes();
		Log.d(TAG, "Camera " + id);
		Log.d(TAG, "Sizes");
		for (Size size : sizes) {
			Log.d(TAG, size.width + "x" + size.height);
		}
		Log.d(TAG, "frameRates");
		for (int[] rates : frameRates) {
			Log.d(TAG, rates[0] + "-" + rates[1]);
		}
		Log.d(TAG, "formats");
		for (Integer format : formats) {
			Log.d(TAG, format.toString());
		}
		cam.release();
	}

	
	void startPreview() {
		Log.d(TAG, "startPreview");
		Camera.Parameters parameters = camera.getParameters();

		parameters.setPreviewSize(640, 480);
		parameters.setPreviewFormat(ImageFormat.NV21);

		parameters.setPreviewFrameRate(15);
		camera.setParameters(parameters);
		

		parameters = camera.getParameters();
		int imageFormat = parameters.getPreviewFormat();

		camera.setDisplayOrientation(90);


		int bufferSize;
		bufferSize = 640 * 480 * ImageFormat.getBitsPerPixel(imageFormat) / 8;

		byte[] cameraBuffer = new byte[bufferSize];
		camera.addCallbackBuffer(cameraBuffer);
		try {
			camera.setPreviewDisplay(previewView.mHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		camera.startPreview();

	}

	void stopPreView() {
		Log.d(TAG, "stopPreView");
		camera.setPreviewCallbackWithBuffer(null);
		camera.stopPreview();
	}

	// 处理图片格式，一般摄像头抓到的数据为ImageFormat.NV21，不同的格式，需要调整的。

	// private void decodeToBitMap(byte[] data, android.hardware.Camera _camera)
	// {
	// camera.setPreviewCallback(null);
	// Size size = camera.getParameters().getPreviewSize();
	// //FileOutputStream outStream = null;
	// try {
	// YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width,
	// size.height, null);
	//
	// if (image != null) {
	// ByteArrayOutputStream stream = new ByteArrayOutputStream();
	// image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80,
	// stream);
	//
	// //在我的手机上有两种预览模式，发现全屏模式时需要调整图片的大小才能正确定位。
	//
	// Bitmap myBitmap=BitmapFactory.decodeByteArray(stream.toByteArray(), 0,
	// stream.size());
	// stream.close();
	// if (mPreviewLayoutProxy.getFullScreenMode()) { // fullscreen mode
	// Bitmap tmpScaleBmp=null;
	// Bitmap tmpRotateBmp=null;
	//
	// //手机是竖屏横排是与其别的哦
	// if (((mOrientation/90) == 0) || ((mOrientation/90) == 2)) {
	// //tmpRotateBmp = rotateMyBitmap(myBitmap);
	// //tmpScaleBmp = scaleMyBitmap(tmpRotateBmp);
	// //FindFacesInBitmap(tmpScaleBmp);
	// if (tmpRotateBmp != null) {
	// tmpRotateBmp.recycle();
	// tmpRotateBmp = null;
	// }
	// } else {
	// //FindFacesInBitmap(scaleMyBitmap(myBitmap));
	// }
	// if (tmpScaleBmp != null) {
	// tmpScaleBmp.recycle();
	// tmpScaleBmp = null;
	// }
	// } else { //normal mode
	// //FindFacesInBitmap(myBitmap);
	// }
	// //DrawRectOnFace();
	// if (myBitmap != null) {
	// myBitmap.recycle();
	// myBitmap = null;
	// }
	// }
	// } catch (Exception ex) {
	// Log.e("Sys", "Error:" + ex.getMessage());
	// }
	// camera.setPreviewCallback(mPreviewCallback);
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		Log.d(TAG, "onCreate");
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		this.setContentView(R.layout.preview);

		previewView = (CameraPreviewView) this
				.findViewById(R.id.camera_preview);
		previewView.setHolderCallback(new SurfaceCallback());
		dumpCameraCaps(CAMERA_ID);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		if (this.camera != null) {
			stopPreView();
			this.camera.release();
			this.camera = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");

		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");

		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStart");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop");
		super.onStop();
		
	}
	
	
	public class SurfaceCallback implements SurfaceHolder.Callback {

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
			int n = Camera.getNumberOfCameras();
			Log.d(TAG, "getNumberOfCameras=" + n);
			if (n > 1) {
				camera = Camera.open(CAMERA_ID);
			}

			if (camera != null) {

				startPreview();

			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {
			// TODO Auto-generated method stub
			
		}

	}
}
