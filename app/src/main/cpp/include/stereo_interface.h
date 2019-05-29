/***
author:hrp
date:2019/03/08
function:stereo_match
***/
#pragma once

typedef struct faceRectMy {
    int x;
    int y;
    int width;
    int height;
} faceRectIn;//人脸框的位置大小

//测距的接口
int stereo_interface(
        unsigned char *Right, //输入的参考底图
        unsigned char *Left,  //输入的实时人脸图
        float *depth, //输出的测距结果
        faceRectIn &rectIn //人脸框的位置大小，会只输出该框的测距结果
);

//温度漂移的接口
void temper_warp_interface(
        unsigned char *input,  //输入的参考底图
        unsigned char *output, //输出底图温度漂移后的图片
        unsigned char *mapx,   //温度x方向漂移的参数
        unsigned char *mapy,   //温度y方向漂移的参数
        int width,             //输入图像的宽
        int height,            //输入图像的高
        float temperature,     //ntc实时读取到的当前温度
        float temp_stand,      //标定参数里面的常温
        float temp_max,        //标定参数里面的最大温度
        float temp_min         //标定参数里面的最小温度
);