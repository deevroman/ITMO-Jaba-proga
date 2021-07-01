package ru.trickyfoxy.lab8.graphics;

public class Geomic {
    public static boolean pointInSegment(double x1, double y1, double x2, double y2, double x3, double y3) {
        double tmp;
        if (x1 > x2) {
            tmp = x1;
            x1 = x2;
            x2 = tmp;

            tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
        // TODO переписать через расстояние до отрезка
        double A = y1 - y2;
        double B = x2 - x1;
        double C = x1 * y2 - x2 * y1;

        if (Math.abs(A * x3 + B * y3 + C) <= 2500) {
            if (y1 < y2) {
                return x1 <= x3 && x3 <= x2 && y1 <= y3 && y3 <= y2;
            } else {
                return x1 <= x3 && x3 <= x2 && y2 <= y3 && y3 <= y1;
            }
        }
        return false;
    }
}
