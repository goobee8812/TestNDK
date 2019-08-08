package com.example.testndk.bean;

/**
 * Ir摄像头 rgb摄像头参数设置
 */
public class IrRgbStateBean {
    //ir和彩色摄像头内参
    float fxir;
    float fyir;
    float cxir;
    float cyir;
    float fxrgb;
    float fyrgb;
    float cxrgb;
    float cyrgb;

    //ir和彩色摄像头外参
    float r00;
    float r01;
    float r02;
    float r10;
    float r11;
    float r12;
    float r20;
    float r21;
    float r22;
    float t1;
    float t2;
    float t3;

    public IrRgbStateBean() {
        this.fxir = 0.01f;
        this.fyir = 0.02f;
        this.cxir = 0.03f;
        this.cyir = 0.04f;
        this.fxrgb = 0.05f;
        this.fyrgb = 0.06f;
        this.cxrgb = 0.07f;
        this.cyrgb = 0.08f;
        r00 = 1.0f;
        r01 = 2.0f;
        r02 = 3.0f;
        r10 = 4.0f;
        r11 = 5.0f;
        r12 = 6.0f;
        r20 = 7.0f;
        r21 = 8.0f;
        r22 = 9.0f;
        t1 = 10.0f;
        t2 = 20.0f;
        t3 = 30.0f;
    }

    public float getFxir() {
        return fxir;
    }

    public void setFxir(float fxir) {
        this.fxir = fxir;
    }

    public float getFyir() {
        return fyir;
    }

    public void setFyir(float fyir) {
        this.fyir = fyir;
    }

    public float getCxir() {
        return cxir;
    }

    public void setCxir(float cxir) {
        this.cxir = cxir;
    }

    public float getCyir() {
        return cyir;
    }

    public void setCyir(float cyir) {
        this.cyir = cyir;
    }

    public float getFxrgb() {
        return fxrgb;
    }

    public void setFxrgb(float fxrgb) {
        this.fxrgb = fxrgb;
    }

    public float getFyrgb() {
        return fyrgb;
    }

    public void setFyrgb(float fyrgb) {
        this.fyrgb = fyrgb;
    }

    public float getCxrgb() {
        return cxrgb;
    }

    public void setCxrgb(float cxrgb) {
        this.cxrgb = cxrgb;
    }

    public float getCyrgb() {
        return cyrgb;
    }

    public void setCyrgb(float cyrgb) {
        this.cyrgb = cyrgb;
    }

    public float getR00() {
        return r00;
    }

    public void setR00(float r00) {
        this.r00 = r00;
    }

    public float getR01() {
        return r01;
    }

    public void setR01(float r01) {
        this.r01 = r01;
    }

    public float getR02() {
        return r02;
    }

    public void setR02(float r02) {
        this.r02 = r02;
    }

    public float getR10() {
        return r10;
    }

    public void setR10(float r10) {
        this.r10 = r10;
    }

    public float getR11() {
        return r11;
    }

    public void setR11(float r11) {
        this.r11 = r11;
    }

    public float getR12() {
        return r12;
    }

    public void setR12(float r12) {
        this.r12 = r12;
    }

    public float getR20() {
        return r20;
    }

    public void setR20(float r20) {
        this.r20 = r20;
    }

    public float getR21() {
        return r21;
    }

    public void setR21(float r21) {
        this.r21 = r21;
    }

    public float getR22() {
        return r22;
    }

    public void setR22(float r22) {
        this.r22 = r22;
    }

    public float getT1() {
        return t1;
    }

    public void setT1(float t1) {
        this.t1 = t1;
    }

    public float getT2() {
        return t2;
    }

    public void setT2(float t2) {
        this.t2 = t2;
    }

    public float getT3() {
        return t3;
    }

    public void setT3(float t3) {
        this.t3 = t3;
    }
}
