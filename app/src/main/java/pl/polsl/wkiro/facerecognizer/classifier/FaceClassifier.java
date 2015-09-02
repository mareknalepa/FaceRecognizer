package pl.polsl.wkiro.facerecognizer.classifier;

import android.content.Context;

import org.opencv.core.Mat;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceClassifier {

    private Context context;
    private String modelPath;

    public FaceClassifier(Context c) {
        context = c;
        modelPath = context.getFilesDir() + File.separator + "face_recognizer_model";
        init();
    }

    public void trainClassifier(ClassifierDatabase classifierDatabase) {
        classifierDatabase.load();
        List<Mat> images = classifierDatabase.getImages();
        List<String> labels = classifierDatabase.getLabels();

        long[] imagesAddrArray = new long[images.size()];
        for (int i = 0; i < images.size(); ++i) {
            imagesAddrArray[i] = images.get(i).getNativeObjAddr();
        }

        Map<String, Integer> labelMap = prepareLabels(labels);
        int[] numericLabels = new int[labels.size()];
        for (int i = 0; i < labels.size(); ++i) {
            numericLabels[i] = labelMap.get(labels.get(i));
        }

        train(imagesAddrArray, numericLabels, modelPath);
        classifierDatabase.deleteExamples();
    }

    public void loadClassifier() {
        load(modelPath);
    }

    public String recognizeFace(Mat faceFrame) {
        return "Unknown";
    }

    private Map<String, Integer> prepareLabels(List<String> labelsList) {
        Map<String, Integer> labels = new HashMap<>();
        int counter = 0;
        for (String l : labelsList) {
            if (!labels.containsKey(l)) {
                labels.put(l, counter++);
            }
        }
        return labels;
    }

    private static native void init();

    private static native void load(String path);

    private static native void train(long[] images, int[] labels, String path);

    private static native int predict(long image);
}
