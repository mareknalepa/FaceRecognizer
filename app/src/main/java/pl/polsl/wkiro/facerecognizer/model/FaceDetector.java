package pl.polsl.wkiro.facerecognizer.model;

import android.content.Context;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pl.polsl.wkiro.facerecognizer.R;

public class FaceDetector {

    private Context context;
    private CascadeClassifier classifier;

    public FaceDetector(Context context) {
        this.context = context;

        try {
            InputStream is = context.getResources().openRawResource(R.raw.lbpcascade_frontalface);
            File dir = context.getDir("lbpcascade", Context.MODE_PRIVATE);
            File file = new File(dir, "lbpcascade_frontalface.xml");
            FileOutputStream fos = new FileOutputStream(file);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            is.close();
            fos.close();

            loadDetector(file.getAbsolutePath());

            //noinspection ResultOfMethodCallIgnored
            dir.delete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void loadDetector(String absolutePath) {
        classifier = new CascadeClassifier(absolutePath);
        if (classifier.empty()) {
            classifier = null;
        }
    }

    public List<Face> detectFaces(Mat frame) {
        int minFaceSize = Math.round(frame.rows() * 0.2f);
        MatOfRect faceObjects = new MatOfRect();

        if (classifier == null) {
            return new ArrayList<>();
        }

        classifier.detectMultiScale(frame, faceObjects, 1.1, 2, 2, new Size(minFaceSize, minFaceSize), new Size());

        Rect[] faceRects = faceObjects.toArray();
        List<Face> faces = new ArrayList<>();
        for (Rect faceRect : faceRects) {
            faces.add(new Face(faceRect));
        }

        return faces;
    }
}
