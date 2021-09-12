package ru.trickyfoxy.lab8.graphics;

import java.awt.*;
import java.awt.geom.Ellipse2D;

public class PointShape implements Shapes {
    Graphics2D g2D;
    double x = 0;
    float y = 0;
    int pointWidth = 4;

    protected PointShape(){}

    public PointShape(Graphics2D g2D, double x, float y){
        this.g2D = g2D;
        this.x = x;
        this.y = y;
    }

    public PointShape(Graphics2D g2D, double x, float y, int pointWidth) {
        this.g2D = g2D;
        this.x = x;
        this.y = y;
        this.pointWidth = pointWidth;
    }

    @Override
    public Shapes process() {
        double ovalW = pointWidth;
        double ovalH = pointWidth;
        Ellipse2D point = new Ellipse2D.Double(x, y, ovalW * 2, ovalH * 2);
        g2D.fill(point);
        return this;
    }

    @Override
    public double getCenterX() {
        return x;
    }

    @Override
    public double getCenterY() {
        return y;
    }

    @Override
    public double getCenterX2() {
        return getCenterX();
    }

    @Override
    public double getCenterY2() {
        return getCenterY();
    }

}
