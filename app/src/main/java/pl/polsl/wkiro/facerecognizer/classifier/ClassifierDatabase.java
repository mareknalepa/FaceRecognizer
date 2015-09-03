package pl.polsl.wkiro.facerecognizer.classifier;

import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassifierDatabase {

    public final static int IMAGE_WIDTH = 256;
    public final static int IMAGE_HEIGHT = 256;

    public final static String IMAGES_LIST_FILE = "faces_list";
    public final static String MODEL_FILE = "face_recognizer_model";
    public final static String LABELS_FILE = "face_recognizer_labels";

    private Context context;
    private List<String> labels;
    private List<Mat> images;

    public ClassifierDatabase(Context c) {
        context = c;
        labels = new ArrayList<>();
        images = new ArrayList<>();
    }

    public void load() {
        labels.clear();
        images.clear();
        FileInputStream fis;
        try {
            fis = context.openFileInput(IMAGES_LIST_FILE);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line = br.readLine();
            while (line != null) {
                for (int i = 0; i < line.length(); ++i) {
                    if (line.charAt(i) == ';') {
                        String label = line.substring(0, i);
                        String filePath = line.substring(i + 1);
                        Mat image = readImageFile(filePath);
                        labels.add(label);
                        images.add(image);
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

    public void add(String label, Mat image) {
        labels.add(label);
        images.add(image);
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(IMAGES_LIST_FILE, Context.MODE_APPEND);
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Date date = new Date();
            String path = df.format(date);
            while (pathExists(path)) {
                path += '_';
            }
            writeImageFile(path, image);
            String contents = label + ";" + path + System.getProperty("line.separator");
            fos.write(contents.getBytes());
            fos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        String[] files = context.fileList();
        for (String path : files) {
            context.deleteFile(path);
        }
        load();
    }

    public void deleteExamples() {
        String[] files = context.fileList();
        for (String path : files) {
            if (!path.equals(MODEL_FILE) && !path.equals(LABELS_FILE)) {
                context.deleteFile(path);
            }
        }
        load();
    }

    public int examplesNumber() {
        return images.size();
    }

    public int classesNumber() {
        Set<String> uniqueLabels = new HashSet<>();
        for (String label : labels) {
            uniqueLabels.add(label);
        }
        return uniqueLabels.size();
    }

    public boolean isTrained() {
        String[] files = context.fileList();
        for (String path : files) {
            if (path.equals(MODEL_FILE)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getLabels() {
        return labels;
    }

    public List<Mat> getImages() {
        return images;
    }

    private Mat readImageFile(String path) {
        Mat m = new Mat(IMAGE_HEIGHT, IMAGE_WIDTH, CvType.CV_8UC1);
        try {
            File dir = context.getFilesDir();
            File imageFile = new File(dir, path);
            FileInputStream imageFis = context.openFileInput(path);
            byte[] buffer = new byte[(int) imageFile.length()];
            imageFis.read(buffer);
            imageFis.close();
            m.put(0, 0, buffer);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    private void writeImageFile(String path, Mat image) {
        byte[] buffer = new byte[(int) (image.total() * image.channels())];
        image.get(0, 0, buffer);
        try {
            FileOutputStream imageFos = context.openFileOutput(path, Context.MODE_PRIVATE);
            imageFos.write(buffer);
            imageFos.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private boolean pathExists(String path) {
        try {
            FileInputStream fis = context.openFileInput(path);
            fis.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
