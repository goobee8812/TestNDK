package com.example.testndk.bean;

/**
 * 获取深度图数据参数配置
 */
public class AdaptiveStateBean {
    int SADWindowSize;                     //SAD窗口大小
    int minDisparity;                      //最小视差，一般为负数
    int numberOfDisparities;               //视差范围 = 最大视差+最小视差-1
    int textureThreshold;                  //纹理check 阈值
    int uniquenessRatio;                   //uni check 阈值
    short disp12MaxDiff;                   //LR check 阈值
    short lambdaAD;                        //SAD系数
    short lambdaCensus;                    //census系数
    int focalLength;                       //焦距
    float b;                               //基线宽度
    float z;                               //参考图距离
    boolean isLeftCam;                        //摄像头是否在左边，默认是false
    boolean isfilled;                         //是否补洞
    boolean isDenoise;                        //是否去噪
    int speckleWindowSize;                 //去噪程度，越大去噪越多
    int holeSize;                          //补洞程度，越大补洞越多

    public AdaptiveStateBean() {
        this.SADWindowSize = 1;
        this.minDisparity = 2;
        this.numberOfDisparities = 3;
        this.textureThreshold = 4;
        this.uniquenessRatio = 5;
        this.disp12MaxDiff = 6;
        this.lambdaAD = 7;
        this.lambdaCensus = 8;
        this.focalLength = 9;
        this.b = 10.0f;
        this.z = 11.0f;
        this.isLeftCam = true;
        this.isfilled = true;
        this.isDenoise = true;
        this.speckleWindowSize = 12;
        this.holeSize = 13;
    }

    public int getSADWindowSize() {
        return SADWindowSize;
    }

    public void setSADWindowSize(int SADWindowSize) {
        this.SADWindowSize = SADWindowSize;
    }

    public int getMinDisparity() {
        return minDisparity;
    }

    public void setMinDisparity(int minDisparity) {
        this.minDisparity = minDisparity;
    }

    public int getNumberOfDisparities() {
        return numberOfDisparities;
    }

    public void setNumberOfDisparities(int numberOfDisparities) {
        this.numberOfDisparities = numberOfDisparities;
    }

    public int getTextureThreshold() {
        return textureThreshold;
    }

    public void setTextureThreshold(int textureThreshold) {
        this.textureThreshold = textureThreshold;
    }

    public int getUniquenessRatio() {
        return uniquenessRatio;
    }

    public void setUniquenessRatio(int uniquenessRatio) {
        this.uniquenessRatio = uniquenessRatio;
    }

    public short getDisp12MaxDiff() {
        return disp12MaxDiff;
    }

    public void setDisp12MaxDiff(short disp12MaxDiff) {
        this.disp12MaxDiff = disp12MaxDiff;
    }

    public short getLambdaAD() {
        return lambdaAD;
    }

    public void setLambdaAD(short lambdaAD) {
        this.lambdaAD = lambdaAD;
    }

    public short getLambdaCensus() {
        return lambdaCensus;
    }

    public void setLambdaCensus(short lambdaCensus) {
        this.lambdaCensus = lambdaCensus;
    }

    public int getFocalLength() {
        return focalLength;
    }

    public void setFocalLength(int focalLength) {
        this.focalLength = focalLength;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        this.b = b;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        z = z;
    }

    public boolean isLeftCam() {
        return isLeftCam;
    }

    public void setLeftCam(boolean leftCam) {
        isLeftCam = leftCam;
    }

    public boolean isIsfilled() {
        return isfilled;
    }

    public void setIsfilled(boolean isfilled) {
        this.isfilled = isfilled;
    }

    public boolean isDenoise() {
        return isDenoise;
    }

    public void setDenoise(boolean denoise) {
        isDenoise = denoise;
    }

    public int getSpeckleWindowSize() {
        return speckleWindowSize;
    }

    public void setSpeckleWindowSize(int speckleWindowSize) {
        this.speckleWindowSize = speckleWindowSize;
    }

    public int getHoleSize() {
        return holeSize;
    }

    public void setHoleSize(int holeSize) {
        this.holeSize = holeSize;
    }
}
