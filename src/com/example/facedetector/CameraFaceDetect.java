package com.example.facedetector;



/**

* <p>Copyright: Copyright (c) 2013-1</p>
* <p>Filename: TakeActivity.java</p>
* @author ѧ��:��Է��   ���   �����   ָ����ʦ:������
* @version 1.0
*/
/**
* ��������Ҫ�Ĺ��߰�
* 
*/
/**
* ����˵����������Ĺ��캯������������Ĵ��ڣ����ڲ������ã�������������ť���Զ��Խ�����ȡͼ��
* 
* 
*/  
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
public class CameraFaceDetect extends Activity {
        private static final String TAG = "TakeActivity";
    private SurfaceView surfaceView;
    private Camera camera = null;
    private boolean preview;
    @Override//��д
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
            requestWindowFeature(Window.FEATURE_NO_TITLE);//û�б���
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// ����ȫ��
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//����
        setContentView(R.layout.csdn_camera_facedetect);
        surfaceView =(SurfaceView)this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setFixedSize(960, 540);        //���÷ֱ���
        /*��������Surface��ά���Լ��Ļ����������ǵȴ���Ļ����Ⱦ���潫�������͵��û���ǰ*/
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        Button btn = (Button)findViewById(R.id.takepicture);
        btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                                camera.autoFocus(null);//�Զ��Խ� 
                                camera.takePicture(null, null, new TakePictureCallback());        }
                });
    }

    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" }) //��ע����ָ���ľ��� 

  private final class SurfaceCallback implements SurfaceHolder.Callback{

             /**
         * ����˵������������������׽��������׽ʧ�ܽ��ر������������Ϣ
         * 
         */
                public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                int height) {        
                }

                @SuppressLint({ "NewApi", "NewApi", "NewApi" })//��ע����ָ���ľ���
                
                public void surfaceCreated(SurfaceHolder holder) {
                        
                        try {
                                //�׳��쳣
                                           for (int i = 0; i < Camera.getNumberOfCameras(); i++) {                                           
                                            CameraInfo info = new CameraInfo();
                                            MyLog("total num is :"+Camera.getNumberOfCameras()+"");                                           
                                            Camera.getCameraInfo(i, info);
                                            if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {//
                                                    camera = Camera.open(i);
                                                     MyLog("find is :"+i+"");
                                            }
                                            
                                           }
                                           
                                        
//                                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//                                Display display = wm.getDefaultDisplay();
//                                Camera.Parameters parameters = camera.getParameters();
//                                parameters.setPreviewSize(display.getWidth(), display.getHeight());//����Ԥ����Ƭ�Ĵ�С
//                                parameters.setPreviewFrameRate(3);//ÿ��3֡
//                                parameters.setPictureFormat(PixelFormat.JPEG);//������Ƭ�������ʽ
//                                parameters.set("jpeg-quality", 85);//��Ƭ����
//                                parameters.setPictureSize(display.getWidth(), display.getHeight());//������Ƭ�Ĵ�С
//                                camera.setParameters(parameters);
                                 camera.setDisplayOrientation(90);
                                camera.setPreviewDisplay(surfaceView.getHolder());//ͨ��SurfaceView��ʾȡ������
                                camera.startPreview();//��ʼԤ��
                                preview = true;
                        } catch (IOException e) {
                                Log.e(TAG, e.toString());
                        }

                }
                public void surfaceDestroyed(SurfaceHolder holder) {
                        if(camera!=null){
                                if(preview) camera.stopPreview();
                                camera.release();
                        }
                }
            
    }

        @Override
        /**
     * ����˵�����ж�����Ƿ�Ϊ��
     * 
     */

        public boolean onKeyDown(int keyCode, KeyEvent event) {
                if(camera!=null && event.getRepeatCount()==0){
                        switch (keyCode) {
                        case KeyEvent.KEYCODE_SEARCH:
                                camera.autoFocus(null);//�Զ��Խ�
                                break;

                        case KeyEvent.KEYCODE_CAMERA:
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                                camera.takePicture(null, null, new TakePictureCallback());

                                break;
                        }
                }
                return true;
        }
        /**
     * ����˵����������Ƭ��ʽ�����ԣ���Ƭ���������ر�
     * 
     */
        private final class TakePictureCallback implements PictureCallback{

                public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                                //�����쳣
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
                                FileOutputStream outStream = new FileOutputStream(file);
                                bitmap.compress(CompressFormat.JPEG, 100, outStream);
                                outStream.close();
                                camera.stopPreview();
                                camera.startPreview();//��ʼԤ��
                        } catch (Exception e) {
                                Log.e(TAG, e.toString());
                        }
                }
        }
        /**
     * ����˵������Ƭ�洢��¼���洢·��
     * 
     */
        
        public void MyLog(String str){
//                
//                // д�ļ�
//        File path = new File("/mnt/sdcard/sunliang");
//        File file = new File("/mnt/sdcard/sunliang/log.txt");
//        if (!path.exists()) {
//            // ·��������? Just ����
//            path.mkdirs();
//        }
//        if (!file.exists()) {
//            // �ļ������ڡ� Just����
//            try {
//                    //�����쳣
//                file.createNewFile();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        OutputStreamWriter osw = null;
//        //
//        try {
//                //�����쳣
//            osw = new OutputStreamWriter(new FileOutputStream(file));
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//                e1.printStackTrace();
//        }
//        try {
//                //�����쳣
//            osw.write(str);
//            osw.close();
//
//        } catch (IOException e) {
//                //�׳�IOException e�쳣
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//                
//        
//        
        }
        

}
	
