package com.example.sargis_kh.model;

/**
 * Created by Sargis_Kh on 5/4/2016.
 */
public class Vector2 {

    private double xx;
    private double yy;

    public Vector2() {
        this.xx = 0;
        this.yy = 0;
    }

    public Vector2(double x, double y) {
        this.xx = x;
        this.yy = y;
    }

    public double x() {
        return xx;
    }

    public double y() {
        return yy;
    }

    public void setX(double x) {
        xx = x;
    }

    public void setY(double y) {
        yy = y;
    }
}
