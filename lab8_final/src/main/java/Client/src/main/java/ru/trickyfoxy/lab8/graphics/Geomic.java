package ru.trickyfoxy.lab8.graphics;

/**
 * Класс с методами для геометрических вычислений
 */
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

        double A = y1 - y2;
        double B = x2 - x1;
        double C = x1 * y2 - x2 * y1;

        double dist = Math.abs(A*x3+B*y3+C)/Math.sqrt(A*A+B*B);

        double dist2 = Math.sqrt((x1-x3)*(x1-x3)+(y1-y3)*(y1-y3));
        double dist3 = Math.sqrt((x2-x3)*(x2-x3)+(y2-y3)*(y2-y3));

        return Math.min(dist, Math.min(dist2, dist3)) < 10;

    }
}
