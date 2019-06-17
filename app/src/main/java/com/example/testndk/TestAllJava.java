package com.example.testndk;

import android.util.Log;

public class TestAllJava {
    private static final String TAG = "Test";
    public static void main(String[] args){
        String test = new String("咳咳");
        System.out.println("main: " + test);
        change(test);
        System.out.println("main: " + test);
    }

    private static void change(String string){
        string += "hello";  //等于 string = new String
    }
}
