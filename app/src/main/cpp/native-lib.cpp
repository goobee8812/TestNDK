#include <jni.h>
#include <string>
#include <android/log.h>
#include "stereo_interface.h"

#define LOG_TAG  "C_TAG"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testndk_JniTest_goobeeStringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jint JNICALL
Java_com_example_testndk_JniTest_stereoInterface(
        JNIEnv *env,
        jobject/* this */,
        jbyteArray right,
        jbyteArray left,
        jfloatArray depth,
        jint x,
        jint y,
        jint width,
        jint height) {

    jbyte *c_right_array;
    jbyte *c_left_array;
    jfloat *c_float_array;
    jint len_arr;
    jint len_float_arr;
    //1. 获取数组长度
    len_arr = env->GetArrayLength(right);
    len_float_arr = env->GetArrayLength(depth);
    //2. 申请缓冲区
    c_right_array = (jbyte *) malloc(sizeof(jbyte) * len_arr);
    c_left_array = (jbyte *) malloc(sizeof(jbyte) * len_arr);
    c_float_array = (jfloat *) malloc(sizeof(jfloat) * len_float_arr);
    //3. 初始化缓冲区
    memset(c_right_array, 0, sizeof(jbyte) * len_arr);
    memset(c_left_array, 0, sizeof(jbyte) * len_arr);
    memset(c_float_array, 0, sizeof(jfloat) * len_float_arr);

//    4. 拷贝java数组数据
    env->GetByteArrayRegion(right, 0, len_arr, c_right_array);
    unsigned char *rightBuf = (unsigned char *) c_right_array;

//    LOGD("printf log 255508 = %d",rightBuf[255508]);
//    LOGD("printf log 255509 = %d",rightBuf[255509]);
//    LOGD("printf log 255510 = %d",rightBuf[255510]);
//    LOGD("printf log 255511 = %d",rightBuf[255511]);

    env->GetByteArrayRegion(left, 0, len_arr, c_left_array);
    unsigned char *leftBuf = (unsigned char *) c_left_array;

    env->GetFloatArrayRegion(depth, 0, len_arr, c_float_array);
    float *depthBuf = c_float_array;

    faceRectMy s;
    s.x = x;
    s.y = y;
    s.width = width;
    s.height = height;

    jint result = stereo_interface(rightBuf, leftBuf, depthBuf, s);
    env->SetFloatArrayRegion(depth, 0, len_float_arr, c_float_array);  //设置输出值

    //5. 释放资源
    free(c_right_array);
    free(c_left_array);
    free(c_float_array);
    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_testndk_JniTest_stereoInterface2(
        JNIEnv *env,
        jobject/* this */,
        jbyteArray right,
        jbyteArray left,
        jfloatArray depth,
        jint x,
        jint y,
        jint width,
        jint height,
        jboolean isShort) {

    jbyte *c_right_array;
    jbyte *c_left_array;
    jfloat *c_float_array;
    jint len_arr;
    jint len_float_arr;
    //1. 获取数组长度
    len_arr = env->GetArrayLength(right);
    len_float_arr = env->GetArrayLength(depth);
    //2. 申请缓冲区
    c_right_array = (jbyte *) malloc(sizeof(jbyte) * len_arr);
    c_left_array = (jbyte *) malloc(sizeof(jbyte) * len_arr);
    c_float_array = (jfloat *) malloc(sizeof(jfloat) * len_float_arr);
    //3. 初始化缓冲区
    memset(c_right_array, 0, sizeof(jbyte) * len_arr);
    memset(c_left_array, 0, sizeof(jbyte) * len_arr);
    memset(c_float_array, 0, sizeof(jfloat) * len_float_arr);

//    4. 拷贝java数组数据
    env->GetByteArrayRegion(right, 0, len_arr, c_right_array);
    unsigned char *rightBuf = (unsigned char *) c_right_array;

//    LOGD("printf log 255508 = %d",rightBuf[255508]);
//    LOGD("printf log 255509 = %d",rightBuf[255509]);
//    LOGD("printf log 255510 = %d",rightBuf[255510]);
//    LOGD("printf log 255511 = %d",rightBuf[255511]);

    env->GetByteArrayRegion(left, 0, len_arr, c_left_array);
    unsigned char *leftBuf = (unsigned char *) c_left_array;

    env->GetFloatArrayRegion(depth, 0, len_arr, c_float_array);
    float *depthBuf = c_float_array;

    faceRectMy s;
    s.x = x;
    s.y = y;
    s.width = width;
    s.height = height;

    jint result = stereo_interface(rightBuf, leftBuf, depthBuf, s,isShort);
    env->SetFloatArrayRegion(depth, 0, len_float_arr, c_float_array);  //设置输出值

    //5. 释放资源
    free(c_right_array);
    free(c_left_array);
    free(c_float_array);
    return result;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_example_testndk_JniTest_temperWarpInterface(
        JNIEnv *env,
        jobject/* this */,
        jbyteArray input,
        jbyteArray output,

        jbyteArray mapx,
        jbyteArray mapy,
        jint width,
        jint height,
        jfloat temperature,
        jfloat temp_stand,
        jfloat temp_max,
        jfloat temp_min
) {
    jint result = 0;

    jbyte *c_input_array;
    jbyte *c_output_array;
    jbyte *c_mapx_array;
    jbyte *c_mapy_array;

    jint len_input_arr;
    jint len_mapx_arr;
    jint len_mapy_arr;

    //1. 获取数组长度
    len_input_arr = env->GetArrayLength(input);
    len_mapx_arr = env->GetArrayLength(mapx);
    len_mapy_arr = env->GetArrayLength(mapy);

    //2. 申请缓冲区
    c_input_array = (jbyte *) malloc(sizeof(jbyte) * len_input_arr);
    c_output_array = (jbyte *) malloc(sizeof(jbyte) * len_input_arr);
    c_mapx_array = (jbyte *) malloc(sizeof(jbyte) * len_mapx_arr);
    c_mapy_array = (jbyte *) malloc(sizeof(jbyte) * len_mapy_arr);

    //3. 初始化缓冲区
    memset(c_input_array, 0, sizeof(jbyte) * len_input_arr);
    memset(c_output_array, 0, sizeof(jbyte) * len_input_arr);
    memset(c_mapx_array, 0, sizeof(jbyte) * len_mapx_arr);
    memset(c_mapy_array, 0, sizeof(jbyte) * len_mapy_arr);

    //    4. 拷贝java数组数据
    env->GetByteArrayRegion(input, 0, len_input_arr, c_input_array);
    unsigned char *inputBuf = (unsigned char *) c_input_array;

    env->GetByteArrayRegion(output, 0, len_input_arr, c_output_array);
    unsigned char *outBuf = (unsigned char *) c_output_array;

    env->GetByteArrayRegion(mapx, 0, len_mapx_arr, c_mapx_array);
    unsigned char *mapxBuf = (unsigned char *) c_mapx_array;

    env->GetByteArrayRegion(mapy, 0, len_mapy_arr, c_mapy_array);
    unsigned char *mapyBuf = (unsigned char *) c_mapy_array;

//    temper_warp_interface(inputBuf,outBuf,mapxBuf,mapyBuf,width,height,temperature,temp_stand,temp_max,temp_min);
//    env->SetByteArrayRegion(output, 0, len_input_arr, c_output_array);  //设置输出值
//
//    if ((env)->ExceptionCheck()) {  // 检查JNI调用是否有引发异常
//        (env)->ExceptionDescribe();
//        (env)->ExceptionClear();        // 清除引发的异常，在Java层不会打印异常的堆栈信息
//        result = 1;
//        LOGD("Result : 数组越界异常了！！");
//        //return;
//    }

    try {
        temper_warp_interface(inputBuf,outBuf,mapxBuf,mapyBuf,width,height,temperature,temp_stand,temp_max,temp_min);
        env->SetByteArrayRegion(output, 0, len_input_arr, c_output_array);  //设置输出值
    }catch(...){
        //
        result = 1;
        LOGD("Result : 数组越界异常了！！");
    }

    //5. 释放资源
    free(c_input_array);
    free(c_output_array);
    free(c_mapx_array);
    free(c_mapy_array);

    return result;
}


