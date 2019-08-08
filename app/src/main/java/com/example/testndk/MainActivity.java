package com.example.testndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testndk.bean.FaceRectIn;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Test";

    private static String uri = Environment.getExternalStorageDirectory()
            + File.separator;

    private static final String img_uri = "file:///android_asset/";

    private static byte[] defaultImgData;
    private static float[] depthImg = new float[256000];

    private static byte[] baseImgResizeData;

    private static Mat baseBitmapResizeMat;
    private static Mat nowMat;
    private static boolean isHalfSize = false;

//    Bitmap testBitmap = Bitmap.createBitmap(630, 840, Bitmap.Config.RGB_565);

    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;

    private Button button;

    private Bitmap bitmap;

    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("base-lib");
    }

    private static final int UPDATE_VIEW = 0x001;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VIEW:
                    Mat bitmapMat = (Mat)msg.obj;
                    if (bitmapMat == null) return;
                    Bitmap bitmap = Bitmap.createBitmap(bitmapMat.cols(), bitmapMat.rows(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(bitmapMat, bitmap);
                    Log.d(TAG, "handleMessage: 更新");
                    imageView2.setImageBitmap(bitmap);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.id_img_iv);
        imageView2 = (ImageView) findViewById(R.id.id_img2_iv);
        imageView3 = (ImageView) findViewById(R.id.id_img3_iv);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(JniTest.goobeeStringFromJNI() + "Result: ");

//        copyAssetAndWrite("test.bmp");
//        copyAssetAndWrite("500mm.bmp");
//
//        File dataFile = new File(getCacheDir(), "500mm.bmp");
//        File nowFile = new File(getCacheDir(), "test.bmp");

        baseBitmapResizeMat = new Mat();
        nowMat = new Mat();

        try {
            InputStream is = getResources().getAssets().open("500mm.bmp");
            Bitmap baseBitmap = BitmapFactory.decodeStream(is);
            Mat mat = new Mat(baseBitmap.getHeight(),baseBitmap.getWidth(), CvType.CV_8UC4);
            Utils.bitmapToMat(baseBitmap,mat);
            Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);

            InputStream is2 = getResources().getAssets().open("test.bmp");
            Bitmap baseBitmap2 = BitmapFactory.decodeStream(is2);
            Mat mat2 = new Mat(baseBitmap2.getHeight(),baseBitmap.getWidth(), CvType.CV_8UC4);
            Utils.bitmapToMat(baseBitmap2,mat2);
            Imgproc.cvtColor(mat2,mat2,Imgproc.COLOR_RGB2GRAY);

            /**
             * 已经resize
             */
            float scale = 0.5f;
            float width = mat.width();
            float height = mat.height();
            Size size = new Size(width*scale,height*scale);
            Imgproc.resize(mat,baseBitmapResizeMat,size);
            Imgproc.resize(mat2,nowMat,size);


            int baseLength = baseBitmapResizeMat.width()*baseBitmapResizeMat.height();
            baseImgResizeData = new byte[baseLength];
            baseBitmapResizeMat.get(0,0,baseImgResizeData);

            defaultImgData = new byte[nowMat.width()* nowMat.height()];
            nowMat.get(0,0,defaultImgData);

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onCreate: baseBitmapResizeMat size" + baseBitmapResizeMat.size() + "--" + nowMat.size());

        Bitmap bitmap3 = Bitmap.createBitmap(baseBitmapResizeMat.cols(), baseBitmapResizeMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(baseBitmapResizeMat, bitmap3);
        imageView3.setImageBitmap(bitmap3);

        Bitmap bitmap4 = Bitmap.createBitmap(nowMat.cols(), nowMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(nowMat, bitmap4);
        imageView.setImageBitmap(bitmap4);

        button = (Button) findViewById(R.id.id_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Mat mat4 = getImage();
                        Message message = Message.obtain();
                        message.what = UPDATE_VIEW;
                        message.obj = mat4;
                        handler.sendMessage(message);
                    }
                }).start();
            }
        });

    }



    static Mat mat = null;
    static Mat mat1 = null;
    static Mat mat2 = null;
    static Mat mat3 = null;
    static Mat grayMat = new Mat();
    static Mat src = new Mat(800, 1280, CvType.CV_8UC1);

    /**
     * @return
     */
    public static Mat getImage() {
        //2、获取深度图：
        Log.d(TAG, "getImage: " + baseImgResizeData.length);
        Log.d(TAG, "getImage: " + defaultImgData.length);
        int result = JniTest.stereoInterface(baseImgResizeData, defaultImgData, depthImg,new FaceRectIn());
        Log.d(TAG, "getImage: " + result);


        if (mat == null) {
            mat = new Mat(grayMat.height(), grayMat.width(), CvType.CV_32FC3);
        }


        int cols = grayMat.width();
        int rows = grayMat.height();

        if (mat3 == null) {
            mat3 = new Mat(rows, cols, CvType.CV_32FC1);
        }


        int length = depthImg.length;

        for (int i = 0; i < length; i++) {
            if (depthImg[i] >= 5000.0 || depthImg[i] == 0.0) {
                depthImg[i] = 0;
            } else {
                if (false) {
                    depthImg[i] = 255 - fun(depthImg[i]);  //255
                } else {
                    depthImg[i] = 255 - fun(depthImg[i]);  //255
                }
            }
        }

        mat3.put(0, 0, depthImg);  //已获取

//        /**
//         * 显示深度图或者彩色图
//         */
//        if (!MyApplication.isShowDepth()) {
//            Mat mat = new Mat(grayMat.height(), grayMat.width(), CvType.CV_8UC1);
//            mat3.convertTo(mat, CvType.CV_8UC1);
//            Mat imColor = new Mat();
//            Imgproc.applyColorMap(mat, imColor, Imgproc.COLORMAP_JET);
//            mat.release();
//            return imColor;
//        }

        if (mat1 == null || mat2 == null) {
            mat1 = new Mat(rows, cols, CvType.CV_32FC1);
            mat1.setTo(new Scalar(60));
            mat2 = new Mat(rows, cols, CvType.CV_32FC1);
            mat2.setTo(new Scalar(60));
        }

        List<Mat> matList = new ArrayList<>();
        matList.add(mat1);
        matList.add(mat2);
        matList.add(mat3);
        Core.merge(matList, mat);

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_HSV2RGB);
        mat.convertTo(mat, CvType.CV_8UC4);
        
        return mat;
    }




    private static int fun(float x) {
        double y = 0;
        if (200 <= x && x < 400)
            y = 0.375 * x - 75;
        else if (400 <= x && x < 600)
            y = 0.225 * x - 15;
        else if (600 <= x && x < 800)
            y = 0.15 * x + 30;
        else if (800 <= x && x < 2750)
            y = 3.59e-02 * x + 1.21e+02;
        else if (5000 >= x && x >= 2750)
            y = 1.56e-02 * x + 1.77e+02;
        else if (x > 5000)
            y = 255.0;
        return (int) y;
    }

    /**
     * 将asset文件写入缓存
     */
    private boolean copyAssetAndWrite(String fileName) {
        try {
            File cacheDir = getCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
