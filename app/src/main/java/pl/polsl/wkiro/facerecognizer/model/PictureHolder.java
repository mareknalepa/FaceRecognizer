package pl.polsl.wkiro.facerecognizer.model;

import org.opencv.core.Mat;

public class PictureHolder {

    private Mat frameRgba;
    private Mat frameGray;

    private PictureHolder() {}

    private final static PictureHolder instance = new PictureHolder();

    public static PictureHolder getInstance() {
        return instance;
    }

    public Mat getFrameRgba() {
        return frameRgba;
    }

    public void setFrameRgba(Mat frameRgba) {
        this.frameRgba = frameRgba.clone();
    }

    public Mat getFrameGray() {
        return frameGray;
    }

    public void setFrameGray(Mat frameGray) {
        this.frameGray = frameGray.clone();
    }
}
