package com.example.testndk;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    private static byte[] depthImg = new byte[10240000];

    private static byte[] baseImgResizeData;

    private static Mat baseBitmapResizeMat;
    private static Mat nowMat;

//    Bitmap testBitmap = Bitmap.createBitmap(630, 840, Bitmap.Config.RGB_565);

    private ImageView imageView;
    private ImageView imageView2;
    private ImageView imageView3;

    private Bitmap bitmap;

    static {
        System.loadLibrary("opencv_java3");
//        System.loadLibrary("base-lib");
    }

    private static final int UPDATE_VIEW = 0x001;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_VIEW:
                    Bitmap bitmap = (Bitmap)msg.obj;
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

        imageView = findViewById(R.id.id_img_iv);
        imageView2 = findViewById(R.id.id_img2_iv);
        imageView3 = findViewById(R.id.id_img3_iv);

        // Example of a call to a native method
        TextView tv = findViewById(R.id.sample_text);
        tv.setText(JniTest.goobeeStringFromJNI() + "Result: ");

        copyAssetAndWrite("test.bmp");
        copyAssetAndWrite("500mm.bmp");

        File dataFile = new File(getCacheDir(), "500mm.bmp");
        File nowFile = new File(getCacheDir(), "test.bmp");

//        testOpenCV();

