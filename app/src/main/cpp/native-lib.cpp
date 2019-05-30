#include <jni.h>
#include <string>
#include <android/log.h>
#include "stereo_interface.h"

#define LOG_TAG  "C_TAG"
#define LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_testndk_JniTest_goobeeStringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}


extern "C" JNIEXPORT jint JNICALL
Java_com_example_testndk_JniTest_stereoInterface(
        JNIEnv* env,
        jobject/* this */,
        jbyteArray right,
        jbyteArray left,
        jfloatArray depth,
//        jboolean* right,
//        jboolean* left,
//        jfloat* depth,
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
    memset(c_right_array, 0, sizeof(jbyte)*len_arr);
    memset(c_left_array, 0, sizeof(jbyte)*len_arr);
    memset(c_float_array, 0, sizeof(jfloat)*len_float_arr);

//    4. 拷贝java数组数据
    env->GetByteArrayRegion(right, 0, len_arr, c_right_array);
    unsigned char* rightBuf = (unsigned char*)c_right_array;

    LOGD("printf log 255508 = %d",rightBuf[255508]);
    LOGD("printf log 255509 = %d",rightBuf[255509]);
    LOGD("printf log 255510 = %d",rightBuf[255510]);
    LOGD("printf log 255511 = %d",rightBuf[255511]);

    env->GetByteArrayRegion(left, 0, len_arr, c_left_array);
    unsigned char* leftBuf = (unsigned char*)c_left_array;

    env->GetFloatArrayRegion(depth, 0, len_arr, c_float_array);
    float* depthBuf = c_float_array;

//    jbyte* bBuffer = (env)->GetByteArrayElements(right,0);
//    unsigned char* rightBuf = (unsigned char*)bBuffer;
//    jbyte* bBuffer2 = (env)->GetByteArrayElements(left,0);
//    unsigned char* leftBuf = (unsigned char*)bBuffer2;
//    jfloat * bBuffer3 = (env)->GetFloatArrayElements(depth,0);
//    float* depthBuf = bBuffer3;
    //获取Java中的实例类
//    jclass objectClass = (env)->FindClass("com/example/testndk/FaceRectIn");

    faceRectMy s;
    s.x = x;
    s.y = y;
    s.width = width;
    s.height = height;

    jint result = stereo_interface(rightBuf,leftBuf,depthBuf,s);
    env->SetFloatArrayRegion(depth,0,len_float_arr,c_float_array);  //设置输出值

    free(c_right_array);
    free(c_left_array);
    free(c_float_array);
    return  result;
}

