package com.example.facedetector;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class FaceDetect extends Activity {
	static final int CAMERA_ID = 1;
	static final String TAG = "FaceDetection";
	private Camera camera;
	private CameraPreviewView previewView = null;

	int state = 0;
	Handler handler = new Handler();

	private class FrameCatcher implements Camera.PreviewCallback {
		public int mFrames = 0;
		private final int mExpectedSize;

		public FrameCatcher(int width, int height) {
			mExpectedSize = width * height * 3 / 2;
		}

		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			if (mExpectedSize != data.length) {
				throw new UnsupportedOperationException("bad size, got "
						+ data.length + " expected " + mExpectedSize);
			}
			mFrames++;
			camera.addCallbackBuffer(data);
		}

	}

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

	@SuppressWarnings("deprecation")
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

		FrameCatcher catcher = new FrameCatcher(640, 480);
		camera.setPreviewCallbackWithBuffer(null);
		camera.setPreviewCallbackWithBuffer(catcher);

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

	// ����ͼƬ��ʽ��һ������ͷץ��������ΪImageFormat.NV21����ͬ�ĸ�ʽ����Ҫ�����ġ�

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
	// //���ҵ��ֻ���������Ԥ��ģʽ������ȫ��ģʽʱ��Ҫ����ͼƬ�Ĵ�С������ȷ��λ��
	//
	// Bitmap myBitmap=BitmapFactory.decodeByteArray(stream.toByteArray(), 0,
	// stream.size());
	// stream.close();
	// if (mPreviewLayoutProxy.getFullScreenMode()) { // fullscreen mode
	// Bitmap tmpScaleBmp=null;
	// Bitmap tmpRotateBmp=null;
	//
	// //�ֻ�������������������Ŷ
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

		dumpCameraCaps(CAMERA_ID);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause");
		if (this.camera != null) {
			stopPreView();
			this.camera.release();
			this.camera = null;
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume");
		int n = Camera.getNumberOfCameras();
		Log.d(TAG, "getNumberOfCameras=" + n);
		if (n > 1) {
			this.camera = Camera.open(CAMERA_ID);
		}

		if (this.camera != null) {

			startPreview();

		}

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

}