//            InputStream is =getResources().getAssets().open("hello.jpg");
        InputStream is = null;
        try {
//            is = getResources().getAssets().open("test.bmp");
            is = getResources().getAssets().open("hello.bmp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeStream(is);

//            InputStream is2 = getResources().getAssets().open("500mm.bmp");
//            Bitmap bitmap2 = BitmapFactory.decodeStream(is2);

        baseBitmapResizeMat = new Mat();
        nowMat = Imgcodecs.imread(nowFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
        Mat mat = Imgcodecs.imread(dataFile.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);

//            Mat mat = new Mat(bitmap2.getHeight(),bitmap2.getWidth(), CvType.CV_8UC1);
//            Utils.bitmapToMat(bitmap2,mat);
        Log.d(TAG, "onCreate: " + "mat size; " + mat.size());
        float scale = 0.5f;
        float width = mat.width();
        float height = mat.height();
        Size size = new Size(width * scale, height * scale);
        Imgproc.resize(mat, baseBitmapResizeMat, size);

        double[] test = baseBitmapResizeMat.get(0,0);
        Log.d(TAG, "onCreate: baseBitmapResizeMat size" + test.length);
        for (int i=0;i<test.length;i++){
            Log.d(TAG, "onCreate: double: " + test[i]);
        }

        Bitmap bitmap3 = Bitmap.createBitmap(baseBitmapResizeMat.cols(), baseBitmapResizeMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(baseBitmapResizeMat, bitmap3);
        imageView3.setImageBitmap(bitmap3);

        baseImgResizeData = new byte[baseBitmapResizeMat.width() * baseBitmapResizeMat.height()];
        baseBitmapResizeMat.get(0, 0, baseImgResizeData);  //赋错值了
        Log.d(TAG, "onCreate: " + baseImgResizeData[0]);


//        writeFileToSDCard(baseImgResizeData,null,"test_byte_data.txt",false,true);

//            imageView2.setImageBitmap(bitmap);
//            Log.d(TAG, "onCreate: " + baseImgData.length + " --- " + defaultImgData.length);


        findViewById(R.id.id_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onCreate:系统时间：enter " + System.currentTimeMillis());
//                imageView.setImageBitmap(getImage(nowMat));
                imageView.setImageBitmap(getImage(bitmap));
                Log.d(TAG, "onCreate:系统时间：finish " + System.currentTimeMillis());

//                Mat mat = new Mat(2,2,CvType.CV_8UC3);
//                mat.setTo(new Scalar(12,25,110));
//                byte[] data = new byte[mat.rows()*mat.cols()*3];
//                mat.get(0,0,data);
//                for (int i=0;i<data.length;i++){
//                    Log.d(TAG, "onClick: " + data[i]);
//                }
//                double[] d = mat.get(0,0);
//                Log.d(TAG, "onClick: double:" + d.length);
//                for (int j=0;j<d.length;j++){
//                    Log.d(TAG, "onClick: d: " + d[j]);
//                }
//                mat.put(1,1,18,20,22);
//                double[] point = mat.get(1,1);
//                Log.d(TAG, "onClick: -point- : " + point[1]);
//                byte[] newData = new byte[mat.rows()*mat.cols()*3];
//                mat.get(0,0,newData);
//                for (int i=0;i<newData.length;i++){
//                    Log.d(TAG, "onClick -- : " + newData[i]);
//                }


            }
        });

//        Log.d(TAG, "onCreate:系统时间：enter " + System.currentTimeMillis());
//        int result = stereoInterface(baseImgData,defaultImgData,depthImg,0,0,800,1280);
//        Log.d(TAG, "onCreate:系统时间：finish " + System.currentTimeMillis());
//        Log.d(TAG, "onCreate: result: " + result + "---: " + defaultImgData[100] + " -- " + defaultImgData[105000]);

//        Bitmap bitmap = rgb2Bitmap(defaultImgData,800,1280);
//        imageView.setImageBitmap(bitmap);
//        saveImageToGallery(rgb2Bitmap(depthImg,800,1280));
    }

    private void testOpenCV() {
        Mat img1 = Imgcodecs.imread(uri + "test1.png", Imgcodecs.IMREAD_GRAYSCALE);
        Mat img2 = Imgcodecs.imread(uri + "test2.png", Imgcodecs.IMREAD_GRAYSCALE);
        Mat img = new Mat();
        //像素做差
        Core.absdiff(img1, img2, img);

        //腐蚀  可以看到腐蚀是把暗度增强，
        // 达到去除噪声的目的，但弊端是比较细小的点会被当作噪点忽略掉，
        // 腐蚀的强度与kernel（腐蚀范围）、iterations（腐蚀次数）有关。
        Mat kernel = Imgproc.getStructuringElement(1, new Size(3, 3));
        Imgproc.erode(img, img, kernel, new Point(-1, -1), 2);

        //膨胀 膨胀与腐蚀相反，先腐蚀后膨胀就可以达到将相同点去除，突出不同点的效果。
        Mat kernel1 = Imgproc.getStructuringElement(1, new Size(2, 3));
        Imgproc.dilate(img, img, kernel1);

        Mat threshImg = new Mat();
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy = new Mat();

        //转化成灰度
//        Imgproc.cvtColor(img, threshImg, Imgproc.COLOR_RGB2GRAY);

//        //检测边缘
        Imgproc.threshold(threshImg, threshImg, 20, 255, Imgproc.THRESH_BINARY);

        //找到轮廓(3：CV_RETR_TREE，2：CV_CHAIN_APPROX_SIMPLE)
        Imgproc.findContours(threshImg, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));

        List<Rect> boundRect = new ArrayList<Rect>();
        for (int i = 0; i < contours.size(); i++) {
            //根据轮廓生成外包络矩形
            Rect rect = Imgproc.boundingRect(contours.get(i));
            Log.d(TAG, "Rect: " + contours.size() + "w:" + rect.width + "--h:" + rect.height);
            if (rect.width * rect.height < 200) continue; //框的阈值可以根据图片大小来确定，只要是小于某个值，则是误差
            boundRect.add(rect);
        }

        for (int i = 0; i < boundRect.size(); i++) {
            Scalar color = new Scalar(0, 0, 255);
            //绘制矩形
            Imgproc.rectangle(img1, boundRect.get(i).tl(), boundRect.get(i).br(), color, 2, Core.LINE_8, 0);
        }
        Log.d(TAG, "onCreate: Rect" + boundRect.size());

//        Utils.matToBitmap(img1, testBitmap);
//        imageView.setImageBitmap(testBitmap);

        Bitmap testBitmap2 = Bitmap.createBitmap(img1.cols(), img1.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img2, testBitmap2);
        imageView2.setImageBitmap(testBitmap2);
    }


    /**
     * @方法描述 将RGB字节数组转换成Bitmap，
     */
    static public Bitmap rgb2Bitmap(byte[] data, int width, int height) {
        int[] colors = convertByteToColor(data);    //取RGB值转换为int数组
        if (colors == null) {
            return null;
        }
        Bitmap bmp = Bitmap.createBitmap(colors, 0, width, width, height,
                Bitmap.Config.ARGB_8888);
        return bmp;
    }

    // 将一个byte数转成int
    // 实现这个函数的目的是为了将byte数当成无符号的变量去转化成int
    public static int convertByteToInt(byte data) {
        int heightBit = (int) ((data >> 4) & 0x0F);
        int lowBit = (int) (0x0F & data);
        return heightBit * 16 + lowBit;
    }

    // 将纯RGB数据数组转化成int像素数组
    public static int[] convertByteToColor(byte[] data) {
        int size = data.length;
        if (size == 0) {
            return null;
        }
        int arg = 0;
//        if (size % 4 != 0) {
//            arg = 1;
//        }
        // 一般RGB字节数组的长度应该是3的倍数，
        // 不排除有特殊情况，多余的RGB数据用黑色0XFF000000填充
        int[] color = new int[size * 3];
        int red, green, blue;
        int colorLen = color.length;

        for (int i = 0; i < colorLen; ++i) {
            red = convertByteToInt(data[i]);
            green = convertByteToInt(data[i]);
            blue = convertByteToInt(data[i]);
            // 获取RGB分量值通过按位或生成int的像素值
            color[i] = (red << 16) | (green << 8) | blue | 0xFF000000;
        }

        return color;
    }

    /**
     * @方法描述 Bitmap转RGB 获取灰度图像的单通道
     */
    public static byte[] getSingleRGBFromBMP(Bitmap bmp) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        byte[] pixels = new byte[w * h]; // Allocate for RGB
        int k = 0;
        for (int x = 0; x < h; x++) {
            for (int y = 0; y < w; y++) {
                int color = bmp.getPixel(y, x);
                //只取单通道
                pixels[y] = (byte) Color.red(color);
            }
        }
        return pixels;
    }

    public int saveImageToGallery(Bitmap bmp) {
        //生成路径
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        String dirName = "erweima16";
        File appDir = new File(root, dirName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        //文件名为时间
//        long timeStamp = System.currentTimeMillis();
//        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String sd = sdf.format(new Date(timeStamp));
        String fileName = "test" + System.currentTimeMillis() + ".jpg";

        //获取文件
        File file = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            return 2;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public static Bitmap getImage(Bitmap srcBitmap, boolean test) {
        Mat src = new Mat(srcBitmap.getHeight(), srcBitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(srcBitmap, src);

        Mat grayMat = new Mat();
//        Imgproc.cvtColor(src, grayMat, Imgproc.COLOR_RGB2GRAY);//rgbMat to gray grayMat

        //缩放
        float scale = 0.5f;
        float width = src.width();
        float height = src.height();
        Size size = new Size(width * scale, height * scale);

        Imgproc.resize(src, grayMat, size);
//        Imgcodecs.imwrite(Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/erweima16/Test_" + System.currentTimeMillis() + ".jpg",grayMat);

        byte[] nowImg = new byte[grayMat.width() * grayMat.height()];
        grayMat.get(0, 0, nowImg);

        float[] depthImg = new float[grayMat.width() * grayMat.height()];


        int result = JniTest.stereoInterface(baseImgResizeData, nowImg, depthImg, 0, 0, 400, 640);

        Log.d(TAG, "getImage: " + baseImgResizeData.length + "--" + nowImg.length + "--" + depthImg.length + "---result is " + result);

        Mat mat = new Mat(grayMat.height(), grayMat.width(), CvType.CV_32FC1);
        mat.put(0, 0, depthImg);

        Bitmap processedImage = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(mat, processedImage);
        return processedImage;
    }


    public Bitmap getImage(Mat src) {

//        Mat src = new Mat(srcBitmap.getHeight(), srcBitmap.getWidth(), CvType.CV_8UC1);
//        Utils.bitmapToMat(srcBitmap, src);

        Mat grayMat = new Mat();
        //缩放
        float scale = 0.5f;
        float width = src.width();
        float height = src.height();
        Size size = new Size(width * scale, height * scale);

        Imgproc.resize(src, grayMat, size);
        Log.d(TAG, "getImage resize: " + grayMat.get(0,0)[0]);

        double[] test = grayMat.get(0,0);
        Log.d(TAG, "onCreate: grayMat size" + test.length);
        for (int i=0;i<test.length;i++){
            Log.d(TAG, "onCreate: double2: " + test[i]);
        }

        Bitmap bitmap3 = Bitmap.createBitmap(grayMat.cols(), grayMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(grayMat, bitmap3);
        imageView2.setImageBitmap(bitmap3);



//        Imgcodecs.imwrite(Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/erweima16/Test_" + System.currentTimeMillis() + ".jpg",grayMat);

        byte[] nowImg = new byte[grayMat.width() * grayMat.height()];
        grayMat.get(0, 0, nowImg);
//        baseBitmapResizeMat.get(0, 0, nowImg);
        Log.d(TAG, "getImage:nowImg: " + nowImg[0]);
        float[] depthImg = new float[grayMat.width() * grayMat.height()];

        for (int i=0;i<200;i++){
            Log.d(TAG, "getImage: baseImgResizeData:" + baseImgResizeData[i] + " nowImg:"+nowImg[i]);
        }

        Log.d(TAG, "getImage:对比传入的两个数组： " + equals(baseImgResizeData,nowImg));

//        LogUtil.d("--" + MyApplication.getBaseImgResizeData().length + "--" + nowImg.length + "--" + depthImg.length);
        int result = JniTest.stereoInterface(baseImgResizeData, nowImg, depthImg, 0, 0, 400, 640);
//        int result = JniTest.stereoInterface(baseImgResizeData, baseImgResizeData, depthImg, 0, 0, 400, 640);

//        LogUtil.d("返回结果是：" + result);
        Log.d(TAG, "返回结果是：" + result);
        final float[] testDatat = depthImg;

        writeFileToSDCard(testDatat, null, null, false, true);

        Mat mat = new Mat(grayMat.height(), grayMat.width(), CvType.CV_32FC3);
        int cols = grayMat.width();
        int rows = grayMat.height();

        Mat mat1 = new Mat(rows, cols, CvType.CV_32FC1);
        mat1.put(0, 0, depthImg);

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (depthImg[i + j * cols] >= 5000.0 || depthImg[i + j * cols] == 0.0) {
                    mat.put(j, i, 60, 60, 0);
                } else {
                    mat.put(j, i, 60, 60, 255 - fun((float) mat1.get(j, i)[0]));
                }
            }
        }
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_HSV2RGB);
        mat.convertTo(mat, CvType.CV_8UC4);

        Log.d(TAG, "getImage: matsize: " + mat.size());

        Bitmap processedImage = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, processedImage);

        src.release();
        mat.release();
        grayMat.release();
        return processedImage;
    }


    public Bitmap getImage(Bitmap srcBitmap) {
        Mat src = new Mat(srcBitmap.getHeight(), srcBitmap.getWidth(), CvType.CV_8UC4);
        Utils.bitmapToMat(srcBitmap, src);
        Imgproc.cvtColor(src,src,Imgproc.COLOR_RGB2GRAY);

        Mat grayMat = new Mat();
        //缩放
        float scale = 0.5f;
        float width = src.width();
        float height = src.height();
        Size size = new Size(width * scale, height * scale);

        Imgproc.resize(src, grayMat, size);

        Bitmap bitmap3 = Bitmap.createBitmap(grayMat.cols(), grayMat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(grayMat, bitmap3);
        imageView2.setImageBitmap(bitmap3);


//        Imgcodecs.imwrite(Environment.getExternalStorageDirectory().getAbsolutePath()
//                + "/erweima16/Test_" + System.currentTimeMillis() + ".jpg",grayMat);
        double[] d = grayMat.get(90,340);
        Log.d(TAG, "getImage: double :" + d.length);

        byte[] nowImg = new byte[grayMat.width() * grayMat.height()];
        grayMat.get(0, 0, nowImg);
        Log.d(TAG, "getImage: double : " + nowImg[36340] + "--" + nowImg[36341]);


        float[] depthImg = new float[grayMat.width() * grayMat.height()];


//        int result = JniTest.stereoInterface(nowImg, nowImg, depthImg, 0, 0, 400, 640);
        int result = JniTest.stereoInterface(baseImgResizeData, nowImg, depthImg, 0, 0, 400, 640);

//        LogUtil.d("返回结果是：" + result);
        Log.d(TAG, "返回结果是：" + result);
        final float[] testDatat = depthImg;

        writeFileToSDCard(testDatat, null, null, false, true);

        Mat mat = new Mat(grayMat.height(), grayMat.width(), CvType.CV_32FC3);
        int cols = grayMat.width();
        int rows = grayMat.height();

        Mat mat1 = new Mat(rows, cols, CvType.CV_32FC1);
        mat1.put(0, 0, depthImg);

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                if (depthImg[i + j * cols] >= 5000.0 || depthImg[i + j * cols] == 0.0) {
                    mat.put(j, i, 60, 60, 0);
                } else {
                    mat.put(j, i, 60, 60, 255 - fun((float) mat1.get(j, i)[0]));
                }
            }
        }

        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_HSV2RGB);
        mat.convertTo(mat, CvType.CV_8UC4);


        Bitmap processedImage = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, processedImage);

        src.release();
        mat.release();
        grayMat.release();
        return processedImage;
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

    /*
     * 此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
     *
     * @param buffer   写入文件的内容
     * @param folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
     * @param fileName 文件名称，默认app_log.txt
     * @param append   是否追加写入，true为追加写入，false为重写文件
     * @param autoLine 针对追加模式，true为增加时换行，false为增加时不换行
     */
    public synchronized static void writeFileToSDCard(@NonNull final float[] buffer, @Nullable final String folder,
                                                      @Nullable final String fileName, final boolean append, final boolean autoLine) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                boolean sdCardExist = Environment.getExternalStorageState().equals(
//                        android.os.Environment.MEDIA_MOUNTED);
//                String folderPath = "";
//                if (sdCardExist) {
//                    //TextUtils为android自带的帮助类
//                    if (TextUtils.isEmpty(folder)) {
//                        //如果folder为空，则直接保存在sd卡的根目录
//                        folderPath = Environment.getExternalStorageDirectory()
//                                + File.separator;
//                    } else {
//                        folderPath = Environment.getExternalStorageDirectory()
//                                + File.separator + folder + File.separator;
//                    }
//                } else {
//                    return;
//                }

                Log.d(TAG, "run: start");

                String folderPath = Environment.getExternalStorageDirectory()
                        + File.separator;

                File fileDir = new File(folderPath);
                if (!fileDir.exists()) {
                    if (!fileDir.mkdirs()) {
                        return;
                    }
                }
                File file;
                //判断文件名是否为空
                if (TextUtils.isEmpty(fileName)) {
                    file = new File(folderPath + "app_log_new.txt");
                } else {
                    file = new File(folderPath + fileName);
                }
                RandomAccessFile raf = null;
                FileOutputStream out = null;
                DataOutputStream dos = null;
                try {
                    if (append) {
                        //如果为追加则在原来的基础上继续写文件
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(file.length());
//                        raf.writef(buffer);
                        if (autoLine) {
                            raf.write("\n".getBytes());
                        }
                    } else {
                        //重写文件，覆盖掉原来的数据
                        out = new FileOutputStream(file);
                        dos = new DataOutputStream(out);

                        for (int i = 0; i < buffer.length; i++) {
                            if (buffer[i] == 0.0) continue;
                            dos.writeChars(buffer[i] + "(" + i + ")|");
                        }

//                        for (float f : buffer) {
//                            if (f == 0.0) continue;
//                            dos.writeChars(f + "|");
//                        }
                        dos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Log.d(TAG, "run: write finish");
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                        if (dos != null) {
                            dos.close();
                        }
                        if (out != null) {
                            out.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /*
     * 此方法为android程序写入sd文件文件，用到了android-annotation的支持库@
     *
     * @param buffer   写入文件的内容
     * @param folder   保存文件的文件夹名称,如log；可为null，默认保存在sd卡根目录
     * @param fileName 文件名称，默认app_log.txt
     * @param append   是否追加写入，true为追加写入，false为重写文件
     * @param autoLine 针对追加模式，true为增加时换行，false为增加时不换行
     */
    public synchronized static void writeFileToSDCard(@NonNull final byte[] buffer, @Nullable final String folder,
                                                      @Nullable final String fileName, final boolean append, final boolean autoLine) {
        new Thread(new Runnable() {
            @Override
            public void run() {
//                boolean sdCardExist = Environment.getExternalStorageState().equals(
//                        android.os.Environment.MEDIA_MOUNTED);
//                String folderPath = "";
//                if (sdCardExist) {
//                    //TextUtils为android自带的帮助类
//                    if (TextUtils.isEmpty(folder)) {
//                        //如果folder为空，则直接保存在sd卡的根目录
//                        folderPath = Environment.getExternalStorageDirectory()
//                                + File.separator;
//                    } else {
//                        folderPath = Environment.getExternalStorageDirectory()
//                                + File.separator + folder + File.separator;
//                    }
//                } else {
//                    return;
//                }

                Log.d(TAG, "run: start");

                String folderPath = Environment.getExternalStorageDirectory()
                        + File.separator;

                File fileDir = new File(folderPath);
                if (!fileDir.exists()) {
                    if (!fileDir.mkdirs()) {
                        return;
                    }
                }
                File file;
                //判断文件名是否为空
                if (TextUtils.isEmpty(fileName)) {
                    file = new File(folderPath + "app_log_new.txt");
                } else {
                    file = new File(folderPath + fileName);
                }
                RandomAccessFile raf = null;
                FileOutputStream out = null;
                DataOutputStream dos = null;
                try {
                    if (append) {
                        //如果为追加则在原来的基础上继续写文件
                        raf = new RandomAccessFile(file, "rw");
                        raf.seek(file.length());
//                        raf.writef(buffer);
                        if (autoLine) {
                            raf.write("\n".getBytes());
                        }
                    } else {
                        //重写文件，覆盖掉原来的数据
                        out = new FileOutputStream(file);
                        dos = new DataOutputStream(out);

                        for (int i = 0; i < buffer.length; i++) {
                            if (buffer[i] == 0.0) continue;
                            dos.writeChars(buffer[i] + "(" + i + ")|");
                        }

//                        for (float f : buffer) {
//                            if (f == 0.0) continue;
//                            dos.writeChars(f + "|");
//                        }
                        dos.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Log.d(TAG, "run: write finish");
                    try {
                        if (raf != null) {
                            raf.close();
                        }
                        if (dos != null) {
                            dos.close();
                        }
                        if (out != null) {
                            out.close();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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

    /**
     * 比较两个byte数组数据是否相同,相同返回 true
     *
     * @param data1
     * @param data2
     * @param len
     * @return
     */
    public static boolean memcmp(byte[] data1, byte[] data2, int len) {
        if (data1 == null && data2 == null) {
            return true;
        }
        if (data1 == null || data2 == null) {
            return false;
        }
        if (data1 == data2) {
            return true;
        }
        boolean bEquals = true;
        int i;
        for (i = 0; i < data1.length && i < data2.length && i < len; i++) {
            if (data1[i] != data2[i]) {
                Log.d(TAG, "memcmp: 不相等 i: " + i + " data1: " + data1[i] + " data2: " + data2[i]);
                bEquals = false;
                break;
            }
        }
        return bEquals;
    }

    public static boolean equals(byte[] data1, byte[] data2) {
        if (data1 == null && data2 == null)
        {
            return true;
        }
        if (data1 == null || data2 == null) {
            Log.d(TAG, "equals: 一个是null");
            return false;
        }
        if (data1.length != data2.length) {
            Log.d(TAG, "equals: 长度不等");
            return false;
        }
        return memcmp(data1, data2, data1.length);
    }

}
