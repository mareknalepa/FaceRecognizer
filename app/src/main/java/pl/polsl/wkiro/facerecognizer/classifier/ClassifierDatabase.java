package pl.polsl.wkiro.facerecognizer.classifier;

import android.content.Context;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.io.BufferedReader;
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
            fis = context.openFileInput("faces_list");
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
            fos = context.openFileOutput("faces_list", Context.MODE_APPEND);
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Date date = new Date();
            String path = df.format(date);
            while (pathExists(path)) {
                path += '_';
            }
            writeImageFile(path, image);
            String contents = label + ";" + path + System.getProperty("line.separator");
            fos.write(contents.getBytes());
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
            if (!path.equals("face_recognizer_model")) {
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
            if (path.equals("face_recognizer_model")) {
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
            FileInputStream imageFis = context.openFileInput(path);
            byte[] buffer = new byte[1024];
            StringBuilder sb = new StringBuilder();
            int length;
            while ((length = imageFis.read(buffer)) != -1) {
                sb.append(new String(buffer, 0, length));
            }
            byte[] fileContents = sb.toString().getBytes();
            imageFis.close();
            m.put(0, 0, fileContents);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    private void writeImageFile(String path, Mat image) {
        byte[] imageBytes = new byte[(int) (image.total() * image.channels())];
        image.get(0, 0, imageBytes);
        try {
            FileOutputStream imageFos = context.openFileOutput(path, Context.MODE_PRIVATE);
            imageFos.write(imageBytes);
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
