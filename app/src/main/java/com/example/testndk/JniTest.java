package com.example.testndk;

public class JniTest {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("base-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String goobeeStringFromJNI();

    public static native int stereoInterface(byte[]right,byte[]left,float[]depth,int x,int y,int width,int height);
    public static native int stereoInterface2(byte[]right,byte[]left,float[]depth,int x,int y,int width,int height,boolean isShort);

    public static native int temperWarpInterface(byte[]input,byte[]output,byte[]mapx,byte[]mapy,int width,int height,float temperature,float temp_stand,float temp_max,float temp_min);
//    public native int stereoInterface(int x,int y,int width,int height);
}
