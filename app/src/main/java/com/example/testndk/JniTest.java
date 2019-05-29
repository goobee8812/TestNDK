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
//    public native int stereoInterface(int x,int y,int width,int height);
}
