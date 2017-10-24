package com.example.apichaya.addrealmsudent.customs;

import com.example.apichaya.addrealmsudent.Model.RgbColorObject;

import java.util.ArrayList;

/**
 * Created by apple on 10/23/2017 AD.
 */

public class Functions {
    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 2;
    public static final int COLOR_BLUE = 3;

    public static double getAVE(ArrayList<RgbColorObject> arrayList, int color) {
        int sum = 0;
        int index = 0;

        for (RgbColorObject object : arrayList) {
            switch (color) {
                case COLOR_RED:
                    sum += object.getRedValue();
                    break;
                case COLOR_GREEN:
                    sum += object.getGreenValue();
                    break;
                case COLOR_BLUE:
                    sum += object.getBlueValue();
                    break;
            }
            index++;
        }

        return getSumAVE(sum, index);
    }

    private static double getSumAVE(int val1, int index) {
        double sum;
        sum = val1 / index;
        return sum;
    }

    public static double getSD(ArrayList<RgbColorObject> arrayList, double ave, int color) {
        double sdRed = 0;
        int index = 0;

        for (RgbColorObject object : arrayList) {
            switch (color) {
                case COLOR_RED:
                    sdRed += Math.pow(object.getRedValue() - ave, 2);

                    break;
                case COLOR_GREEN:
                    sdRed += Math.pow(object.getGreenValue() - ave, 2);
                    break;
                case COLOR_BLUE:
                    sdRed += Math.pow(object.getBlueValue() - ave, 2);
                    break;
            }
            index++;
        }

        return Math.pow((sdRed / (index - 1)), 0.5);

    }

}
