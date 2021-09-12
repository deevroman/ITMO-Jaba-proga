package ru.trickyfoxy.lab8.graphics;

import java.awt.*;
import java.awt.geom.Line2D;

public class LineShape implements Shapes {
    Graphics2D g2D;
    double x1 = 0;
    double y1 = 0;
    double x2 = 0;
    double y2 = 0;

    protected LineShape() {
    }

    public LineShape(Graphics2D g2D, double x1, double y1, double x2, double y2) {
        this.g2D = g2D;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public Shapes process() {
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setStroke(new BasicStroke(5));
        g2D.draw(new Line2D.Float((float) x1, (float) y1, (float) x2, (float) y2));

        return this;
    }

    @Override
    public double getCenterX() {
        return x1;
    }

    @Override
    public double getCenterY() {
        return y1;
    }

    @Override
    public double getCenterX2() {
        return x2;
    }

    @Override
    public double getCenterY2() {
        return y2;
    }

}
