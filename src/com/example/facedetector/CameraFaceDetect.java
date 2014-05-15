package com.example.facedetector;



/**

* <p>Copyright: Copyright (c) 2013-1</p>
* <p>Filename: TakeActivity.java</p>
* @author 学生:彭苑君   杨海市   张叙杰   指导老师:黄锡波
* @version 1.0
*/
/**
* 导入所需要的工具包
* 
*/
/**
* 方法说明：启动类的构造函数，创建相机的窗口，窗口参数设置，创建相机点击按钮，自动对焦，获取图像
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
    @Override//重写
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
            requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//高亮
        setContentView(R.layout.csdn_camera_facedetect);
        surfaceView =(SurfaceView)this.findViewById(R.id.surfaceView);
        surfaceView.getHolder().setFixedSize(960, 540);        //设置分辨率
        /*下面设置Surface不维护自己的缓冲区，而是等待屏幕的渲染引擎将内容推送到用户面前*/
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().addCallback(new SurfaceCallback());
        Button btn = (Button)findViewById(R.id.takepicture);
        btn.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                                camera.autoFocus(null);//自动对焦 
                                camera.takePicture(null, null, new TakePictureCallback());        }
                });
    }

    @SuppressLint({ "NewApi", "NewApi", "NewApi", "NewApi" }) //标注忽略指定的警告 

  private final class SurfaceCallback implements SurfaceHolder.Callback{

             /**
         * 方法说明：更换人脸，将捕捉人脸，捕捉失败将关闭相机、反馈信息
         * 
         */
                public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                int height) {        
                }

                @SuppressLint({ "NewApi", "NewApi", "NewApi" })//标注忽略指定的警告
                
                public void surfaceCreated(SurfaceHolder holder) {
                        
                        try {
                                //抛出异常
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
//                                parameters.setPreviewSize(display.getWidth(), display.getHeight());//设置预览照片的大小
//                                parameters.setPreviewFrameRate(3);//每秒3帧
//                                parameters.setPictureFormat(PixelFormat.JPEG);//设置照片的输出格式
//                                parameters.set("jpeg-quality", 85);//照片质量
//                                parameters.setPictureSize(display.getWidth(), display.getHeight());//设置照片的大小
//                                camera.setParameters(parameters);
                                 camera.setDisplayOrientation(90);
                                camera.setPreviewDisplay(surfaceView.getHolder());//通过SurfaceView显示取景画面
                                camera.startPreview();//开始预览
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
     * 方法说明：判断相机是否为空
     * 
     */

        public boolean onKeyDown(int keyCode, KeyEvent event) {
                if(camera!=null && event.getRepeatCount()==0){
                        switch (keyCode) {
                        case KeyEvent.KEYCODE_SEARCH:
                                camera.autoFocus(null);//自动对焦
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
     * 方法说明：声明照片格式、属性，相片册游览、关闭
     * 
     */
        private final class TakePictureCallback implements PictureCallback{

                public void onPictureTaken(byte[] data, Camera camera) {
                        try {
                                //捕获异常
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
                                FileOutputStream outStream = new FileOutputStream(file);
                                bitmap.compress(CompressFormat.JPEG, 100, outStream);
                                outStream.close();
                                camera.stopPreview();
                                camera.startPreview();//开始预览
                        } catch (Exception e) {
                                Log.e(TAG, e.toString());
                        }
                }
        }
        /**
     * 方法说明：相片存储记录、存储路径
     * 
     */
        
        public void MyLog(String str){
//                
//                // 写文件
//        File path = new File("/mnt/sdcard/sunliang");
//        File file = new File("/mnt/sdcard/sunliang/log.txt");
//        if (!path.exists()) {
//            // 路径不存在? Just 创建
//            path.mkdirs();
//        }
//        if (!file.exists()) {
//            // 文件不存在、 Just创建
//            try {
//                    //捕获异常
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
//                //捕获异常
//            osw = new OutputStreamWriter(new FileOutputStream(file));
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//                e1.printStackTrace();
//        }
//        try {
//                //捕获异常
//            osw.write(str);
//            osw.close();
//
//        } catch (IOException e) {
//                //抛出IOException e异常
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//                
//        
//        
        }
        

}
	
