package ru.trickyfoxy.lab1;

import java.util.Random;

import static java.lang.Math.*;

public class Main {

    public static void main(String[] args) {
        // 1
        long[] d = new long[16];
        for (int i = 2; i < 15; i++) {
            d[i - 2] = i;
        }
        // 2
        double[] x = new double[12];
        double rangeMin = -13.0;
        double rangeMax = 11.0;
        for (int i = 0; i < 12; i++) {
            Random r = new Random(48);
            x[i] = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
        }
        //3
        double dd[][] = new double[14][12];
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 12; j++) {
                if (d[i] == 10) {
                    dd[i][j] = cos(cbrt(x[j]) + 3.0 / 4.0);
                } else if (d[i] >= 6 && d[i] <= 9 || d[i] >= 13 && d[i] <= 15) {
                    dd[i][j] = sin(log(pow(tan(x[j]), 2)));
                } else {
                    dd[i][j] = cos(pow(pow(2 * tan(x[j]), 2) / (1 - cbrt(pow(x[j], x[j]))), 2));
                }
            }
        }
        // 4
        for (int i = 0; i < 14; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.format("%.5f ", dd[i][j]);
            }
            System.out.println();
        }

    }
}
