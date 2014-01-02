package com.example.facedetector;


import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PreviewCallback;
import android.media.FaceDetector;
import android.media.FaceDetector.Face;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
//����ʶ��Ĺؼ���


public class CsdnFaceDetect extends Activity {
	final static String TAG=  "CsdnFaceDetect";
	private Camera mCameraDevice = null;
	Handler mHandler = new Handler();
	long mScanEndTime,mSpecPreviewTime,mScanBeginTime,mSpecStopTime,mSpecCameraTime;
	int orientionOfCamera;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_main_activity2);
		setContentView(new myView(this));	//ʹ���Խ���view����ʾ
		Log.i("TAG","CsdnFaceDetect run here");
	}

	private class myView extends View{
		private int imageWidth, imageHeight;
		private int numberOfFace = 5;		//������������
		private FaceDetector myFaceDetect;	//����ʶ�����ʵ��
		private FaceDetector.Face[] myFace;	//�洢�����������������
		float myEyesDistance; 			//����֮��ľ���
		int numberOfFaceDetected; 		//ʵ�ʼ�⵽��������
		Bitmap myBitmap;

		public myView(Context context){		//view��Ĺ��캯����������
			super(context); 
			BitmapFactory.Options BitmapFactoryOptionsbfo = new BitmapFactory.Options(); 
			BitmapFactoryOptionsbfo.inPreferredConfig = Bitmap.Config.RGB_565;	//����λͼ���ɵĲ���������Ϊ565
			myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.girl, BitmapFactoryOptionsbfo);	 
			imageWidth = myBitmap.getWidth(); 
			imageHeight = myBitmap.getHeight(); 
			myFace = new FaceDetector.Face[numberOfFace]; 		//������������ռ�
			myFaceDetect = new FaceDetector(imageWidth, imageHeight, numberOfFace); 
			numberOfFaceDetected = myFaceDetect.findFaces(myBitmap, myFace); 	//FaceDetector ����ʵ������������
			Log.i("zhangcheng","numberOfFaceDetected is " + numberOfFaceDetected);
		}
		
		protected void onDraw(Canvas canvas){			//override����������
			canvas.drawBitmap(myBitmap, 0, 0, null);	//����λͼ 
			Paint myPaint = new Paint(); 
			myPaint.setColor(Color.GREEN); 
			myPaint.setStyle(Paint.Style.STROKE); 
			myPaint.setStrokeWidth(3); 			//����λͼ��paint�����Ĳ���

			for(int i=0; i < numberOfFaceDetected; i++){
				Face face = myFace[i];
				PointF myMidPoint = new PointF(); 
				face.getMidPoint(myMidPoint); 
				myEyesDistance = face.eyesDistance(); 	//�õ��������ĵ���ۼ�������������ÿ���������л���
				canvas.drawRect( 			//���ο��λ�ò���
                        (int)(myMidPoint.x - myEyesDistance), 
                        (int)(myMidPoint.y - myEyesDistance), 
                        (int)(myMidPoint.x + myEyesDistance), 
                        (int)(myMidPoint.y + myEyesDistance), 
                        myPaint);
			}
		}
	}
}
