package pl.polsl.wkiro.facerecognizer.model;

import org.opencv.core.Scalar;

public class ColorsFactory {

    private final static Scalar[] possibleColors = {
            new Scalar(0x3A, 0x50, 0x6B, 0xFF),
            new Scalar(0x26, 0x2A, 0x10, 0xFF),
            new Scalar(0x79, 0x12, 0x05, 0xFF),
            new Scalar(0xD6, 0xBB, 0xC0, 0xFF),
            new Scalar(0xDC, 0x6A, 0xCF, 0xFF),
            new Scalar(0xF2, 0xC1, 0x4E, 0xFF),
            new Scalar(0x75, 0x4F, 0x44, 0xFF)
    };

    private static int currentColor = 0;

    public static Scalar createColor() {
        Scalar color = possibleColors[currentColor];
        ++currentColor;
        currentColor %= possibleColors.length;
        return color;
    }
}
