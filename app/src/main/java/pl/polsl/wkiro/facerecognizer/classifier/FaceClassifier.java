package pl.polsl.wkiro.facerecognizer.classifier;

import org.opencv.core.Mat;

public class FaceClassifier {

    public FaceClassifier() {
    }

    public void release() {
    }

    public void loadClassifier() {
    }

    public void saveClassifier() {
    }

    public void trainClassifier(Mat faceFrame, String person) {

    }

    public String recognizeFace(Mat faceFrame) {
        return "Unknown";
    }

    private static native void load(String path);

    private static native void save(String path);

    private static native void train (long faceFrameAddr, int label);

    private static native int predict(long faceFrameAddr);
}
