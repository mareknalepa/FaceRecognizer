package pl.polsl.wkiro.facerecognizer.classifier;

import android.content.Context;

import org.opencv.core.Mat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FaceClassifier {

    private Context context;
    private String modelPath;
    private Map<Integer, String> labelsMap;
    private boolean classifierLoaded;

    public FaceClassifier(Context c) {
        System.loadLibrary("face_classifier_native");
        context = c;
        modelPath = context.getFilesDir() + File.separator + ClassifierDatabase.MODEL_FILE;
        labelsMap = new HashMap<>();
        classifierLoaded = false;
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
        storeLabelsMap(labelMap);
        classifierDatabase.deleteExamples();
        classifierLoaded = true;
    }

    public void loadClassifier() {
        if (fileExists(ClassifierDatabase.MODEL_FILE) && fileExists(ClassifierDatabase.LABELS_FILE)) {
            loadLabelsMap();
            load(modelPath);
            classifierLoaded = true;
        }
    }

    public String recognizeFace(Mat faceFrame) {
        if (classifierLoaded) {
            int predicted = predict(faceFrame.getNativeObjAddr());
            if (labelsMap.containsKey(predicted)) {
                return labelsMap.get(predicted);
            }
        }
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

    private void storeLabelsMap(Map<String, Integer> labelsMap) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(ClassifierDatabase.LABELS_FILE, Context.MODE_PRIVATE);
            for (Map.Entry<String, Integer> entry : labelsMap.entrySet()) {
                String strLabel = entry.getKey();
                int intLabel = entry.getValue();
                String line = intLabel + ";" + strLabel + System.getProperty("line.separator");
                fos.write(line.getBytes());
            }
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLabelsMap() {
        FileInputStream fis;
        try {
            fis = context.openFileInput(ClassifierDatabase.LABELS_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            while (line != null) {
                for (int i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == ';') {
                        String numericLabel = line.substring(0, i);
                        String strLabel = line.substring(i + 1);
                        int intLabel;
                        try {
                            intLabel = Integer.parseInt(numericLabel);
                        } catch (NumberFormatException e) {
                            intLabel = -1;
                        }
                        labelsMap.put(intLabel, strLabel);
                    }
                }
                line = br.readLine();
            }
            br.close();
            fis.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private boolean fileExists(String path) {
        File dir = context.getFilesDir();
        File file = new File(dir, path);
        return file.exists();
    }

    private static native void init();

    private static native void load(String path);

    private static native void train(long[] images, int[] labels, String path);

    private static native int predict(long image);
}
